package st.zudamue.support.android.exception;

/**
 * Created by xdaniel on 6/25/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public class ZudamueError extends Error {
    public ZudamueError() {
    }

    public ZudamueError(String message) {
        super(message);
    }

    public ZudamueError(String message, Throwable cause) {
        super(message, cause);
    }

    public ZudamueError(Throwable cause) {
        super(cause);
    }

    public ZudamueError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
