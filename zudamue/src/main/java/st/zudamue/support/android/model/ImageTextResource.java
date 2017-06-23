package st.zudamue.support.android.model;

/**
 * Created by xdata on 7/30/16.
 */
public class ImageTextResource implements CharSequence
{
    private CharSequence text;
    int idImageResource;

    public ImageTextResource(CharSequence title, int idImageDrawble)
    {
        this.idImageResource = idImageDrawble;
        this.text  = title;
    }

    @Override
    public int length()
    {
        return text.length();
    }

    @Override
    public char charAt(int index)
    {
        return text.charAt(index);
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        return this.text.subSequence(beginIndex, endIndex);
    }

    @Override
    public String toString()
    {
        return (this.text == null)? "": this.text.toString();
    }

    public CharSequence getText() {
        return text;
    }

    public int getIdImageResource() {
        return idImageResource;
    }
}