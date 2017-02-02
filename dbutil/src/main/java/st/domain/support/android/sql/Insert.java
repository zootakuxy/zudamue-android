package st.domain.support.android.sql;

/**
 *
 * Created by xdata on 1/7/17.
 */

public interface Insert {

    ResultInsertInto insertInto(CharSequence table);

    interface ResultInsertInto extends Values, Column, Columns, As {
    }

    interface ResultColumn extends Column {
    }

    interface As {
        Select as();
    }

    interface Column {
        ResultColumn column(CharSequence column, CharSequence value);
    }

    interface ResultColumns extends Values {
    }

    interface Columns {
        ResultColumns columns(CharSequence ... columns);
    }

    interface Values {
        void values(Object ... values);
    }

}
