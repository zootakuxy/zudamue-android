package com.st.dbutil.android.sqlite;


import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by xdata on 7/25/16.
 */
public class LiteDataBase extends DMLite implements CommandSelect.Select, CommandInsert.Insert, CommandUpdate.Update, CommandDelect.DeleteFrom
{

    private final Context context;
    private final LiteSelect select;
    private final AssetsDataBase SQLite;
    private final LiteInsert insert;
    private final LiteUpdate update;
    private final LiteDelete delete;
    private Operaction operaction;
    private DML op;

    public LiteDataBase(Context context, String dataBaseName, int version)
    {
        super();
        this.context = context;
        this.SQLite =  new AssetsDataBase(context, dataBaseName, version);
        this.insert = new LiteInsert(this.SQLite.getWritableDatabase())
        {
            @Override
            public synchronized void execute() throws DMLException
            {
                validateExecute();
                super.execute();
            }
        };

        this.update = new LiteUpdate(this.SQLite.getWritableDatabase())
        {
            @Override
            public synchronized void execute() throws DMLException {
                validateExecute();
                super.execute();
            }
        };
        this.delete = new LiteDelete(this.SQLite.getWritableDatabase())
        {
            @Override
            public synchronized void execute() throws DMLException {
                validateExecute();
                super.execute();
            }
        };
        this.select = new LiteSelect(this.SQLite.getReadableDatabase())
        {
            @Override
            public synchronized void execute() throws DMLException {
                validateExecute();
                super.execute();
            }
        };
    }

    private void validateExecute()
    {
        if(op == null)
            throw new DMLException.DMLStatusException("A opracao ainda nao foi definida");
    }

    @Deprecated
    @Override
    public void begin() throws DMLException
    {
        if(this.operaction == null)
            throw new DMLException("Note selectd the operacation, frist select the operaction "+toText(BaseStatus.values()));
        this.op.begin();
    }

    public synchronized void setOperaction(Operaction operaction)
    {
        Status status = getStatus();
        BaseStatus base = BaseStatus.findKnowStatus(status);
        if(base == null)
            throw new DMLException.DMLStatusException("Estado atual desconhecido Esatado esconhecido");
        switch (base)
        {
            case INIT: case FINISHED:
                this.operaction = operaction;
                this.defaultDml(operaction);
                break;
            default: throw new DMLException.DMLStatusException("Cam note chage operation while staus:\""+base+"\"");
        }
    }

    private void defaultDml(Operaction operaction)
    {
        switch (operaction)
        {
            case DELETE:this.op =delete;break;
            case INSERT:this.op =insert;break;
            case SELECT:this.op =select;break;
            case UPDATE:this.op =update;break;
        }
    }

    public  void begin(Operaction operaction)
    {
        this.setOperaction(operaction);
        this.begin();
    }


    @Override
    public void end() throws DMLException
    {
        if(op == null)
            throw new DMLException.DMLStatusException("A opracao ainda nao foi definida");
        this.op.end();
        this.op = null;
        this.operaction = null;
    }

    @Override
    public void execute() throws DMLException
    {
        this.validateExecute();
        this.op.execute();
    }

    @Override
    public Object getResult() throws DMLException
    {
        if(op == null)
            throw new DMLException.DMLStatusException("A opracao ainda nao foi definida");
        return this.op.getResult();
    }

    public ArrayList<LinkedHashMap<CharSequence, Object>> getSelectResult()
    {
        if(op == null)
            throw new DMLException.DMLInvalidOperationException("A opracao ainda nao foi definida");
        if(op != select)
            throw new DMLException.DMLInvalidOperationException("A a atual operacao é esta definida para o select");
        return  this.select.getResult();
    }

    public LinkedHashMap<CharSequence, Object> getInserttResult()
    {
        if(op == null)
            throw new DMLException.DMLInvalidOperationException("A opracao ainda nao foi definida");
        if(op != insert)
            throw new DMLException.DMLInvalidOperationException("A a atual operacao é esta definida para o select");
        return  this.insert.getResultCatch();
    }

    @Override
    public String getSql() throws DMLException
    {
        if(op == null)
            throw new DMLException.DMLStatusException("A opracao ainda nao foi definida");
        return op.getSql();
    }

    @Override
    public DML.Status getStatus()
    {
        if(this.operaction == null) return BaseStatus.INIT;
        switch (this.operaction)
        {
            case DELETE:return this.delete.getStatus();
            case INSERT:return this.insert.getStatus();
            case UPDATE:return this.update.getStatus();
            case SELECT:return this.select.getStatus();
            default:return BaseStatus.INIT;
        }
    }

    @Override
    public <T extends CommandDelect.Where> T deleteFrom(String tableName) throws DMLException {
        if(op == null)
            throw new DMLException.DMLInvalidOperationException("A opracao ainda nao foi definida");
        if(op != delete)
            throw new DMLException.DMLInvalidOperationException("A a atual operacao é esta definida para o delete");
        return  this.delete.deleteFrom(tableName);
    }

    @Override
    public <T extends CommandInsert.Values & CommandInsert.Columns & CommandInsert.As> T insertInto(String tableName, String tableId) throws DMLException {
        if(op == null)
            throw new DMLException.DMLInvalidOperationException("A opracao ainda nao foi definida");
        if(op != insert)
            throw new DMLException.DMLInvalidOperationException("A a atual operacao é esta definida para o insert");
        return this.insert.insertInto(tableName, tableId);

    }

    @Override
    public <T extends CommandSelect.From & CommandSelect.SelectAs> T select(String... columns) throws DMLException {
        if(op == null)
            throw new DMLException.DMLInvalidOperationException("A opracao ainda nao foi definida");
        if(op != select)
            throw new DMLException.DMLInvalidOperationException("A a atual operacao é esta definida para o select");
        return  this.select.select(columns);
    }

    @Override
    public <T extends CommandUpdate.Set & CommandUpdate.SetColumns> T update(String tableName, String primaryKey) throws DMLException {
        if(op == null)
            throw new DMLException.DMLInvalidOperationException("A opracao ainda nao foi definida");
        if(op != update)
            throw new DMLException.DMLInvalidOperationException("A a atual operacao é esta definida para o update");
        return  this.update.update(tableName, primaryKey);

    }

    @Override
    public void setDebugable(Debugable debugable) {
        super.setDebugable(debugable);
        this.insert.setDebugable(debugable);
        this.select.setDebugable(debugable);
        this.update.setDebugable(debugable);
        this.delete.setDebugable(debugable);
    }

    @Override
    public void setDebugable(boolean debugable, int typeDebug) {
        super.setDebugable(debugable, typeDebug);
        try {
            this.insert.setDebugable(debugable, typeDebug);
            this.update.setDebugable(debugable, typeDebug);
            this.delete.setDebugable(debugable, typeDebug);
            this.select.setDebugable(debugable, typeDebug);
        }catch (Exception ex)
        {

        }
    }

    @Override
    public void setTagDebug(String tagDebug) {
        super.setTagDebug(tagDebug);
        try {
            this.select.setTagDebug(tagDebug);
            this.update.setTagDebug(tagDebug);
            this.delete.setTagDebug(tagDebug);
            this.insert.setTagDebug(tagDebug);
        }catch (Exception ex)
        {

        }
    }

    public Context getContext()
    {
        return context;
    }

    public DML getOp()
    {
        return op;
    }

    public enum Operaction
    {
        SELECT,
        INSERT,
        UPDATE,
        DELETE;
    }

}
