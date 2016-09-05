package com.st.dbutil.android.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.st.dbutil.android.sqlite.DMLite.Column.find;

/**
 * Created by xdata on 7/23/16.
 */
public class LiteSelect extends DMLite implements CommandSelect.ComplectSelect
{
    private SQLiteDatabase dataBase;
    private ArrayList<Column> listColumns;
    private ArrayList<WhereClausa> listWhere;
    private ArrayList<LinkedHashMap<CharSequence, Object>> result;
    private int countLastColumns;
    private String tableName;
    private int limit;
    private long next;
    private boolean nextCalled;
    private String[] rowids;
    private SelectStatus oldStatus;


    public LiteSelect(SQLiteDatabase dataBase)
    {
        super();
        this.dataBase = dataBase;
        this.listColumns = new ArrayList<>();
        this.listWhere = new ArrayList<>();
    }

    @Override
    public void begin() throws DMLException
    {
        this.checkStatus(SelectStatus.BEGIN);
        super.debug("Bigin Started");
        this.end();
    }

    @Override
    public void end() throws DMLException
    {
        debug("Cleanig the datas");
        this.listColumns.clear();
        this.listWhere.clear();
        this.result = null;
        this.countLastColumns = -1;
        this.tableName = null;
        this.limit= -1;
        this.next = -1;
        this.nextCalled = false;
        this.rowids = null;
    }

    @Override
    public synchronized void execute() throws DMLException
    {
        this.checkStatus(SelectStatus.EXECUTE);
        debug("The executing started");
        String rowid = "";
        String where = null;
        String [] whereValue = null;
        Column pk = Column.findPrimaryKey(this.listColumns);
        if(rowids != null)
        {

            where = pk.realName +" IN("+super.createInterogacion(rowids.length)+")";
            whereValue = new String[rowids.length];

            for(int i = 0; i<this.rowids.length; i++)
                whereValue[i] = rowids[i]+"";
        }


        if(pk!= null && !pk.use)
            rowid = pk.realName+", ";

        Cursor cursor = this.dataBase.query(this.tableName, new String[]{rowid+this.tableName+".*"}, where, whereValue, null, null, null);
        boolean exists = cursor.moveToFirst();

        String[] columns = cursor.getColumnNames();
        if(this.listColumns.get(0).equals("*"))
        {
            this.listColumns.clear();
            for(String column: columns)
            {
                Column coll;
                listColumns.add(coll= new Column(column, column));
                coll.use = true;
            }
        }

        this.result = new ArrayList<>();
        int row = 0;
        LinkedHashMap<CharSequence, Object> map, mapPublic;
        do
        {
            if((row < limit || limit<0) && exists)
            {
                map = catchAll(cursor, this);
                debug("row catched:"+map);
                //Verificar se a linah pode ser publicada
                if(this.accept(map))
                {
                    mapPublic = new LinkedHashMap<>();
                    for (Column coll : this.listColumns)
                    {
                        if(coll.use)
                            mapPublic.put(coll.alias, map.get(coll.realName));
                    }
                    if(row < limit
                            || limit<0)
                        this.result.add(mapPublic);
                    else break;
                    row++;
                }
            }
        }while (cursor.moveToNext() && (row < limit || limit<0));

        ArrayList<Column> shows = Column.useds(this.listColumns);
        debug("finished {rowsNum:\""+this.result.size()+"\", columnsNum:\""+shows.size()+"\", columns:"+shows+"}");
        cursor.close();
        this.next = 0;
        this.checkStatus(SelectStatus.FINISHED);
    }

    /**
     * Test if client acept row
     * @param completMap
     * @return
     */
    private boolean accept(LinkedHashMap<CharSequence, Object> completMap)
    {
        if(this.listWhere.size()==0) return true;

        boolean resultFinal;
        int i =0;
        for(DMLite.WhereClausa condicion: this.listWhere)
        {
            condicion.result = condicion.condicion.accept(++i, completMap);
        }

        resultFinal = listWhere.get(0).result;

        for(i=1; i<this.listWhere.size(); i++)
        {
            WhereClausa condicion= this.listWhere.get(i);
            if(condicion.concat == WhereConcat.AND)
                resultFinal = resultFinal && condicion.result;
            else resultFinal = resultFinal && condicion.result;
        }
        debug("filter{map:"+completMap.toString()+", accept:\""+resultFinal+"\"}");
        return resultFinal;
    }

