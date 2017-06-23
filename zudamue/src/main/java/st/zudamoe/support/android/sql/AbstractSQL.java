package st.zudamoe.support.android.sql;

import android.support.annotation.NonNull;

import java.util.List;

import st.zudamoe.support.android.sql.builder.Select;
import st.zudamoe.support.android.sql.object.Identifier;

/**
 *
 * Created by xdata on 12/31/16.
 */

public abstract class AbstractSQL implements SQL {

    private String level;
    public AbstractSQL(){
        this.level = "";
    }

    @Override
    public int length() {
        return this.sql().length();
    }

    @Override
    public AbstractSQL addArgument(Object value) {
        this.arguments().add( value );
        return this;
    }

    @Override
    public char charAt(int index) {
        return this.sql().charAt(index);
    }

    protected String getVariable( ) {
        return "?";
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return this.sql().subSequence(start, end);
    }

    protected String processArgument(CharSequence column) {
        String value;
        if (column == null) {
            value = this.getVariable();
            this.arguments().add(null);

        } else if (column instanceof Identifier){
            value = ( ( Identifier ) column).name();
            appendArguments( ((Identifier) column).arguments());
        }
        else if(column instanceof Select) {
            value = "("+((SQL) column).sql()+")";
            appendArguments(((SQL) column).arguments());
        }
        else if( column instanceof Operation){
            value = ((Operation) column).sql();
            appendArguments( ((Operation) column).arguments() );

        }
        else {
            value = this.getVariable();
            this.arguments().add( column.toString() );
        }
        return value;
    }

    protected String processIdentifier(CharSequence column) {

        String value = String.valueOf(column);
        if (column == null) {
            value = "NULL";

        } else if(column instanceof Identifier) {
            value = ((Identifier) column).name();
            appendArguments(((Identifier) column).arguments());
        }
        else if(column instanceof Argument) {
            value = this.getVariable();
            String arg = ((Argument) column).argument();
            arguments().add(arg);
        }
        else if( column instanceof Operation){
            value = ((Operation) column).sql();
            appendArguments( ((Operation) column).arguments() );

        } else if ( column instanceof Select ){
            value = "("+((SQL) column).sql()+")";
            if(((Select) column).name != null)
                value += " as "+((Select) column).name;
            appendArguments(((SQL) column).arguments());
        }
        return value;
    }

    @NonNull
    protected String processIdentifierConjunct( CharSequence[] columns ) {
        String identifier ="";
        for(int i = 0; i<columns.length; i++){
            String aux =  processIdentifier( columns[i] );
            if(i+1 <columns.length) identifier += " "+aux+",";
            else identifier += aux;
        }
        if(columns.length >0) identifier = "("+identifier+")";
        return identifier;
    }

    @NonNull
    protected String processArgumentsConjunct(CharSequence[] columns) {
        String identifier ="";
        for(int i = 0; i<columns.length; i++){
            String aux =  processIdentifier(columns[i]);
            if(i+1 <columns.length) identifier += " "+aux+",";
            else identifier +=aux;
        }
        identifier = "("+identifier+")";
        return identifier;
    }

    private void appendArguments(List<Object> arguments) {
        if(arguments != null && arguments.size()>0)
            this.arguments().addAll( arguments );
    }


}
