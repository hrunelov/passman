package net.hannesrunelov.seedpass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServiceListItemView extends LinearLayout {

    private Service service;
    public Service getService() {
        return service;
    }
    public void setService(Service service) {
        if (nameLabel == null) nameLabel = (TextView)findViewById(R.id.serviceLabel);
        if (nameLabel == null) return;
        if (includeLabel == null) includeLabel = (TextView)findViewById(R.id.includeLabel);
        if (nameLabel == null) return;
        this.service = service;
        nameLabel.setText(service.name);

        // Password preview
        StringBuilder sb = new StringBuilder();
        if ((service.include & PasswordGenerator.UPPERCASE) == PasswordGenerator.UPPERCASE) {
            sb.append("A");
            if (!((service.include & PasswordGenerator.LOWERCASE) == PasswordGenerator.LOWERCASE)) {
                sb.append("BC");
            }
        }
        if ((service.include & PasswordGenerator.LOWERCASE) == PasswordGenerator.LOWERCASE) {
            sb.append("a");
            if (!((service.include & PasswordGenerator.UPPERCASE) == PasswordGenerator.UPPERCASE)) {
                sb.append("bc");
            }
        }
        if ((service.include & PasswordGenerator.NUMBERS) == PasswordGenerator.NUMBERS) {
            sb.append("123");
        }
        if ((service.include & PasswordGenerator.SYMBOLS) == PasswordGenerator.SYMBOLS) {
            sb.append("#$&");
        }
        includeLabel.setText(sb.toString());
    }

    private ServiceChangedListener serviceChangedListener = null;
    private TextView nameLabel;
    private TextView includeLabel;

    public ServiceListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ServiceListItemView);

        // Password generation dialog
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordGenerationHelper.showGeneratePasswordDialog(context, service);
            }
        });

        // Edit dialog
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
                nameText.setText(service.name);
                nameText.selectAll();

                // Set up Include checkboxes
                final CheckBox uppercaseCheckBox = new CheckBox(context);
                uppercaseCheckBox.setText(R.string.label_uppercase);
                uppercaseCheckBox.setChecked(
                        (service.include & PasswordGenerator.UPPERCASE) == PasswordGenerator.UPPERCASE);
                final CheckBox lowercaseCheckBox = new CheckBox(context);
                lowercaseCheckBox.setText(R.string.label_lowercase);
                lowercaseCheckBox.setChecked(
                        (service.include & PasswordGenerator.LOWERCASE) == PasswordGenerator.LOWERCASE);
                final CheckBox numberCheckBox = new CheckBox(context);
                numberCheckBox.setText(R.string.label_numbers);
                numberCheckBox.setChecked(
                        (service.include & PasswordGenerator.NUMBERS) == PasswordGenerator.NUMBERS);
                final CheckBox symbolCheckBox = new CheckBox(context);
                symbolCheckBox.setText(R.string.label_symbols);
                symbolCheckBox.setChecked(
                        (service.include & PasswordGenerator.SYMBOLS) == PasswordGenerator.SYMBOLS);

                // Group them together
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(nameText);
                layout.addView(uppercaseCheckBox);
                layout.addView(lowercaseCheckBox);
                layout.addView(numberCheckBox);
                layout.addView(symbolCheckBox);
                nameText.requestFocus();

                // Set up dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder .setTitle(R.string.title_edit)
                        .setView(layout)
                        .setNegativeButton(R.string.button_cancel, null)
                        .setPositiveButton(R.string.button_save, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Update Service
                                byte include = 0b0000;
                                if (uppercaseCheckBox.isChecked()) include |= 0b0001;
                                if (lowercaseCheckBox.isChecked()) include |= 0b0010;
                                if (numberCheckBox.isChecked()) include |= 0b0100;
                                if (symbolCheckBox.isChecked()) include |= 0b1000;
                                Service newService = new Service(nameText.getText().toString(), include);
                                if (!newService.equals(service)) {
                                    Service oldService = service;
                                    setService(newService);
                                    serviceChangedListener.onServiceChanged(oldService, newService);
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();

                // Enable/disable button
                final Button saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                nameText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        saveButton.setEnabled(s.length() > 0 && (uppercaseCheckBox.isChecked() ||
                                                                 lowercaseCheckBox.isChecked() ||
                                                                 numberCheckBox.isChecked() ||
                                                                 symbolCheckBox.isChecked()));
                    }
                });
                CheckBox.OnCheckedChangeListener checkListener = new CheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        saveButton.setEnabled(nameText.length() > 0 && (uppercaseCheckBox.isChecked() ||
                                                                        lowercaseCheckBox.isChecked() ||
                                                                        numberCheckBox.isChecked() ||
                                                                        symbolCheckBox.isChecked()));
                    }
                };
                uppercaseCheckBox.setOnCheckedChangeListener(checkListener);
                lowercaseCheckBox.setOnCheckedChangeListener(checkListener);
                numberCheckBox.setOnCheckedChangeListener(checkListener);
                symbolCheckBox.setOnCheckedChangeListener(checkListener);
                return true;
            };
        });

        a.recycle();
    }

    public void setServiceChangedListener(ServiceChangedListener l) {
        serviceChangedListener = l;
    }

    public static abstract class ServiceChangedListener {
        public abstract void onServiceChanged(Service oldService, Service newService);
    }
}
