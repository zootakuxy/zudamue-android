package st.domain.support.android.sql;

import android.support.annotation.NonNull;

/**
 * Created by xdata on 12/31/16.
 */

public abstract class Argument implements CharSequence {

    public abstract String argument();

    @Override
    public int length() {
        return argument().length();
    }

    @Override
    public char charAt(int index) {
        return argument().charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return argument().subSequence(start, end);
    }

    @NonNull
    @Override
    public String toString() {
        return argument();
    }
}
