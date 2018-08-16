package st.zudamue.support.android.util.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by xdaniel on 7/26/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
public class ZMoney extends Number implements Comparable<ZMoney>, CharSequence, Serializable
{
    private double value;
    private NumberFormat numberFormat;
    private String format;

    private static  NumberFormat format()
    {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("PT", "st"));
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        format.setCurrency(Currency.getInstance("STD"));
        return format;
    }

    public ZMoney() {
        this(0);
    }

    public ZMoney(double value)
    {
        this.value = value;
        this.numberFormat = format();
        this.format = numberFormat.format(this.value);
    }

    public ZMoney(String money)
    {
        this.format = money;
        this.numberFormat = format();
        try
        {
            value = this.numberFormat.parse(money).doubleValue();
        } catch (ParseException e)
        {
            throw new NumberFormatException("Can not format "+money+" to money");
        }
    }

    public ZMoney(ZMoney ZMoney)
    {
        this.value = ZMoney.value;
        this.format = ZMoney.format;
        this.numberFormat = ZMoney.numberFormat;
    }

    @Override
    public int length()
    {
        return format.length();
    }

    @Override
    public char charAt(int index)
    {
        return format.charAt(index);
    }

    @Override
    public CharSequence subSequence(int i, int i1) {
        return null;
    }

    @Override
    public int compareTo(ZMoney s)
    {
        Double value = this.value;
        return value.compareTo(s.value);
    }

    @Override
    public int intValue()
    {
        Double value = this.value;
        return value.intValue();
    }

    @Override
    public String toString()
    {
        return this.format;
    }

    @Override
    public long longValue()
    {
        Double value = this.value;
        return value.longValue();
    }

    @Override
    public float floatValue()
    {
        Double value = this.value;
        return value.floatValue();
    }

    @Override
    public double doubleValue()
    {
        return value;
    }

    /**
     * Somar dois valores em moeda
     * @param value1
     * @param value2
     * @return
     */
    public static ZMoney sum(ZMoney value1, ZMoney value2)
    {
        if(value1 == null || value2 == null)
            throw new NumberFormatException("Can not sum the moneys null");
        double result = value1.value + value2.value;
        return new ZMoney(result);
    }
    /**
     * Somar dois valores em moeda
     * @param value1
     * @param value2
     * @return
     */
    public static ZMoney sub(ZMoney value1, ZMoney value2)
    {
        if(value1 == null || value2 == null)
            throw new NumberFormatException("Can not sub the moneys null");
        double result = value1.value - value2.value;
        return new ZMoney(result);
    }


    /**
     * Somar dois valores em moeda
     * @param value1
     * @param value2
     * @return
     */
    public static ZMoney mult(ZMoney value1, ZMoney value2)
    {
        if(value1 == null || value2 == null)
            throw new NumberFormatException("Can not mult the moneys null");
        double result = value1.value * value2.value;
        return new ZMoney(result);
    }

    public static ZMoney div(ZMoney value1, ZMoney value2)
    {
        if(value1 == null || value2 == null)
            throw new NumberFormatException("Can not div the moneys null");
        double result = value1.value / value2.value;
        return new ZMoney(result);
    }
}
