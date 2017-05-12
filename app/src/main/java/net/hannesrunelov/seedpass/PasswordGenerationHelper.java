package net.hannesrunelov.seedpass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Set;

public class PasswordGenerationHelper {

    public static void showGeneratePasswordDialog(final Context context,
                                                  final String service,
                                                  final String key) {

        final String[] result = { "" };

        // Set up Master Password text box
        final EditText keyText = new EditText(context);
        keyText.setHint(R.string.hint_key);
        keyText.setSingleLine(true);
        keyText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // Set up password text
        final TextView resultText = new TextView(context);
        resultText.setTypeface(Typeface.MONOSPACE);
        resultText.setTextColor(Color.BLACK);
        resultText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f);
        resultText.setVisibility(View.GONE);

        // Set up Show checkbox
        final CheckBox showToggle = new CheckBox(context);
        showToggle.setText(R.string.button_show);
        showToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                resultText.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        // Group them together
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(keyText);
        layout.addView(showToggle);
        layout.addView(resultText);
        keyText.requestFocus();

        // Set up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder .setTitle(R.string.title_generate)
                .setMessage(service)
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
                                                R.string.password_copied,
                                                Toast.LENGTH_LONG).show();
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
        keyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                copyButton.setEnabled(keyText.length() > 0);
                result[0] = generatePassword(service, s.toString());
                resultText.setText(" " + result[0]);
            }
        });
        if (key != null) keyText.setText(key);
    }
    public static void showGeneratePasswordDialog(final Context context, final String service) {
        showGeneratePasswordDialog(context, service, null);
    }

    public static void showExportPasswordsDialog(final Context context, final Set<String> services) {

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

    private static String generatePassword(String name, String key) {
        if (key.equals("")) return "";
        return PasswordGenerator.getPassword((name + key).toLowerCase());
    }
}
