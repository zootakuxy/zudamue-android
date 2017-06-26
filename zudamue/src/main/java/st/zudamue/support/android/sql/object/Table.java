package st.zudamue.support.android.sql.object;

import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by xdaniel on 6/2/17.
 *
 *  @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public class Table extends ClassIdentifier {


    public Table() {

                Log.i("scanner.log-field", "Can enter");
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for( Field field : fields ){
                Log.i("scanner.log-field", String.valueOf( field.getName() ));

                if( field.getType().equals( Column.class )
//                        && Modifier.isPublic( field.getModifiers() )
//                        && !Modifier.isFinal( field.getModifiers() )
                        ) {
                    boolean accessible = field.isAccessible();
                    field.setAccessible( true );
                    field.set( this, column( field.getName() ) );
                    field.setAccessible( false );
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static Column column(String name ){
        return new Column( name );
    }
}
