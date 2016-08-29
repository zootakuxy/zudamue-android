package com.st.dbutil.android.sqlite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Created by xdata on 7/23/16.
 */
public class LiteUpdate extends DMLite implements DML, CommandUpdate.CompletUpdate
{
    private final SQLiteDatabase dataBase;
    private ArrayList<Column> listColumns;
    private ArrayList<Column> listColumnsWait;
    private LinkedHashMap<CharSequence, Object> listValuess;
    private String tableName;
    private UpdateStatus oldStatus;
    private Condicion condicion;
    private String[] ids;
    private Column pk;

    public LiteUpdate(SQLiteDatabase writableDataBase)
    {
        this.dataBase = writableDataBase;
        this.listColumns = new ArrayList<>();
        this.listValuess = new LinkedHashMap<>();
        this.listColumnsWait = new ArrayList<>();
    }

    @Override
    public void begin() throws DMLException
    {
        this.checkStatus(UpdateStatus.BEGIN);
        Log.i("DBA:APP.TEST", "LiteUpdate-> Begin Update");
        this.end();
    }

    @Override
    public void end() throws DMLException
    {
        if(getStatus() == UpdateStatus.EXECUTE)
            throw new DMLException.DMLStatusException("Can not end the operation while STATUS = \""+ UpdateStatus.UPDATE+"\"");
        this.listColumns.clear();
        this.listValuess.clear();
        this.listColumnsWait.clear();
        this.tableName = null;
        this.condicion = null;
        this.ids = null;
    }

    @Override
    public synchronized void execute() throws DMLException
    {
        this.checkStatus(UpdateStatus.EXECUTE);
        Log.i("DBA:APP.TEST", "LiteUpdate-> Begin onExecute definitions");
        String finalResult = "FAILED";
        boolean result = this.initExecute();
        if(result)
            finalResult = "SUCESSFULL";
        Log.i("DBA:APP.TEST", "LiteUpdate-> End onExecute definitions \""+finalResult+"\"");
        this.checkStatus(UpdateStatus.FINISHED);
    }

