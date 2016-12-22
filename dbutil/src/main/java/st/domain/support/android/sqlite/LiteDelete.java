package st.domain.support.android.sqlite;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by xdata on 7/23/16.
 */
public class LiteDelete extends DMLite implements  CommandDelect.CompletDelete
{

    private final SQLiteDatabase dataBase;

    public LiteDelete(SQLiteDatabase writableDataBase)
    {
        this.dataBase = writableDataBase;
    }

    @Override
    public void begin() throws DMLException {

    }

    @Override
    public void end() throws DMLException {

    }

    @Override
    public synchronized void execute() throws DMLException
    {
    }

    @Override
    public Object getResult() throws DMLException {
        return null;
    }

    @Override
    public String getSql() throws DMLException
    {
        return null;
    }

    @Override
    public <T extends CommandDelect.Where> T deleteFrom(String tableName) throws DMLException {
        return null;
    }

    @Override
    public <T extends CommandSelect.WhereAdd> T where(Condicion condicion) throws DMLException {
        return null;
    }

    @Override
    public <T> T and(Condicion condicion) throws DMLException {
        return null;
    }

    @Override
    public <T> T or(Condicion condicion) throws DMLException {
        return null;
    }
}
