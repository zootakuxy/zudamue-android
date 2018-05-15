package st.zudamue.support.android.sql.annnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xdaniel on 12/02/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD } )
public @interface Column {

    String value();
}

