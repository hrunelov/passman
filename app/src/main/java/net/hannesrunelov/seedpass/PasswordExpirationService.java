package net.hannesrunelov.seedpass;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class PasswordExpirationService extends IntentService {

    private Handler handler;

    public PasswordExpirationService() {
        super(null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String password = intent.getStringExtra(getString(R.string.password));
        Long startTime = System.currentTimeMillis();

        while(System.currentTimeMillis() - startTime < getResources().getInteger(
                R.integer.password_expiration_time)*1000);

        ClipboardManager clipboard =
                (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip.getItemAt(0).getText().equals(password)) {
            clipboard.setPrimaryClip(ClipData.newPlainText("Empty"," "));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PasswordExpirationService.this, getString(R.string.password_expired),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
