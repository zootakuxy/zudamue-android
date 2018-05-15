package st.zudamue.support.android.sql.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by xdaniel on 7/22/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
public class AssetsDatabase extends SQLiteOpenHelper
{
    private static String dataBaseName;
    private final Context context;
    private ActionExecute execute;
    private String assetsName;
    private String tag;

    /**
     * Criar o controlador do banco de dados
     * @param context
     * @param dataBaseName
     * @param version
     */
    public AssetsDatabase(Context context, String dataBaseName, int version)
    {
        super(context, dataBaseName, null, version);
        AssetsDatabase.dataBaseName = dataBaseName;
        this.assetsName = AssetsDatabase.dataBaseName;
        this.context = context;
        this.setUpDataBase();
        this.tag = getClass().getSimpleName();
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
        Log.i(getTag(), "CREATE DATABASE REQUIREDE");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        this.execute = ActionExecute.UPGRADE;
        Log.i(getTag(), "UPGRADE REQUIREDA");
    }

    /**
     * Verificar se ha necessidade de criar ou efetuar um upgrada no banco de dados
     * Caso for necessario criar o banco de dados a criacao sera feita baseando do ficheiro vindo do aassets
     * Quando for para efetuar um upgrade entao primeiramente a base de dados sera eliminada e depois copiada para o deirectorio destino
     */
    public final void setUpDataBase()
    {
        SQLiteDatabase data = this.getWritableDatabase();
        if(this.execute == ActionExecute.CREATE) {
            Log.i(getTag(), "SET UP -> CREATING DATA BASE");
            this.createFromAssets(data);
            Log.i(getTag(), "SET UP -> DATABASE CREATED");
        } else if(this.execute == ActionExecute.UPGRADE) {
            Log.i(getTag(), "SET UP -> UPGRADE DATABASE");
            upgradeFromAssets(data);
            Log.i(getTag(), "SET UP -> DATABASE UPGRADED");
        }

        data.close();
    }

    /**
     * Atualizar a partir de um assets
     * @param data
     */
    protected void upgradeFromAssets(SQLiteDatabase data)
    {
        Log.i(getTag(), "DROPING CURRENT DATABASE");
        boolean result = SQLiteDatabase.deleteDatabase(new File(data.getPath()));
        if(result)
            Log.i(getTag(), "CURRENTE DATABASE DROPED");
        else Log.i(getTag(), "CAN NOT DROPED DATABASE");
        this.createFromAssets(data);
    }

    /**
     * Criar a base de dadas a partir do ficheiro assete
     * @param dataBase
     */
    protected void createFromAssets(SQLiteDatabase dataBase)
    {
        Log.i(getTag(), "COPYING DATA BASE "+ dataBaseName+" TO -> PATCH "+dataBase.getPath());
        InputStream inputStream;
        FileOutputStream outputStream;
        try
        {
            String out = dataBase.getPath();
            Log.i(getTag(), "INIT COPY");
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

            Log.i(getTag(), "NEW DATABASE CREATED SUCESSFUL");

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void outputDatabase()
    {

        File outputFolder = new File(Environment.getExternalStorageDirectory().getPath()+"/"
                + context.getPackageName()+"/"
                + "AssetsDataBase/"
                + this.getDatabaseName()+"/"
        );

        boolean export = outputFolder.exists();
        if(!export)
            export = outputFolder.mkdirs();

        if(export)
        {
            Calendar current = Calendar.getInstance();
            String month = ( current.get(Calendar.MONTH) <10 )? "0"+ current.get(Calendar.MONTH) : String.valueOf( current.get(Calendar.MONTH) );
            String day = (current.get(Calendar.DAY_OF_MONTH) < 10)? "0"+current.get(Calendar.DAY_OF_MONTH) : String.valueOf(current.get(Calendar.DAY_OF_MONTH));
            String hour = (current.get(Calendar.HOUR_OF_DAY) < 10)? "0"+current.get(Calendar.HOUR_OF_DAY) : String.valueOf(current.get(Calendar.HOUR_OF_DAY));
            String minute = (current.get(Calendar.MINUTE) < 10)? "0"+current.get(Calendar.MINUTE) : String.valueOf(current.get(Calendar.MINUTE));
            String second = (current.get(Calendar.SECOND) < 10)? "0"+current.get(Calendar.SECOND) : String.valueOf(current.get(Calendar.SECOND));


            String time = ""+current.get(Calendar.YEAR)+month+day;
            time = time +" "+hour+minute+second;
            String fileName = "export."+time+"."+this.getDatabaseName();
            File outFile  = new File(outputFolder, fileName);


            dumpDatabase(outFile);
        }
        else {
            Log.w(getTag(), "Data Base not exported");
        }
    }

    public void dumpDatabase(File outFile)
    {
        try {

            String in = this.getReadableDatabase().getPath();
            Log.i(getTag(), "INIT COPY");
            InputStream inputStream;
            FileOutputStream outputStream;

            inputStream = new FileInputStream(in);
            outputStream = new FileOutputStream(outFile, false);

            byte[] buffer = new  byte[1024];
            int length;

            while ((length = inputStream.read(buffer))>0)
            {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public SQLiteDatabase getDataBase()
    {
        return this.getReadableDatabase();
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private enum ActionExecute
    {
        CREATE,
        UPGRADE,
        NONE
    }
}