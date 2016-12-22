package st.domain.support.android.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.storage.StorageVolume;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xdata on 7/22/16.
 */
public class AssetsDataBase extends SQLiteOpenHelper
{
    private static String dataBaseName;
    private final Context context;
    private ActionExecute execute;
    private String assetsName;

    /**
     * Criar o controlador do banco de dados
     * @param context
     * @param dataBaseName
     * @param version
     */
    public AssetsDataBase(Context context, String dataBaseName, int version)
    {
        super(context, dataBaseName, null, version);
        AssetsDataBase.dataBaseName = dataBaseName;
        this.assetsName = AssetsDataBase.dataBaseName;
        this.context = context;
        this.setUpDataBase();
    }

    /**
     * Definir o nome do asste caso o focheiro tenha um nome diferente ao do banco de dados
     * @param assetsName
     */
    public void setAssets(String assetsName)
    {
        this.assetsName = assetsName;
    }

    public void forceAction(ActionExecute actionExecute)
    {
        this.execute = actionExecute;
        this.setUpDataBase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        this.execute = ActionExecute.CREATE;
        Log.i("DBA:APP.TEST", "CREATE DATABASE REQUIREDE");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        this.execute = ActionExecute.UPGRADE;
        Log.i("DBA:APP.TEST", "UPGRADE REQUIREDA");
    }

    /**
     * Verificar se ha necessidade de criar ou efetuar um upgrada no banco de dados
     * Caso for necessario criar o banco de dados a criacao sera feita baseando do ficheiro vindo do aassets
     * Quando for para efetuar um upgrade entao primeiramente a base de dados sera eliminada e depois copiada para o deirectorio destino
     */
    public final void setUpDataBase()
    {
        SQLiteDatabase data = this.getWritableDatabase();
        if(this.execute == ActionExecute.CREATE)
        {
            Log.i("DBA:APP.TEST", "SET UP -> CREATING DATA BASE");
            this.createFromAssets(data);
            Log.i("DBA:APP.TEST", "SET UP -> DATABASE CREATED");
        }
        else if(this.execute == ActionExecute.UPGRADE)
        {
            Log.i("DBA:APP.TEST", "SET UP -> UPGRADE DATABASE");
            upgradeFromAssets(data);
            Log.i("DBA:APP.TEST", "SET UP -> DATABASE UPGRADED");
        }

        data.close();
    }

    /**
     * Atualizar a partir de um assets
     * @param data
     */
    protected void upgradeFromAssets(SQLiteDatabase data)
    {
        Log.i("DBA:APP.TEST", "DROPING CURRENT DATABASE");
        boolean result = SQLiteDatabase.deleteDatabase(new File(data.getPath()));
        if(result)
            Log.i("DBA:APP.TEST", "CURRENTE DATABASE DROPED");
        else Log.i("DBA:APP.TEST", "CAN NOT DROPED DATABASE");
        this.createFromAssets(data);
    }

    /**
     * Criar a base de dadas a partir do ficheiro assete
     * @param dataBase
     */
    protected void createFromAssets(SQLiteDatabase dataBase)
    {
        Log.i("DBA:APP.TEST", "COPYING DATA BASE "+ dataBaseName+" TO -> PATCH "+dataBase.getPath());
        InputStream inputStream;
        FileOutputStream outputStream;
        try
        {
            String out = dataBase.getPath();
            Log.i("DBA:APP.TEST", "INIT COPY");
            inputStream = this.context.getAssets().open(this.assetsName);
            outputStream = new FileOutputStream(out, false);
            byte[] buffer = new  byte[1024];
            int length;

            while ((length = inputStream.read(buffer))>0)
            {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            Log.i("DBA:APP.TEST", "NEW DATABASE CREATED SUCESSFUL");

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void restory()
    {
        InputStream inputStream;
        FileOutputStream outputStream;
        try
        {
            String in = this.getReadableDatabase().getPath();
            Log.i("DBA:APP.TEST", "INIT COPY");
            inputStream = new FileInputStream(in);

            File outFile = new File("/sdcard/Quitanda/quitanda.database.restore.db");

            outputStream = new FileOutputStream(outFile, false);

            StorageVolume k;
            byte[] buffer = new  byte[1024];
            int length;

            while ((length = inputStream.read(buffer))>0)
            {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }



    public SQLiteDatabase getDataBase()
    {
        return this.getReadableDatabase();
    }

    private enum ActionExecute
    {
        CREATE,
        UPGRADE,
        NONE
    }
}