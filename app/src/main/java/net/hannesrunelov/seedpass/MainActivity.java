package net.hannesrunelov.seedpass;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listView;
    private ServiceListAdapter listAdapter;
    private RecyclerView.LayoutManager listLayout;
    private FloatingActionButton addButton;
    private Set<String> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
        initDeleteItems();
        initAddItems();
        initNotification();
    }

    private void initWidgets() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (RecyclerView)findViewById(R.id.serviceList);
        addButton = (FloatingActionButton)findViewById(R.id.addButton);

        setSupportActionBar(toolbar);

        // Service List
        listView.setHasFixedSize(true);
        listLayout = new LinearLayoutManager(this);
        listView.setLayoutManager(listLayout);

        // Load
        services = PreferencesHelper.read(this);
        listAdapter = new ServiceListAdapter(services);
        listView.setAdapter(listAdapter);
    }

    private void initDeleteItems() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                // Remove service
                int position = viewHolder.getAdapterPosition();
                services.remove(listAdapter.serviceAt(position));
                listAdapter.notifyItemRemoved(position);

                // Save
                PreferencesHelper.write(MainActivity.this, services);
            }
        });
        itemTouchHelper.attachToRecyclerView(listView);
    }

    private void initAddItems() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Set up Name text box
                final EditText nameText = new EditText(MainActivity.this);
                nameText.setHint(R.string.hint_name);
                nameText.setSingleLine(true);
                nameText.setFilters(new InputFilter[] {
                        new InputFilter.LengthFilter(16)
                });

                // Set up dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder .setTitle(R.string.title_add)
                        .setView(nameText)
                        .setNegativeButton(R.string.button_cancel, null)
                        .setPositiveButton(R.string.button_add, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Add service to list
                                String name = nameText.getText().toString();
                                if (name.equals("")) return;
                                int oldSize = services.size();
                                services.add(name);
                                if (services.size() > oldSize) {
                                    int position = listAdapter.positionOf(name);
                                    listAdapter.notifyItemInserted(position);
                                }

                                // Save
                                PreferencesHelper.write(MainActivity.this, services);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();

                // Enable/disable button
                final Button addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                addButton.setEnabled(false);
                nameText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        addButton.setEnabled(s.length() > 0);
                    }
                });
            }
        });
    }

    private void initNotification() {
        // TODO
        /* RemoteViews rv = new RemoteViews(getPackageName());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder .setContentTitle("Generate Password")
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setOngoing(true);

        // Add services
        for (String name : services) {
            builder.addAction(R.drawable.ic_key, name, null);
        }

        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(42, notification);
        */
    }
}
