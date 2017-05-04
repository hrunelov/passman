package net.hannesrunelov.passman;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class AccountInfo implements Comparable<AccountInfo>, Serializable {

    private static final long serialVersionUID = 4736214666399900542L;

    public final String account;
    public final int length;
    public final int include;

    public AccountInfo(String account, int length, int include) {
        this.account = account;
        this.length = length;
        this.include = include;
    }

    public AccountInfo(String account, int length) {
        this(account, length, 0b1111);
    }

    public AccountInfo(String account) {
        this(account, PasswordGenerator.DEFAULT_LENGTH, 0b1111);
    }

    @Override
    public int compareTo(@NonNull AccountInfo o) {
        return this.account.compareTo(o.account);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AccountInfo)) return false;

        AccountInfo other = (AccountInfo)o;
        return  this.account.equals(other.account) &&
                this.length == other.length &&
                this.include == other.include;
    }

    @Override
    public int hashCode() {
        int result = account.hashCode();
        result = 31 * result + length;
        result = 31 * result + include;
        return result;
    }
}
