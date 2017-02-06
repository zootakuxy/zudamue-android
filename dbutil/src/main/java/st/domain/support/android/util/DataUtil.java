package st.domain.support.android.util;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * Created by dchost on 02/02/17.
 */

public class DataUtil {

    private Date date;

    public DataUtil(Date date){

        this.date = date;

    }

    public DataUtil() {

        this.date = Calendar.getInstance().getTime();

    }

    public Date firtDayOfWeek() {

        /*
        SUNDAY as domingo          1  | 10
        MONDAY as segunda-feira    2  | 11
        TUESDAY as terça-feira     3  | 12
        WEDNESDAY as quarta-feira  4  | 13
        THURSDAY as quinta-feira   5  | 14
        FRIDAY as sexta-feira      6  | 15
        SATURDAY as sabado         7  | 16
         */


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);

        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int decrementDays = (currentDayOfWeek-1) * -1;

        calendar.add(Calendar.DATE, decrementDays);

        return calendar.getTime();
    }

    public Date lastDayOfWeek () {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);

        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int incrementDays = 7 - currentDayOfWeek ;

        calendar.add(Calendar.DATE, incrementDays);
        return calendar.getTime();

    }


    public Date firstDayOfMonth() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        calendar.set(Calendar.DATE, 1);
        return  calendar.getTime();

    }

    public Date lastDayOfMonth() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) +1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);



        /*
        int dateMaximun = calendar.getMaximum(Calendar.MONTH);
        calendar.value(Calendar.DATE, dateMaximun);
        */

        return calendar.getTime();
    }

    public int countDayOfMonth() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.lastDayOfMonth());
        return calendar.get(Calendar.DATE);

    }

    @Override
    public String toString() {
        return this.date.toString();
    }
}
