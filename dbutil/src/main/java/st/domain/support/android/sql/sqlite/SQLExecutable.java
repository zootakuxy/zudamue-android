package st.domain.support.android.sql.sqlite;

import android.database.sqlite.SQLiteDatabase;

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
