package net.hannesrunelov.seedpass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;
import android.os.Handler;
import android.widget.ViewSwitcher;

public class PasswordGenerationHelper {

    public static void showGeneratePasswordDialog(final Context context,
                                                  final Service service,
                                                  final String key) {

        final String[] result = { "" };

        // Set up Master Password text box
        final EditText keyText = new EditText(context);
        keyText.setHint(R.string.hint_key);
        keyText.setSingleLine(true);
        keyText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // Set up password text
        final TextSwitcher resultSwitcher = new TextSwitcher(context);
        resultSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView resultText = new TextView(context);
                resultText.setTypeface(Typeface.MONOSPACE);
                resultText.setTextColor(Color.BLACK);
                resultText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f);
                return resultText;
            }
        });
        resultSwitcher.setVisibility(View.GONE);
        Animation in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        in.setDuration(100);
        out.setDuration(100);
        resultSwitcher.setInAnimation(in);
        resultSwitcher.setOutAnimation(out);

        // Set up Show checkbox
        final CheckBox showToggle = new CheckBox(context);
        showToggle.setText(R.string.button_show);
        showToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                resultSwitcher.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        // Group them together
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(keyText);
        layout.addView(showToggle);
        layout.addView(resultSwitcher);
        keyText.requestFocus();

        // Set up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder .setTitle(R.string.title_generate)
                .setMessage(service.name)
                .setView(layout)
                .setNegativeButton(R.string.button_cancel, null)
                .setPositiveButton(R.string.button_copy, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Warn about clipboard history
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                        builder2.setTitle(R.string.title_warning)
                                .setMessage(R.string.msg_warning)
                                .setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        showGeneratePasswordDialog(context, service, keyText.getText().toString());
                                    }
                                })
                                .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        // Copy generated password
                                        ClipboardManager clipboard =
                                                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText(context.getString(R.string.password), result[0]);
                                        clipboard.setPrimaryClip(clip);

                                        Toast.makeText(context,
                                                String.format(context.getString(R.string.password_copied),
                                                        context.getResources().getInteger(
                                                                R.integer.password_expiration_time)),
                                                Toast.LENGTH_LONG).show();

                                        // Start expiration
                                        Intent intent = new Intent(context, PasswordExpirationService.class);
                                        intent.putExtra(context.getString(R.string.password), result[0]);
                                        context.startService(intent);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                MainActivity.getInstance().finish();
                                            }
                                        }, 200);
                                    }
                                });
                        builder2.show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

        // Enable/disable button
        final Button copyButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        copyButton.setEnabled(keyText.length() > 0);
        keyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                copyButton.setEnabled(keyText.length() > 0);
                result[0] = generatePassword(service, s.toString());
                resultSwitcher.setText(" " + result[0]);
            }
        });
        if (key != null) keyText.setText(key);
    }
    public static void showGeneratePasswordDialog(final Context context, final Service service) {
        showGeneratePasswordDialog(context, service, null);
    }

    public static void showExportPasswordsDialog(final Context context, final Set<Service> services) {

        // Set up Master Password text box
        final EditText keyText = new EditText(context);
        keyText.setHint(R.string.hint_key);
        keyText.setSingleLine(true);
        keyText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // Set up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder .setTitle(R.string.title_export)
                .setView(keyText)
                .setPositiveButton(R.string.button_export, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        /*
                        // Save passwords to file
                        try {
                            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                            dir.mkdirs();
                            File file = new File(dir.getAbsolutePath().toString() + "/" +
                                    context.getString(R.string.export_filename));
                            Log.e("File", file.getAbsolutePath().toString());
                            file.createNewFile();
                            FileOutputStream out = new FileOutputStream(file);
                            OutputStreamWriter writer =
                                    new OutputStreamWriter(out);

                            String key = keyText.getText().toString();
                            for (String a : services) {
                                writer.append(a + ":\n" + generatePassword(a, key));
                            }
                            writer.close();
                            out.close();
                            Toast.makeText(context,
                                    R.string.export_success + " " + file.getAbsolutePath().toString(),
                                    Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, R.string.export_fail,
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        */
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

        // Enable/disable button
        final Button exportButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        exportButton.setEnabled(false);
        keyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                exportButton.setEnabled(keyText.length() > 0);
            }
        });
    }

    private static String generatePassword(Service service, String key) {
        if (key.equals("")) return "";
        return PasswordGenerator.getPassword((service.name + key).toLowerCase(), service.include);
    }
}
