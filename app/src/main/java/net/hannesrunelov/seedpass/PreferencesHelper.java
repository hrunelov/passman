package net.hannesrunelov.seedpass;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;
import java.util.TreeSet;

public class PreferencesHelper {

    public static void write(Activity activity, Set<String> services) {
        SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        editor.putStringSet(activity.getString(R.string.services), services);
        editor.commit();
    }

    public static Set<String> read(Activity activity) {
        SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
        Set<String> data = prefs.getStringSet(activity.getResources().getString(R.string.services),
                null);
        Set<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        if (data != null) {
            for (String a : data) result.add(a);
        }
        return result;
    }
}