    /**
     * Capture the statusValues for cusor row into map
     * @param cursor
     * @param debugable
     * @return
     */
    public static LinkedHashMap<CharSequence, Object> catchAll(Cursor cursor, Debugable debugable)
    {
        LinkedHashMap<CharSequence, Object> map = new LinkedHashMap<CharSequence, Object>()
        {
            @Override
            public Object get(Object key)
            {
                key = key.toString().toUpperCase();
                return super.get(key);
            }
        };
        Object object;
        String columnName;

        for(int i=0; i<cursor.getColumnCount(); i++)
        {
            object = null;
            columnName = cursor.getColumnName(i);
            switch (cursor.getType(i))
            {
                case Cursor.FIELD_TYPE_BLOB:
                    object = cursor.getBlob(i);
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    object = cursor.getDouble(i);
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    object = cursor.getInt(i);
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    object = cursor.getString(i);
                    break;
                case Cursor.FIELD_TYPE_NULL:
                    break;

            }
            map.put(columnName.toUpperCase(), object);
        }
        return map;
    }

    @NonNull
    public static List<? extends Map<String, String>> toNetIntentList(ArrayList<LinkedHashMap<CharSequence, Object>> result) {
        List<LinkedHashMap<String, String>> resultTreated = new ArrayList<>();

        LinkedHashMap<String, String> map;
        for(LinkedHashMap<CharSequence, Object> item : result)
        {
            map = new LinkedHashMap<>();
            for(Map.Entry<CharSequence, Object> iMap : item.entrySet())
                map.put(iMap.getKey().toString(), (iMap.getValue() != null)? iMap.getValue().toString(): "");
            resultTreated.add(map);
        }
        return resultTreated;
    }

    @Override
    public ArrayList<LinkedHashMap<CharSequence, Object>> getResult() throws DMLException
    {
        if(!getStatus().equals(SelectStatus.FINISHED))
            throw new DMLException.DMLStatusException("The current stutus can not get result | STATUS:\""+getStatus()+"\" use this metho while STAUS:\""+ SelectStatus.FINISHED+"\"");
        if(this.getStatus().equalsName(SelectStatus.FINISHED))
            return this.result;
        return null;
    }

    @Override
    public String getSql() throws DMLException
    {
        return null;
    }

    /**
     * Obter a proxima linha
     * @return
     */
    public HashMap<CharSequence, Object> next() throws DMLException
    {
        if(!getStatus().equals((SelectStatus.FINISHED)))
            throw new DMLException.DMLStatusException("O estado do query nao esta finalizado");
        if(next < this.result.size())
        {
            this.nextCalled = true;
            return this.result.get((int) this.next++);
        }
        return null;
    }

    /**
     * Get the value of column
     * @param columnName the name of column
     * @return
     * @throws DMLException
     */
    public Object get(String columnName) throws DMLException
    {
        if(find(this.listColumns, columnName) == null)
            throw new DMLException.DMLColumnNotFound("The alias column \""+columnName+"\" not found");

        if(!this.nextCalled)
            this.next();

        HashMap<CharSequence, Object> row = this.result.get((int) (next - 1));
        return row.get(columnName);
    }

    /**
     * Verificar se existe uma proxima linha no resultado
     * @return
     */
    public boolean hasNext() throws DMLException
    {
        String k;
        if(getStatus() != SelectStatus.FINISHED) throw new DMLException.DMLStatusException("Can nov validae if has next in status:\""+this.getStatus()+"\"");
        return this.next+1 < this.result.size();
    }

    @Override
    public <T extends CommandSelect.From & CommandSelect.SelectAs> T select(String... clunmnsName) throws DMLException
    {
        this.checkStatus(SelectStatus.SELECT);
        debug("Definig columns from columns:"+toText(clunmnsName));
        if(clunmnsName == null || clunmnsName.length == 0)
            throw new DMLException.DMLInvalidColumnException("O paramentro das columnas a ser selecionada esta invalida");

        if(this.listColumns.size()>0)
            if(this.listColumns.equals("*"))
                throw new DMLException.DMLInvalidColumnException("Column * alered defined, can not define others columns");

        if(clunmnsName.length>1)
            for(String s: clunmnsName)
            {
                if(s.equals("*"))
                    throw new DMLException.DMLInvalidColumnException("Can not define * column and other column in one stantment");
            }

        //Registrar as colunas
        for(String column: clunmnsName)
        {
            Column col = new Column(column, column);
            col.use = true;
            this.listColumns.add(col);
        }

        //Defenir a partir de que index as novas colunas acabaram de entrar
        this.countLastColumns = clunmnsName.length;
        return (T) this;
    }

