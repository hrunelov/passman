package net.hannesrunelov.passman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PassList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_list);
        StringBuilder sb = new StringBuilder();
        int i = 0b1111;
        sb.append(PasswordGenerator.getPassword("asdf1234", 64, i)+"\n");
        sb.append(PasswordGenerator.getPassword("1234asdf", 64, i)+"\n");
        Log.w("Password", sb.toString());
    }
}