    private boolean initExecute()
    {
        boolean finalResult = true;
        String where = null;
        String[] whereValues = null;
        ContentValues contetValues = new ContentValues();

        //Mapera os valore no contet value
        this.mapParam(contetValues, this.listValuess);

        //Caarregar a list todas que serao atualizadas
        LiteSelect db = new LiteSelect(this.dataBase);
        db.setDebugable(this);
        db.begin();
        db.select("*")
              .fromKey(this.tableName, this.pk.realName)
              .where(this.condicion);
        db.execute();

        ArrayList<String> condicionIds = new ArrayList<>();

        //Obter os id das condicoes que estajam verdadeiras
        Log.i("DBA:APP.TEST", "LiteUpdate-> RESULT FROM QUERE"+db.getResult());
        if(condicion != null)
            for(LinkedHashMap<CharSequence, Object> row: db.getResult())
            {
                Log.i("DBA:APP.TEST", "LiteUpdate-> rowID = "+row);
                if(condicion != null && row.get(pk.realName) == null)
                    throw new DMLException.DMLInvalidCondicionException("LiteUpdate-> The row id can not be null please redifine yor DLL and try again");
                String id = row.get(this.pk.realName).toString();
                if(!condicionIds.contains(id))
                    condicionIds.add(id);
            }

        //Verificar se foi expecificado os id que ira receber a atulizacao
        if(ids != null)
            for(String id: ids)
                if(!condicionIds.contains(id))
                    condicionIds.add(id);

        // Criar o wher filter de SQLite open help
        if(condicionIds.size() != 0)
        {
            whereValues = new String[condicionIds.size() ];
            where =this.pk.realName+" IN ("+super.createInterogacion(condicionIds.size())+")";
            for(int i=0; i<condicionIds.size(); i++)
                whereValues[i] = condicionIds.get(i)+"";
        }

        int result = this.dataBase.update(this.tableName, contetValues, where, whereValues);
        if(result == -1)
            finalResult = false;
        return finalResult;
    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public String getSql() {
        return null;
    }


    @Override
    public <T extends CommandUpdate.Set & CommandUpdate.SetColumns> T update(String tableName, String tableID)
    {
        this.checkStatus(UpdateStatus.UPDATE);
        Log.i("DBA:APP.TEST", "LiteUpdate-> Update table "+tableName);
        if(tableName == null)
            throw new DMLException.DMLInvalidArgmentException("Invalid table name, the table name can not be null");
        if(tableID == null)
            throw new DMLException.DMLInvalidArgmentException("Invalid primary key, the primary key can not be null");

        this.listColumns = LiteSelect.columnsOf(this.dataBase, tableName, this);
        Column pk = Column.findReal(this.listColumns, tableID);
        Log.i("DBA:APP.TEST", "LiteUpdate-> prymany key | "+pk.toText());


        if(pk == null)
            throw new DMLException.DMLInvalidArgmentException("The column primary key does not exist in table");


        this.pk = pk;
        this.tableName = tableName;
        this.pk.type = Column.ColumnType.RRIMARY_KEY;
        return (T) this;
    }

    @Override
    public <T extends CommandUpdate.Set & CommandUpdate.Where & CommandUpdate.WhereROWID & CommandUpdate.SetColumns> T set(String columnName, Object value) throws DMLException
    {
        this.checkStatus(UpdateStatus.SET);
        Log.i("DBA:APP.TEST", "LiteUpdate-> Defining set{column:\""+columnName+", value:\""+value+"\"}");
        //Validar se a coluna nao esta nula nem vazia
        if(columnName == null || columnName.isEmpty())
            throw new DMLException.DMLInvalidColumnException("The column name \""+columnName+"\" can not be null or empty");

        //Validar se a coluna existe na tabela
        DMLite.Column column;
        if((column = DMLite.Column.findReal(this.listColumns, columnName)) == null)
            throw new DMLException.DMLInvalidColumnException("The column name \""+columnName+"\" does not exist in table");

        //Validar se para essa coluna ja possui valores definidos
        if(column.use)
            throw new DMLException.DMLInvalidColumnException("The column \""+columnName+"\" alered defined value");
        this.listValuess.put(column.realName, value);
        column.use = true;
        return (T) this;
    }

    @Override
    public <T extends CommandUpdate.Values> T setColumns(String... columnsName) throws DMLException
    {
        this.checkStatus(UpdateStatus.SET_COLL);
        Log.i("DBA:APP.TEST", "LiteUpdate-> Defining setColumns{"+Arrays.toString(columnsName)+"}");
        //Validar se as colunas foram definidas
        if(columnsName == null || columnsName.length == 0)
            throw new DMLException.DMLInvalidColumnException("The columns name can not be null or empty");

        //Valdar se os valores estao definido na table e se nao esta ja marcada para usar nem estao duplicada na  lista
        DMLite.Column column;
        for(int i =0; i<columnsName.length; i++)
        {
            String coll= columnsName[i];
            //Validar se a coluna nao esta nula nem vazia
            if(coll == null || coll.isEmpty())
                throw new DMLException.DMLInvalidColumnException("The column name \""+coll+"\" can not be null or empty");

            //Validar se a coluna existe na tabela
            column = DMLite.Column.findReal(this.listColumns, coll);
            if(column == null)
                throw new DMLException.DMLInvalidColumnException("The column \""+coll+"\" not found in table \""+this.tableName+"\"");
            if(column.use == true)
                throw new DMLException.DMLInvalidColumnException("The column "+toText(columnsName)+"alered defined in current update");
        }

        //Adicionar as colunas a list de espera de valores
        for(String coll: columnsName)
        {
            column = DMLite.Column.findReal(this.listColumns, coll);
            this.listColumnsWait.add(column);
            column.use = true;
        }
        return (T) this;
    }

    @Override
    public <T extends CommandUpdate.Set & CommandUpdate.Where & CommandUpdate.SetColumns> T values(Object... values) throws DMLException
    {
        this.checkStatus(UpdateStatus.VALUES);
        Log.i("DBA:APP.TEST", "LiteUpdate-> Defing statusValues{"+Arrays.toString(values)+"}");
        //Validat se os  valores estao definidos e se a quandtidade de valores que estao definidos
        // corresponde a quanditade das colunas que haviam sido previmente definidas
        if(values == null || values.length == 0)
            throw new DMLException.DMLInvalidArgmentException("The statusValues  can not be null or empty");
        if(values.length < this.listColumnsWait.size())
            throw new DMLException.DMLInvalidArgmentException("Missing statusValues argments");
        if(values.length> this.listColumnsWait.size())
            throw new DMLException.DMLInvalidArgmentException("The statusValues is more that in defined columns");

        int valueIndex = 0;
        for(DMLite.Column column: this.listColumnsWait)
        {
            Log.i("DBA:APP.TEST", "LiteUpdate-> Defining set{column:\""+column+"\", value:\""+values[valueIndex]+"\"}");
            this.listValuess.put(column.realName, values[valueIndex++]);
        }
        this.listColumnsWait.clear();
        return (T) this;
    }

    @Override
    public <T extends DML & CommandUpdate.WhereROWID> T where(Condicion condicion) throws DMLException
    {
        this.checkStatus(UpdateStatus.WHERE);
        Log.i("DBA:APP.TEST", "LiteUpdate-> defining where condicion");
        this.condicion = condicion;
        return (T) this;
    }


    @Override
    public <T extends DML> T rowId(String ... ids) throws DMLException
    {
        this.checkStatus(UpdateStatus.WHERE_ROWID);
        Log.i("DBA:APP.TEST", "LiteUpdate-> Defining ROWS_ID{"+Arrays.toString(ids)+"}");
        this.ids = ids;
        return (T) this;
    }


    /**
     * Registro dos staus existente
     * Aqui defini-se para casa status a lista dos proximos status que podera passar para ele
     */
    private enum UpdateStatus implements Status
    {
        /**1*/ INIT(2),
        /**2*/ BEGIN(3),
        /**3*/ UPDATE(4, 5),
        /**4*/ SET(4, 5, 7, 8, 9, 10),
        /**5*/ SET_COLL(6),
        /**6*/ VALUES(4, 5, 7, 8, 9, 10),
        /**7*/ WHERE(8, 9, 10),
        /**8*/ WHERE_ROWID(9, 10),
        /**9*/ RETURNING(10),
        /**10*/ EXECUTE(11),
        /**11*/ FINISHED(1);

        /**
         *
         * @param requiredeIndex Index do status em que o currente status podera passar para ele
         */
        UpdateStatus(int ... requiredeIndex)
        {
            this.gerralPossiblit = requiredeIndex;
        }

        private int[] gerralPossiblit;

        public String possiblit()
        {
            Status[] required = getPossiblite();
            return DMLite.toText(required);
        }

        @NonNull
        @Override
        public Status[] getPossiblite()
        {
            Status required [] = new Status[this.gerralPossiblit.length];
            for(int i = 0; i< gerralPossiblit.length; i++)
            {
                Status req = values()[gerralPossiblit[i]-1];
                required[i] = req;
            }
            return required;
        }

        @Override
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
}
