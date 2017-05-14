package net.hannesrunelov.seedpass;

import android.support.annotation.NonNull;

public class Service implements Comparable<Service> {

    public final String name;
    public final byte include;

    public Service(String name, byte include) {
        this.name = name;
        this.include = include;
    }

    @Override
    public int compareTo(Service other) {
        return name.toLowerCase().compareTo(other.name.toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Service)) return false;
        Service other = (Service)o;
        return name.equals(other.name) && include == other.include;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return name
                + (((include & PasswordGenerator.SYMBOLS) == PasswordGenerator.SYMBOLS) ? "1" : "0")
                + (((include & PasswordGenerator.NUMBERS) == PasswordGenerator.NUMBERS) ? "1" : "0")
                + (((include & PasswordGenerator.LOWERCASE) == PasswordGenerator.LOWERCASE) ? "1" : "0")
                + (((include & PasswordGenerator.UPPERCASE) == PasswordGenerator.UPPERCASE) ? "1" : "0");
    }
}
