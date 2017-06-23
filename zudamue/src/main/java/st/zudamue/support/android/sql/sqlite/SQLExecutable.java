package st.zudamue.support.android.sql.sqlite;

/**
 * Created by dchost on 03/02/17.
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
