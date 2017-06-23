package st.zudamue.support.android.sql.type;

import java.sql.Time;

/**
 *
 * Created by xdata on 1/1/17.
 */

public class TimeCharSequence extends BaseTypeCharSequence<Time> {


    public TimeCharSequence(Time value) {
        super(value);
    }
    public TimeCharSequence(int hour, int minute, int second) {
        this(new Time(hour, minute, second));
    }

    public TimeCharSequence(long time) {
        this(new Time(time));
    }

}
