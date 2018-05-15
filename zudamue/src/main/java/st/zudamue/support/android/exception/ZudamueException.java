package st.zudamue.support.android.exception;

/**
 * Created by xdaniel on 6/25/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
public class ZudamueException extends RuntimeException {
    public ZudamueException() {
    }

    public ZudamueException(String message) {
        super(message);
    }

    public ZudamueException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZudamueException(Throwable cause) {
        super(cause);
    }

    public ZudamueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
