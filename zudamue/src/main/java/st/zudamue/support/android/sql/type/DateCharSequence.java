package st.zudamue.support.android.sql.type;

import java.sql.Date;

/**
 *
 * Created by xdata on 1/1/17.
 */

public class DateCharSequence extends BaseTypeCharSequence<Date> {

    public DateCharSequence(Date date){
        super(date);
    }

    public DateCharSequence(int year, int month, int day) {
        this(new Date(year, month, day));
    }

    public DateCharSequence(long date) {
        this(new Date(date));
    }

    @Override
    public String toString() {
        return String.valueOf(this.value());
    }
}
