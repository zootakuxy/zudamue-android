package st.domain.support.android.old_sql.sqlite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import st.domain.support.android.sql.SQLRow;
import st.domain.support.android.sql.sqlite.AssetsDataBase;
import st.domain.support.android.sql.sqlite.Query;
import st.domain.support.android.sql.sqlite.SQLResources;

/**
 *
 * Created by xdata on 7/25/16.
 */
public class LiteDataBase extends DMLite implements CommandInsert.Insert
{

    private final Context context;
    private final AssetsDataBase SQLite;
    private final LiteInsert insert;
    private final Query query;
    private Operaction operaction;

    private SQLResources liteResources;

    public LiteDataBase(Context context, String dataBaseName, int version)
    {
        this.context = context;
        this.SQLite =  new AssetsDataBase(context, dataBaseName, version);
        this.insert = new LiteInsert(this.SQLite.getWritableDatabase());

        this.liteResources = new SQLResources(this.SQLite.getDataBase());
        this.query = new Query(this.getDataBase());
    }

    public Query query() {
        return query;
    }

    @Deprecated
    @Override
    public void begin() throws DMLException {
    }

    public SQLResources getResources()
    {
        return this.liteResources;
    }

    public synchronized void setOperaction(Operaction operaction)
    {
    }

    public  void begin(Operaction operaction)
    {
        this.setOperaction(operaction);
        this.begin();
    }


    @Override
    public void end() throws DMLException
    {

    }

    @Override
    public void execute() throws DMLException
    {
        insert.execute();
    }

    @Override
    public Object getResult() throws DMLException
    {
       return insert.getResult();
    }

    public SQLRow getInsertResult()
    {
        return this.insert.getResultCatch();
    }

    @Override
    public String getSql() throws DMLException
    {
        return insert.getSql();
    }

    @Override
    public DML.Status getStatus()
    {
        if(this.operaction == null) return BaseStatus.INIT;
        switch (this.operaction)
        {

            case INSERT:return this.insert.getStatus();

            default:return BaseStatus.INIT;
        }
    }


    @Override
    public <T extends CommandInsert.Values & CommandInsert.Columns & CommandInsert.As> T insertInto(String tableName, String tableId) throws DMLException {
        return this.insert.insertInto(tableName, tableId);

    }



    @Override
    public void setDebugable(Debugable debugable) {
        super.setDebugable(debugable);
        this.insert.setDebugable(debugable);
    }

    @Override
    public void setDebugable(boolean debugable, int typeDebug) {
        super.setDebugable(debugable, typeDebug);
        try {
            this.insert.setDebugable(debugable, typeDebug);

        }catch (Exception ex)
        {

        }
    }

    @Override
    public void setTagDebug(String tagDebug) {
        super.setTagDebug(tagDebug);
        try {
            this.insert.setTagDebug(tagDebug);
        }catch (Exception ex)
        {

        }
    }

    public Context getContext()
    {
        return context;
    }


    public SQLiteDatabase getDataBase() {
        return this.SQLite.getWritableDatabase();
    }

    public enum Operaction
    {
        INSERT
    }

}
