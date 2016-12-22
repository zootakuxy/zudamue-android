package st.domain.support.android.text;

/**
 * Created by xdata on 7/25/16.
 */
public class TextException extends Error
{
    public TextException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextException(String message) {
        super(message);
    }
}