    @Override
    public <T extends CommandSelect.From & CommandSelect.Select> T as(String... aliasName) throws DMLException
    {
        this.checkStatus(SelectStatus.ALIAS);
        debug("Defining columns alias:"+toText(aliasName));
        if(aliasName == null
                || aliasName.length == 0)
            throw new DMLException.DMLInvalidAliasException("O paramentro das renomeacoes invalida");

        if(aliasName.length != this.countLastColumns)
            throw new InvalidParameterException("A quantidade de renomecao requer ser igual ao" +
                    " tamanho total das ultimas colunas definidas ultimas colunas " +
                    "definidas="+this.countLastColumns+" | rename="+aliasName.length);

        if(this.listColumns.size()>0)
            if(this.listColumns.get(0).equals("*"))
                throw new DMLException.DMLInvalidAliasException("Can not rename * column");
        Column c;

        for(int i =0; i<aliasName.length; i++)
        {
            String alias =  aliasName[i];
            if((c = find(this.listColumns, alias)) != null)
                throw new DMLException.DMLInvalidAliasException("The alias name \""+alias+"\" alerde definede in other column "+c.realName);
            for(int j=i+1; j<aliasName.length; j++)
                if(alias.equals(aliasName[j]))
                    throw new DMLException.DMLInvalidAliasException("The alias \""+alias+"\" name can not be duplicated ");
        }

        //Iniciar a partir  de onde terminou as ultimas definicoes de colunas
        int index = this.listColumns.size() - this.countLastColumns;
        for(String alias : aliasName)
        {

            Column coll;
            (coll = this.listColumns.get(index)).alias = alias;
            debug("renaming column{name:\""+coll.realName+"\", as:\""+coll.alias+"\"}");
            index ++;
        }

        return (T) this;
    }

    @Override
    public <T extends CommandSelect.Join & CommandSelect.WhereROWID & CommandSelect.Where & CommandSelect.Limit & CommandSelect.Group & DML> T from(String tableName) throws DMLException
    {
        this.checkStatus(SelectStatus.FROM);
        if(tableName == null) new DMLException.DMLInvalidTableException("The table name can not be null");
        debug("Defining table from \""+tableName+"\"");
        this.tableName = tableName;
        return (T) this;
    }


    public <T extends CommandSelect.Join & CommandSelect.Where & CommandSelect.Limit & CommandSelect.Group & CommandSelect.WhereROWID & DML> T fromKey(String tableName, String primaryKey) throws DMLException
    {
        this.from(tableName);
        debug("Defining PRIMARY_KEY:\""+primaryKey+"\"");
        if(primaryKey == null)
            throw new DMLException.DMLInvalidArgmentException("The primary key can not by null");
        Column pk = Column.findReal(this.listColumns, primaryKey);
        if(pk == null)
            this.listColumns.add(new Column(primaryKey, primaryKey, Column.ColumnType.RRIMARY_KEY));
        else pk.type = Column.ColumnType.RRIMARY_KEY;
        return (T) this;
    }

    @Override
    public <T extends CommandSelect.WhereAdd & CommandSelect.Limit & CommandSelect.Group & CommandSelect.Order & DML> T where(Condicion condicion) {
        this.checkStatus(SelectStatus.WHERE);
        debug("Defining where condicion:\""+condicion.getSql()+"\"");
        DMLite.WhereClausa whereClausa = new DMLite.WhereClausa(null, condicion);
        this.listWhere.add(whereClausa);
        return (T) this;
    }

    @Override
    public <T extends CommandSelect.WhereAdd & CommandSelect.Limit & CommandSelect.Group & CommandSelect.Order> T and(Condicion condicion) throws DMLException
    {
        this.checkStatus(SelectStatus.AND);
        debug("Adding condicion and condicion:\""+condicion.getSql()+"\"");
        DMLite.WhereClausa whereClausa = new DMLite.WhereClausa(WhereConcat.AND, condicion);
        this.listWhere.add(whereClausa);
        return (T) this;
    }

    @Override
    public <T extends CommandSelect.WhereAdd & CommandSelect.Limit & CommandSelect.Group & CommandSelect.Order> T or(Condicion condicion) throws DMLException{
        this.checkStatus(SelectStatus.OR);
        debug("Adding condicion or condicion:\""+condicion.getSql()+"\"");
        DMLite.WhereClausa whereClausa = new DMLite.WhereClausa(WhereConcat.OR, condicion);
        this.listWhere.add(whereClausa);
        return (T) this;
    }

