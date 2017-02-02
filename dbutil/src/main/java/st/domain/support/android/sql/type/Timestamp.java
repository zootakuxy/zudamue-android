package st.domain.support.android.sql.type;

/**
 * Created by xdata on 1/1/17.
 */

public class Timestamp extends java.sql.Timestamp implements CharSequence {

    public Timestamp(int year, int month, int date, int hour, int minute, int second, int nano) {
        super(year, month, date, hour, minute, second, nano);
    }

    public Timestamp(long time) {
        super(time);
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
