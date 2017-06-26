package st.zudamue.support.android.exception;

import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by xdaniel on 7/23/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
public class SQLiteException extends ZudamueException {

    public SQLiteException() { }

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