    @Override
    public <T extends CommandSelect.WhereAdd & CommandSelect.Limit & CommandSelect.Group & CommandSelect.Order> T whereRowId(String ... ids) throws DMLException
    {
        this.checkStatus(SelectStatus.WHEREROWID);
        debug("Defining selectin ids:"+toText(ids));
        Column pk = Column.findPrimaryKey(this.listColumns);
        if(pk == null)
            throw new DMLException.DMLInvalidArgmentException("Para definir os id é nessecario definr previamente os primary key");
        this.rowids = ids;
        return (T) this;
    }

    @Override
    public <T extends CommandSelect.Limit & CommandSelect.Order> T groupBy(String... group) throws DMLException
    {

        return null;
    }

    @Override
    public CommandSelect.JoinON innerJoin(String tableName)throws DMLException {
        return null;
    }

    @Override
    public CommandSelect.JoinON rightJoin(String tableName) throws DMLException {
        return null;
    }

    @Override
    public CommandSelect.JoinON leftJoin(String tableName)throws DMLException {
        return null;
    }

    @Override
    public CommandSelect.JoinON fullJoin(String tableName) throws DMLException {
        return null;
    }

    @Override
    public <T extends CommandSelect.Join & CommandSelect.Where & CommandSelect.Limit & CommandSelect.Group> T on(Condicion condicion, String... columns) throws DMLException{
        return null;
    }

    @Override
    public DML limit(int limit) throws DMLException
    {
        this.checkStatus(SelectStatus.LIMIT);
        debug("Defining limit:\""+limit+"\"");
        if(limit<0)
            throw new InvalidParameterException("O limite tem de ser superior a zero");
        this.limit = limit;
        return this;
    }

    @Override
    public <T extends CommandSelect.Limit & CommandSelect.Order> T orderByAsc(String... column) {
        return null;
    }

    @Override
    public <T extends CommandSelect.Limit & CommandSelect.Order> T orderByDesc(String... column) {
        return null;
    }

    protected ArrayList<Column> getColumns()
    {
        return this.listColumns;
    }

    /**
     * Obter as colunas de uma tabela
     * @param dataBase
     * @param tableName
     * @return
     */
    public static ArrayList<Column> columnsOf(SQLiteDatabase dataBase, String tableName, Debugable debugable) throws DMLException
    {
        LiteSelect table = new LiteSelect(dataBase);
        table.setDebugable(debugable.isDebugable(), debugable.getDebugableType());
        table.begin();
        table.select("*")
                .from(tableName)
                .limit(0);
        table.execute();
        table.unUseColumns();
        return table.getColumns();
    }

    private void unUseColumns()
    {
        if(getStatus() != SelectStatus.FINISHED)
            throw new DMLException.DMLStatusException("The current status can not unuse columns STATUS:\""+getStatus()+"\" | this opretion can in STATUS:"+ SelectStatus.FINISHED);
        for(Column coll: this.listColumns)
            coll.use = false;
    }

    /**
     * Registro dos staus existente
     * Aqui defini-se para casa status a lista dos proximos status que podera passar para ele
     */
    protected enum SelectStatus implements Status
    {
        /* 1*/ INIT(2),
        /* 2*/ BEGIN(3, 2),
        /* 3*/ SELECT(3, 4, 5),
        /* 4*/ ALIAS(3, 5),
        /* 5*/ FROM(6, 9, 10, 12),
        /* 6*/ WHERE(7, 8, 9, 10),
        /* 7*/ AND(7, 8, 9, 10),
        /* 8*/ OR(7, 8, 9, 10),
        /* 9*/ EXECUTE(11),
        /*10*/ LIMIT(9),
        /*11*/ FINISHED(1, 2),
        /*12*/ WHEREROWID(7, 8, 9, 10);

        private int[] gerralPossiblit;

        /**
         *
         * @param requiredeIndex Index do auxStatus em que o currente auxStatus podera passar para ele
         */
        SelectStatus(int ... requiredeIndex)
        {
            this.gerralPossiblit = requiredeIndex;
        }

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
                DML.Status req = values()[gerralPossiblit[i]-1];
                required[i] = req;
            }
            return required;
        }

        @Override
        public boolean accept(DML.Status newPossiblit)
        {
            for(DML.Status possiblit: this.getPossiblite())
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
