package net.hannesrunelov.seedpass;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static Activity getInstance() {
        return instance;
    }
    private static Activity instance;

    private RecyclerView listView;
    private ServiceListAdapter listAdapter;
    private FloatingActionButton addButton;
    private TextView helpLabel;
    private Set<Service> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.title_select);

        instance = this;

        initWidgets();
        initDeleteItems();
        initAddItems();
        initNotification();
    }

    private void initWidgets() {
        listView = (RecyclerView)findViewById(R.id.serviceList);
        addButton = (FloatingActionButton)findViewById(R.id.addButton);
        helpLabel = (TextView)findViewById(R.id.helpLabel);

        // Service List
        listView.setHasFixedSize(true);
        LinearLayoutManager listLayout = new LinearLayoutManager(this);
        listView.setLayoutManager(listLayout);

        // Load
        services = PreferencesHelper.read(this);
        listAdapter = new ServiceListAdapter(services);
        listView.setAdapter(listAdapter);
        helpLabel.setVisibility(services.size() > 0 ? View.INVISIBLE : View.VISIBLE);
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
                helpLabel.setVisibility(services.size() > 0 ? View.INVISIBLE : View.VISIBLE);

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
                        new InputFilter.LengthFilter(getResources().getInteger(
                                R.integer.max_service_name_length))
                });

                // Set up Include checkboxes
                final CheckBox uppercaseCheckBox = new CheckBox(MainActivity.this);
                uppercaseCheckBox.setText(R.string.label_uppercase);
                uppercaseCheckBox.setChecked(true);
                final CheckBox lowercaseCheckBox = new CheckBox(MainActivity.this);
                lowercaseCheckBox.setText(R.string.label_lowercase);
                lowercaseCheckBox.setChecked(true);
                final CheckBox numberCheckBox = new CheckBox(MainActivity.this);
                numberCheckBox.setText(R.string.label_numbers);
                numberCheckBox.setChecked(true);
                final CheckBox symbolCheckBox = new CheckBox(MainActivity.this);
                symbolCheckBox.setText(R.string.label_symbols);
                symbolCheckBox.setChecked(true);

                // Group them together
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(nameText);
                layout.addView(uppercaseCheckBox);
                layout.addView(lowercaseCheckBox);
                layout.addView(numberCheckBox);
                layout.addView(symbolCheckBox);
                nameText.requestFocus();

                // Set up dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder .setTitle(R.string.title_add)
                        .setView(layout)
                        .setNegativeButton(R.string.button_cancel, null)
                        .setPositiveButton(R.string.button_add, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Add service to list
                                byte include = 0b0000;
                                if (uppercaseCheckBox.isChecked()) include |= 0b0001;
                                if (lowercaseCheckBox.isChecked()) include |= 0b0010;
                                if (numberCheckBox.isChecked()) include |= 0b0100;
                                if (symbolCheckBox.isChecked()) include |= 0b1000;
                                Log.w("Include", String.valueOf(include));
                                Service service = new Service(nameText.getText().toString(), include);
                                if (service.name.equals("")) return;
                                int oldSize = services.size();
                                services.add(service);
                                if (services.size() > oldSize) {
                                    int position = listAdapter.positionOf(service);
                                    listAdapter.notifyItemInserted(position);
                                    helpLabel.setVisibility(services.size() > 0 ? View.INVISIBLE : View.VISIBLE);
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
                        addButton.setEnabled(s.length() > 0 && (uppercaseCheckBox.isChecked() ||
                                                                lowercaseCheckBox.isChecked() ||
                                                                numberCheckBox.isChecked() ||
                                                                symbolCheckBox.isChecked()));
                    }
                });
                CheckBox.OnCheckedChangeListener checkListener = new CheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        addButton.setEnabled(nameText.length() > 0 && (uppercaseCheckBox.isChecked() ||
                                                                lowercaseCheckBox.isChecked() ||
                                                                numberCheckBox.isChecked() ||
                                                                symbolCheckBox.isChecked()));
                    }
                };
                uppercaseCheckBox.setOnCheckedChangeListener(checkListener);
                lowercaseCheckBox.setOnCheckedChangeListener(checkListener);
                numberCheckBox.setOnCheckedChangeListener(checkListener);
                symbolCheckBox.setOnCheckedChangeListener(checkListener);
            }
        });
    }

    private void initNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pintent = PendingIntent.getActivity(this, 42, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_seedpass_icon_flat);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder .setContentTitle(getString(R.string.notification))
                .setContentIntent(pintent)
                .setSmallIcon(R.drawable.ic_seedpass_icon_mono)
                .setLargeIcon(icon)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setShowWhen(false)
                .setOngoing(true);

        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(42, notification);
    }
}
