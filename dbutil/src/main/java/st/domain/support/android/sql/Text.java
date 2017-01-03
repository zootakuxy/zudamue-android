package st.domain.support.android.sql;

/**
 *
 * Created by xdata on 12/31/16.
 */

public class Text extends Argument {

    private String text;

    public Text(String text){
        this.text = text;
    }

    @Override
    public String argument() {
        return this.text;
    }

    public static Text text(String text) {
        return new Text(text);
    }
}
