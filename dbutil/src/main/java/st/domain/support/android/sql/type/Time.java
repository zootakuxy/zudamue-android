package st.domain.support.android.sql.type;

/**
 * Created by xdata on 1/1/17.
 */

public class Time extends java.sql.Time implements CharSequence {

    public Time(int year, int month, int day) {
        super(year, month, day);
    }

    public Time(long date) {
        super(date);
    }

    @Override
    public int length() {
        return String.valueOf(this).length();
    }

    @Override
    public char charAt(int index) {
        return String.valueOf(this).charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return String.valueOf(this).subSequence(start, end);
    }
}
