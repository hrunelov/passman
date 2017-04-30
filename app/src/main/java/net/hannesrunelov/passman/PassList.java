package net.hannesrunelov.passman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class PassList extends AppCompatActivity {

    private EditText name;
    private EditText key;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_list);

        name = (EditText)findViewById(R.id.name);
        key = (EditText)findViewById(R.id.key);
        password = (EditText)findViewById(R.id.password);

        TextWatcher listener = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getPassword();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        name.addTextChangedListener(listener);
        key.addTextChangedListener(listener);
    }

    private void getPassword() {
        password.setText(PasswordGenerator.getPassword(name.getText().toString() + key.getText().toString()));
    }
}
