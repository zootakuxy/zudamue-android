package st.zudamue.support.android.exception;

/**
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 *
 * Created by xdaniel on 7/25/16.
 */
public class TextException extends ZudamueError
{
    public TextException( String message, Throwable cause) {
        super(message, cause);
    }

    public TextException(String message) {
        super(message);
    }
}
