package net.hannesrunelov.passman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PassList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_list);

        TextView text = (TextView)this.findViewById(R.id.text);
        StringBuilder sb = new StringBuilder();
        sb.append(PasswordGenerator.getPassword("", "", 32)+"\n");
        sb.append(PasswordGenerator.getPassword("a", "b", 32)+"\n");
        sb.append(PasswordGenerator.getPassword("a", "b", 32)+"\n");
        sb.append(PasswordGenerator.getPassword("aa", "bb", 32)+"\n");
        sb.append(PasswordGenerator.getPassword("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC", "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC", 32)+"\n");
        text.setText(sb.toString());
    }
}
