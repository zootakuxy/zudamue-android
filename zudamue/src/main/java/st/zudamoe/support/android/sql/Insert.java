package st.zudamoe.support.android.sql;

/**
 *
 * Created by xdata on 1/7/17.
 */

public interface Insert {

    ResultInsertInto insertInto( CharSequence table );

    interface ResultInsertInto extends Values, Columns, As, CharSequence, SQL {
    }

    interface ResultColumn extends SQL {
    }

    interface As {
        Select as();
    }

    interface ResultColumns extends Values, SQL {
    }

    interface Columns {
        ResultColumns columns( CharSequence  ... columns );
    }

    interface Values {
        SQL values(Object ... values);
    }

}
