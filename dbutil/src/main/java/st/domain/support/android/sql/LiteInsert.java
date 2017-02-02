package st.domain.support.android.sql;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import st.domain.support.android.sql.sqlite.Query;
import st.domain.support.android.sql.builder.Select;

/**
 *
 * Created by xdata on 7/23/16.
 */
public class LiteInsert extends DMLite implements DML, CommandInsert.ComplectInsert
{
    private final SQLiteDatabase dataBase;
    private boolean debugable;
    private List<Column> listColumns;
    private final List<DMLite.WhereClausa> listWhere;
    private final List<Object []> listValues;
    private String tableName;
    private DMLite.CatchResult catchResult;
    private long result;
    private boolean requireCatchResut;
    private SQLRow resultCatch;
    private String tableId;

    public LiteInsert(SQLiteDatabase writableDataBase)
    {
        this.dataBase = writableDataBase;
        this.listColumns = new ArrayList<>();
        this.listWhere = new ArrayList<>();
        this.listValues = new ArrayList<>();
        this.debugable = false;
    }

    @Override
    public void begin() throws DMLException
    {
        Log.i("APP:DBA.TEST", "LiteInsert-> Begin lite insert");
        this.end();
    }



    @Override
    public void end() throws DMLException
    {
        Log.i("APP:DBA.TEST", "LiteInsert-> Clearning");
        this.listColumns.clear();
        this.listWhere.clear();
        this.listValues.clear();
        this.result =  -1;
        this.requireCatchResut = false;
        this.resultCatch = null;
        this.tableId = null;
        super.end();
    }

    @Override
    public synchronized void execute() throws DMLException
    {
        int size = this.listValues.size();
        Log.i(getTag(), "LiteInsert-> Opem list insert");
        for(int i =0; i<size; i++)
        {
            this.result = this.insertRow(listValues.get(i));
            if(this.requireCatchResut)
            {
                Query query = new Query(this.dataBase);
                query.execute(
                        new Select("*")
                            .from(this.tableName)
                            .where(tableId).equal(result+"")
                            .limit(1)
                );

                List<SQLRow> auxResult = query.catchAllRow();
                SQLRow mapa = auxResult.get(0);
                Log.i("DAB:APP.TEST", "LiteInsert-> Result catched: "+mapa);
                if(this.catchResult != null && result != -1)
                    this.catchResult.catchInto(mapa);
                this.resultCatch = mapa;
            }
        }
        Log.i(getTag(), "LiteInsert-> Close List inser");
    }

    private long insertRow(Object [] row)
    {
        ContentValues values = new ContentValues();
        int index =0;
        Log.i(getTag(), "LiteInsert-> Inserting new row");
        LinkedHashMap<CharSequence,Object> map = new LinkedHashMap<>();
        for(Column coll: this.listColumns)
            map.put(coll.realName, row[index++]);

        this.mapParam(values, map);
        long result = this.dataBase.insert(this.tableName, null, values);
        Log.i(getTag(), "LiteInsert-> new row insertid {result:\""+result+"\"}");
        return result;
    }

    @Override
    public Long getResult() throws DMLException
    {
        if(getStatus() != InsertStatus.FINISHED)
            throw new DMLException.DMLStatusException("Can not get result inSelect staus "+getStatus().statusName());
        return this.result;
    }

    @Override
    public String getSql() throws DMLException
    {
        return null;
    }

    @Override
    public synchronized <T extends CommandInsert.Values & CommandInsert.Columns & CommandInsert.As> T insertInto(String tableName, String tableId) throws DMLException
    {
        Log.i(getTag(), "LiteInsert-> Insert into "+tableName);
        if(tableName == null
                || tableName.length() == 0)
            throw new DMLException.DMLInvalidArgmentException("The table name can not be null jor empty");
        this.tableName = tableName;
        this.tableId = tableId;
        return (T) this;
    }

    @Override
    public <T extends CommandInsert.As & CommandInsert.Values> T columns(String... columnsName) throws DMLException
    {

        ArrayList<DMLite.Column> expecified = new ArrayList<>();
        for(int i = 0; i<columnsName.length; i++)
        {
            DMLite.Column column;

            //Validar que a coluna não estaja definia mais de uma vez na lista
            for(int j=i+1; j<columnsName.length; j++)
                if(columnsName[i].equalsIgnoreCase(columnsName[j]))
                    throw new DMLException.DMLInvalidColumnException("A coluna \""+columnsName[i]+"\" Esta especificada mais de uma veze");


            column = new Column(columnsName[i], columnsName[i]);
            expecified.add(column);
        }
        this.listColumns = expecified;
        return (T) this;
    }

    @Override
    public <T extends CommandInsert.Returning & CommandInsert.Values> T values(Object... values) throws DMLException
    {
        //Adicionar o valor da coluna
        this.listValues.add(values);
        return (T) this;
    }

    @Override
    public <T extends DML> T returning(DML.CatchResult catchResult) throws DMLException
    {
        Log.i(getTag(), "LiteInsert-> Defeing returning");
        this.requireCatchResut = true;
        this.catchResult = catchResult;
        return (T) this;
    }

    /**
     * Registro dos staus existente
     * Aqui defini-se para casa status a lista dos proximos status que podera passar para ele
     */
    private enum InsertStatus implements DML.Status
    {
        /*1*/ INIT(2),
        /*2*/ BEGIN(3),
        /*3*/ INSERT(4, 5, 6),
        /*4*/ COLUMNS(5, 6),
        /*5*/ VALUES(5, 7, 8),
        /*6*/ AS(7, 8),
        /*7*/ RETURNING(8),
        /*8*/ EXECUTE(9),
        /*9*/ FINISHED(1);

        /**
         *
         * @param requiredeIndex Index do status em que o currente status podera passar para ele
         */
        InsertStatus(int ... requiredeIndex)
        {
            this.gerralPossiblit = requiredeIndex;
        }

        private int[] gerralPossiblit;

        public String possiblit()
        {
            DML.Status[] required = getPossiblite();
            return DMLite.toText(required);
        }

        @NonNull
        @Override
        public DML.Status[] getPossiblite()
        {
            DML.Status required [] = new DML.Status[this.gerralPossiblit.length];
            for(int i = 0; i< gerralPossiblit.length; i++)
            {
                Status req = values()[gerralPossiblit[i]-1];
                required[i] = req;
            }
            return required;
        }

        public boolean accept(Status newPossiblit)
        {
            for(Status possiblit: this.getPossiblite())
                if(newPossiblit.equalsName(newPossiblit)) return true;
            return false;
        }

        @Override
        public boolean equalsName(Status other)
        {
            return this.name().equals(other.statusName());
        }

        @Override
        public String statusName() {
            return name();
        }

        @Override
        public Status[] statusValues() {
            return values();
        }
    }

    public SQLRow getResultCatch()
    {
        if(this.getStatus() != InsertStatus.FINISHED)
            throw new DMLException.DMLStatusException("Can not get staus while operation not inSelect status "+ InsertStatus.FINISHED+" | current status = "+this.getStatus());
        return this.resultCatch;
    }

}
