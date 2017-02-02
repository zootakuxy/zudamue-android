package st.domain.support.android.sql.sqlite;

import st.domain.support.android.sql.type.Date;
import st.domain.support.android.sql.type.Time;
import st.domain.support.android.sql.type.Timestamp;

/**
 *
 * Created by xdata on 1/1/17.
 */

public class Convert {

    public static String varchar(String varchar) {
        return  varchar;
    }

    public static Date date(java.util.Date date) {
        return  new Date(date.getTime());
    }

    public static Time time(Date time) {
        return new Time(time.getTime());
    }

    public static Timestamp timestamp(Date timestamp) {
        return  new Timestamp(timestamp.getTime());
    }

    public static byte[] blob(byte ... bytesArray) {
        return bytesArray;
    }
}
