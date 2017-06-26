package st.zudamue.support.android.sql.sqlite;

/**
 * Created by xdaniel on 03/02/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public interface SQLExecutable  {

    /**
     * UpdatableSQL one sql statement
     * @return self of executor
     */
    SQLExecutable execute();
    /**
     * execute the encoded char sequence
     * @param sql encoded sql
     */
    SQLExecutable execute(CharSequence sql);


}
