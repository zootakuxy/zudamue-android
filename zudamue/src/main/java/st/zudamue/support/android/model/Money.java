package st.zudamoe.support.android.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by xdata on 7/26/16.
 */
public class Money extends Number implements Comparable<Money>, CharSequence, Serializable
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

    public Money() {
        this(0);
    }

    public Money (double value)
    {
        this.value = value;
        this.numberFormat = format();
        this.format = numberFormat.format(this.value);
    }

    public Money(String money)
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

    public Money(Money money)
    {
        this.value = money.value;
        this.format = money.format;
        this.numberFormat = money.numberFormat;
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
    public int compareTo(Money s)
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
    public static Money sum(Money value1, Money value2)
    {
        if(value1 == null || value2 == null)
            throw new NumberFormatException("Can not sum the moneys null");
        double result = value1.value + value2.value;
        return new Money(result);
    }
    /**
     * Somar dois valores em moeda
     * @param value1
     * @param value2
     * @return
     */
    public static Money sub(Money value1, Money value2)
    {
        if(value1 == null || value2 == null)
            throw new NumberFormatException("Can not sub the moneys null");
        double result = value1.value - value2.value;
        return new Money(result);
    }


    /**
     * Somar dois valores em moeda
     * @param value1
     * @param value2
     * @return
     */
    public static Money mult(Money value1, Money value2)
    {
        if(value1 == null || value2 == null)
            throw new NumberFormatException("Can not mult the moneys null");
        double result = value1.value * value2.value;
        return new Money(result);
    }

    public static Money div(Money value1, Money value2)
    {
        if(value1 == null || value2 == null)
            throw new NumberFormatException("Can not div the moneys null");
        double result = value1.value / value2.value;
        return new Money(result);
    }
}
