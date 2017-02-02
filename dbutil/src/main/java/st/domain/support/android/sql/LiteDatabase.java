package st.domain.support.android.sql;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import st.domain.support.android.sql.builder.Select;
import st.domain.support.android.sql.sqlite.AssetsDatabase;
import st.domain.support.android.sql.sqlite.Query;
import st.domain.support.android.sql.sqlite.SQLResources;


/**
 *
 * Created by xdata on 7/25/16.
 */
public class LiteDatabase extends DMLite implements CommandInsert.Insert
{

    private final Context context;
    private final AssetsDatabase SQLite;
    private final LiteInsert insert;
    private final Query query;
    private Operaction operaction;

    private SQLResources liteResources;

    public LiteDatabase(Context context, String dataBaseName, int version)
    {
        this.context = context;
        this.SQLite =  new AssetsDatabase(context, dataBaseName, version);
        this.insert = new LiteInsert(this.SQLite.getWritableDatabase());

        this.liteResources = new SQLResources(this.SQLite.getDataBase());
        this.query = new Query(this.getDataBase());
    }

    public Query query() {
        return query;
    }

    public Select select(CharSequence ... columns){
        return new Select(columns);
    }



    public CharSequence sum (String ... columns){
        return AggregateFunction.function("sum", columns);
    }

    public CharSequence count (CharSequence ... columns){
        return AggregateFunction.function("count", (String[]) columns);
    }

    public CharSequence max (CharSequence ... columns){
        return AggregateFunction.function("max", (String[]) columns);
    }

    public CharSequence min (CharSequence ... columns){
        return AggregateFunction.function("min", (String[]) columns);
    }

    public CharSequence avg (CharSequence ... columns){
        return AggregateFunction.function("avg", (String[]) columns);
    }

    public CharSequence strftime (String format, Object values) {
        return Function.function("strftime", format, values);
    }

    public CharSequence column (String column){
        return st.domain.support.android.sql.Column.column(column);
    }

    public CharSequence value(byte value){
        return String.valueOf(value);
    }

    public CharSequence value(int value){
        return String.valueOf(value);
    }

    public CharSequence value(long value){
        return String.valueOf(value);
    }

    public CharSequence value(float value){
        return String.valueOf(value);
    }

    public CharSequence value(double value){
        return String.valueOf(value);
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

    public void cloneDatabase() {
        this.SQLite.outputDatabase();
    }

    public enum Operaction
    {
        INSERT
    }

}
