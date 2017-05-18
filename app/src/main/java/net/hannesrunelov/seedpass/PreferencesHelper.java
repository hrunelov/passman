package net.hannesrunelov.seedpass;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class PreferencesHelper {

    public static void write(Activity activity, Set<Service> services) {
        SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

        Set<String> data = new TreeSet<>();
        for (Service service : services) data.add(service.toString());

        editor.putStringSet(activity.getString(R.string.services), data);
        editor.commit();
    }

    public static Set<Service> read(Activity activity) {
        SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
        Set<String> data = prefs.getStringSet(activity.getResources().getString(R.string.services),
                null);
        Set<Service> result = new TreeSet<>();
        if (data != null) {
            for (String str : data) {
                result.add(new Service(str.substring(0, str.length()-4),
                        Byte.parseByte(str.substring(str.length()-4), 2)));
            }
        }
        return result;
    }
}
