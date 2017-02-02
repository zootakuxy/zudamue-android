/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package st.domain.support.android.text;

import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * Essa classe permite recriar um ficeiro xml baseando da estrutura de uma classe em java
 * desde que essa implemente a interface ClassXML
 * @author Servidor
 */
public class XTextName
{
    private String tag = XTextName.class.getSimpleName();

    /**
     * Essa função mapea-se a classe java a um ficheiro xml
     * @param xNameClass Coresponde a classe em java que sera mapeada - Essa classe deve Implementar a interface ClassXML
     */
    public static void apllyText(Class <? extends XFieldName> xNameClass, ApplyMode apllyMode, ApplyRange range) throws TextException
    {
        if(apllyMode == null ||
                xNameClass == null)
            throw new TextException("The argment is invalid, the class jand mode apply can not be null");
        try
        {
            ArrayList<Class> listClass = new ArrayList<>();
            //Assim como das subClasse detro das mesmas classes

            switch (range)
            {
                case CLASS_ONLY: listClass.add(xNameClass); break;
                case CLASS_SUBCLASS:
                    listClass.add(xNameClass);
                    for(Class cc: xNameClass.getDeclaredClasses())
                        listClass.add(cc);
                    break;
                case CLASS_AND_SUPERCLASS: break;
                case CLASS_AND_SUPERCLASS_SUBCLASS: break;
                case CLASS_SUBCLASS_AND_SUPERCLASS_SUBCLASS: break;
                case  CLASS_SUBCLASS_AND_SUPERCLASS: break;
            }


            
            // A primeira classe que aparece é aprimeira que deve aparecer no ficheiro
            //O Primeiro atributo que aparece deve ser o primeiro no XML

            String text;
            for(int i =0  ; i< listClass.size(); i++)
            {
                //Adicionar a classe na estrutura do ficheiro
                Class fieldClass = listClass.get(i);

                for(Field field: fieldClass.getDeclaredFields())
                {

                    Class<?> type = field.getType();

                    if(type.equals(String.class))
                    {
                        switch (apllyMode)
                        {
                            case ORIGINAL: text = field.getName(); break;
                            case TREAT:case TREAT_INIT_UPPER:case TREAT_FIRST_INIT_UPPER:case TREAT_LOWER:case TREAT_UPPER:
                                text = treatText(field.getName(), apllyMode);
                            break;
                            default: text = field.getName(); break;
                        }
                        setFieldValue(field, fieldClass, text);
                    }
                }
            }

        } catch (Exception ex)
        {
            throw new TextException(ex.getMessage(), ex);
        }
    }

    private static void setFieldValue(Field field, Class aClass, String value)
    {
        try
        {
            field.setAccessible(true);
            field.set(aClass, value);
        } catch (Exception ex)
        {
            String message;
            Log.e("wdfe", message = "XTextName-> rename{class:\""+aClass.getName()+"\", field:\""+field.getName()+"\", value:\""+value+"\"} FAILED ");
            throw new TextException(ex.getMessage() +"\n"+message, ex);
        }
    }

    /**
     * Essa funcão serve para tratar os nomes das variaveis de modo que a mesma seja
     * mais legivel de ler
     * Isso so sera invocado para as variaveis que nao tiver um valor definido no ficheiro XML
     * @param name O nome da variavel a que se seve tratar
     * @return 
     */
    public static String treatText(String name, ApplyMode mode)
    {
        String newName = "";
        String[] campos = name.split("_");
        char oldChar;
        char c;
        int start;
        boolean hasPreview;
        boolean frist = true;
        
        for(String ca: campos)
        {
            if(ca.length() >0)
            {
                oldChar = ca.charAt(0);
                start =  0;
                //Verificara as questoes de maiusculas e minusculas
                for(int i = 0; i<ca.length(); i++)
                {
                    hasPreview = false;
                    c = ca.charAt(i);
                    if(i+1 <ca.length())
                        hasPreview = true;
                    
                    if(oldChar >= 'a' && oldChar <= 'z' 
                            && (c >= 'A' && c <= 'Z'))
                    {
                        String aux = ca.substring(start, i);
                        newName = newName + " " +aux;
                        start = i;
                        frist = false;
                    }
                    else if(!hasPreview)
                    {
                        String aux = ca.substring(start, i+1);
                        if(!frist) aux = aux.toLowerCase();
                        newName = newName + " " +aux;
                        start = i;
                    }
                    oldChar = c;
                }
            }
        }
        newName = treatTextModo(newName, mode);
        return newName.trim();
    }

    public static String treatTextModo(String text, ApplyMode mode)
    {
        text = text.trim();
        switch (mode)
        {
            case TREAT_LOWER: return text.toLowerCase();
            case TREAT_UPPER: return text.toUpperCase();
            case TREAT_FIRST_INIT_UPPER: return initFristUpper(text);
            case TREAT_INIT_UPPER: return initTextUpper(text);
            default:
                Log.i(XTextName.class.getSimpleName(), "DEFAULT MODO");
                return text;
        }
    }

    @NonNull
    private static String initTextUpper(String newText) {
        String [] strings = newText.split(" ");
        newText = "";
        int iCount =0;
        for(String part: strings)
        {
            if(part.length()>=1)
                part = initFristUpper(part);
            newText = newText + part+ ((++iCount< strings.length)? " ":"");
        }
        return newText;
    }

    @NonNull
    private static String initFristUpper(String text)
    {
        text.trim();
        text = text.toUpperCase();
        if(text.length()>=1)
            text = text.substring(0, 1) + (text.substring(1, text.length()).toLowerCase());
        return text;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public enum ApplyMode
    {
        ORIGINAL,
        ORIGINAL_UPPER,
        ORIGINAL_LOWER,
        ORIGINAL_INIT_UPPER,
        ORIGINAL_INIT_LOWER,
        TREAT,
        TREAT_UPPER,
        TREAT_LOWER,
        TREAT_INIT_UPPER,
        TREAT_FIRST_INIT_UPPER,
    }


    public enum ApplyRange
    {
        CLASS_ONLY,
        CLASS_SUBCLASS,
        CLASS_SUBCLASS_AND_SUPERCLASS,
        CLASS_SUBCLASS_AND_SUPERCLASS_SUBCLASS,
        CLASS_AND_SUPERCLASS,
        CLASS_AND_SUPERCLASS_SUBCLASS
    }

}
