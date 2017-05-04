package net.hannesrunelov.passman;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Main extends AppCompatActivity {

    private RecyclerView listView;
    private RecyclerView.Adapter listAdapter;
    private RecyclerView.LayoutManager listLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (RecyclerView)findViewById(R.id.accountList);
        listView.setHasFixedSize(true);

        listLayout = new LinearLayoutManager(this);
        listView.setLayoutManager(listLayout);

        AccountInfo[] data = {
                new AccountInfo("Herp",32,0b0100),
                new AccountInfo("Derp",24,0b1100),
                new AccountInfo("Hurr",64,0b0101),
                new AccountInfo("Durr",72,0b1111)
        };
        listAdapter = new AccountListAdapter(data);
        listView.setAdapter(listAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
