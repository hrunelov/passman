package net.hannesrunelov.passman;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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

        // Account List
        listView = (RecyclerView)findViewById(R.id.accountList);
        listView.setHasFixedSize(true);

        listLayout = new LinearLayoutManager(this);
        listView.setLayoutManager(listLayout);

        final List<String> accounts = new ArrayList<>();
        listAdapter = new AccountListAdapter(accounts);
        listView.setAdapter(listAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int index = viewHolder.getAdapterPosition();
                accounts.remove(index);
                listAdapter.notifyItemRemoved(index);
            }
        });
        itemTouchHelper.attachToRecyclerView(listView);

        // Add Button
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accounts.add("Account " + (accounts.size()+1));
                listAdapter.notifyItemInserted(accounts.size());
            }
        });
    }
}
