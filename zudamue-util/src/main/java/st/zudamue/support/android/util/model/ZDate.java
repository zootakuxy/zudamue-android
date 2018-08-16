package st.zudamue.support.android.util.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by xdaniel on 7/21/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
public class ZDate extends java.util.Date
{

    public ZDate()
    {
    }

    /**
     * Iniciar um nova data
     * @param year
     * @param month
     * @param day
     */
    public ZDate(int year, int month, int day)
    {
        this.setYear(year);
        this.setMonth(month);
        this.setDate(day);
    }


    /**
     * Montar um data a partir dos valores dos dia, mes e ano
     * @param year o ona da data
     * @param month o mes da data
     * @param day o dia da data
     * @return a data caso for valida e null caso for invalida
     */
    public static ZDate mount(int year, int month, int day)
    {
        ZDate ZDate = new ZDate(year, month, day);
        if(ZDate.isValid()) return ZDate;

        return null;
    }

    @Override
    public void setMonth(int month) {
        super.setMonth(month-1);
    }

    @Override
    public int getMonth() {
        return super.getMonth()+1;
    }

    @Override
    public void setYear(int year)
    {
        super.setYear(year-1900);
    }

    @Override
    public int getYear() {
        return super.getYear()+1900;
    }

    @Override
    public int getDay() {
        return this.getDate();
    }

    public void setDay(int day)
    {
        this.setDate(day);
    }


    public boolean isValid()
    {
        Month month = Month.find(this.getMonth());
        int day = this.getDay();
        int year = this.getYear();

        if(day<1 || month == null) return false;

        /**
         * 0 - 29 day
         * 1 - 28 day
         */
        int typeYear;

        switch (month)
        {
            case JAN:case MAR:case MAY:case JUL:case AGO:case OCT:case DEZ: return day<=31;
            case ABR:case JUN:case SET:case NOV: return day<=31;
            case FEV:
                if(year%4 == 0) typeYear = 0;
                else typeYear = 1;
                return (typeYear == 0 &&  day<=29)
                        ||(typeYear == 1 && day<=28);
        }
        return false;
    }

    public TypeYear getTypeYear()
    {
        return TypeYear.type(this);
    }

    public String getText(String s)
    {
        DateFormat d = new SimpleDateFormat(s);
        return d.format(this);
    }

    public String getSDay()
    {
        int day = this.getDay();
        return (day<10)? "0"+day: day+"";
    }

    public Month getSMonth()
    {
        int month = this.getMonth();
        return Month.find(month);
    }


    public static enum Month
    {
        JAN(31),
        FEV(29),
        MAR(31),
        ABR(30),
        MAY(31),
        JUN(30),
        JUL(31),
        AGO(31),
        SET(30),
        OCT(31),
        NOV(30),
        DEZ(31);

        private  final int maxDay;

        Month(int maxDay)
        {
            this.maxDay = maxDay;
        }

        public int getMaxDay() {
            return this.maxDay;
        }

        public static Month find(int month)
        {
            if(month>=1 && month<= 12) return values()[month-1];
            return null;
        }
    }

    public static enum TypeYear
    {
        YEAR28_COMUN,
        YEAR29_BISEXTO;

        public static TypeYear type(ZDate ZDate)
        {
            int year = ZDate.getYear();
            return type(year);
        }

        public static TypeYear type(int year)
        {
            int restDivision = year%4;
            if(restDivision == 0) return YEAR29_BISEXTO;
            else return YEAR28_COMUN;
        }
    }
}
