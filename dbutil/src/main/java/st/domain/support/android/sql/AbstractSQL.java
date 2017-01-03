package st.domain.support.android.sql;

/**
 *
 * Created by xdata on 12/31/16.
 */

public abstract class AbstractSQL implements SQL {

    @Override
    public int length() {
        return this.sql().length();
    }

    @Override
    public char charAt(int index) {
        return this.sql().charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return this.sql().subSequence(start, end);
    }

}
