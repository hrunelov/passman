package net.hannesrunelov.seedpass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ServiceListItemView extends CardView {

    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (serviceView == null) serviceView = (TextView)findViewById(R.id.serviceLabel);
        if (serviceView == null) return;
        this.name = name;
        serviceView.setText(name);
    }

    private NameChangedListener nameChangedListener = null;
    private TextView serviceView;

    public ServiceListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private void init(final Context context, AttributeSet attrs) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ServiceListItemView);

        // Password generation dialog
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordGenerationHelper.showGeneratePasswordDialog(context, name);
            }
        });

        // Rename dialog
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Set up Name text box
                final EditText nameText = new EditText(context);
                nameText.setHint(R.string.hint_name);
                nameText.setSingleLine(true);
                nameText.setFilters(new InputFilter[] {
                        new InputFilter.LengthFilter(16)
                });
                nameText.setText(name);
                nameText.selectAll();

                // Set up dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder .setTitle(R.string.title_rename)
                        .setView(nameText)
                        .setNegativeButton(R.string.button_cancel, null)
                        .setPositiveButton(R.string.button_rename, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Update Service
                                String newName = nameText.getText().toString();
                                if (!newName.equals(name)) {
                                    String oldName = name;
                                    setName(newName);
                                    nameChangedListener.onNameChanged(oldName, newName);
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();

                // Enable/disable button
                final Button renameButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                nameText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        renameButton.setEnabled(s.length() > 0);
                    }
                });
                return true;
            };
        });

        a.recycle();
    }

    public void setNamehangedListener(NameChangedListener l) {
        nameChangedListener = l;
    }

    public static abstract class NameChangedListener {
        public abstract void onNameChanged(String oldName, String newName);
    }
}
