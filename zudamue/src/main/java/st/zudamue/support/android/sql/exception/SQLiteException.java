package st.zudamue.support.android.sql.exception;

import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 *
 * Created by xdata on 7/23/16.
 */
public class SQLiteException extends RuntimeException {

    public SQLiteException() {
    }

    public SQLiteException(String message) {
        super(message);
    }

    public SQLiteException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLiteException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public SQLiteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
