/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.st.dbutil.android.xml;

/**
 * Essa classe permite recriar um ficeiro xml baseando da estrutura de uma classe em java
 * desde que essa implemente a interface ClassXML
 * @author Servidor
 */
public class XReference
{

//    public static String REF_AUTO_CLEAR = "REF_AUTO_CLEAR";
//
//
//    /**
//     * Essa função mapea-se a classe java a um ficheiro xml
//     * @param refXmlClass Coresponde a classe em java que sera mapeada - Essa classe deve Implementar a interface ClassXML
//     * @param xmlPach O caminho em que esta o ficheiro XML
//     */
//    public static void apllyText(Class <? extends XFieldName> refXmlClass, String xmlPach)
//    {
//
//        try
//        {
//            ArrayList<Class> listClass = new ArrayList<>();
//            Class referendeClass = refXmlClass;
//            XStream stream = RefConverter.getStream();
//            References references;
//            Object value = null;
//            boolean statusAutoClear;
//
//            //Verificar o estado do limpeza automatica declarado na classe
//            for(Field f: refXmlClass.getDeclaredFields())
//            {
//                if(f.getName().equals(REF_AUTO_CLEAR))
//                {
//                    value = f.get(refXmlClass);
//                    break;
//                }
//            }
//            if(value == null)
//            {
//                for(Field f: refXmlClass.getFields())
//                {
//                    if(f.getName().equals(REF_AUTO_CLEAR))
//                    {
//                        value = f.get(refXmlClass);
//                        break;
//                    }
//                }
//            }
//            statusAutoClear = (boolean) value;
//
//
//            //Tentar carregar o fichiro XML
//            //Caso alguma falha no momento de carga então o ficheiro sera recriado com e estrutura da classe
//            try
//            {
//                references = (References) stream.fromXML(new File(xmlPach));
//            }
//            catch(Exception ex)
//            {
//                references = new References();
//            }
//
//            //Carregar todas as classe que possuem a heraca da internace
//            //Assim como das subClasse detro das mesmas classes
//            while(!referendeClass.equals(XFieldName.class)
//                    && !referendeClass.equals(Object.class))
//            {
//                listClass.addFragment(0, referendeClass);
//                for(Class cc: referendeClass.getDeclaredClasses())
//                    listClass.addFragment(1, cc);
//                referendeClass = referendeClass.getSuperclass();
//            }
//
//            // A primeira classe que aparece é aprimeira que deve aparecer no ficheiro
//            //O Primeiro atributo que aparece deve ser o primeiro no XML
//
//            int countField = 0;
//            for(int i =0  ; i< listClass.size(); i++)
//            {
//                //Adicionar a classe na estrutura do ficheiro
//                Class fieldClass = listClass.get(i);
//                if(!references.contains(fieldClass.getCanonicalName()))
//                    references.addClass(fieldClass.getCanonicalName());
//                ClassClass refClass = references.get(fieldClass.getCanonicalName());
//                refClass.validate();
//                references.trasfer(refClass, i);
//
//                countField = 0;
//                for(Field field: fieldClass.getDeclaredFields())
//                {
//                    //PARA AS VARIAVEIS DECLARADA NA INTREFACE MAI ESSA VARIAES SERAO TOMADAS COMO RESERVADAS
//                   //E POR ISSO ESSA VARIAVEIES NAO SERAO APRESENDADA SIMPLEMNETE COMO UM TRIBUTO
//                    if(XFieldName.class.isAssignableFrom(fieldClass)&&
//                            field.getName().equalsIgnoreCase("REF_AUTO_CLEAR"))
//
//                    {
//                        continue;
//                    }
//                    //Adicionar o campo da classe na estrutura do ficheiro
//                    if(!refClass.contains(field.getName()))
//                        refClass.addFiel(field.getName(), null);
//
//                    FieldClass refField = refClass.get(field.getName());
//                    refField.validate();
//                    refClass.sendIntent(refField, countField);
//
//                    //Para as variaveis String que sao publica e estatica
//                    //Se foi definido o valor para ela no XML então sera carregado essa valor para a aplicacao
//
//
//                    if(field.getModifiers() == Modifier.PUBLIC+Modifier.STATIC)
//                    {
//                        Object vall =  null;
//                        Class<?> type = field.getType();
//                        String xmlValue = refField.getValue();
//                        boolean hasXMLValue = (xmlValue != null && xmlValue.length() >0);
//
//
//
//                        if(type.equals(String.class))
//                        {
//
//                            vall = (!hasXMLValue )? treatText(field.getName())
//                                    :xmlValue;
//
//                        }
//                        else if(hasXMLValue)
//                            vall = treatXMLValues(type, xmlValue);
//
//                        setFieldValue(field, fieldClass, vall, 1);
//                    }
//                    countField ++;
//                }
//            }
//            for(int i =listClass.size()-1; i>= 0; i--)
//            {
//                Class clas = listClass.get(i);
//                //references.trasfer(references.get(clas.getCanonicalName()), i);
//            }
//
//            //Quando o estado de limpesa autmatica no XML for detato activo estão ser feito a limpesa do dados
//            //No ficheiro que esnntra-se invalido na aplicacoa
//            if(statusAutoClear) references.reValidEtructure();
//
//            if(references.isUpgraded())
//                stream.toXML(references, new FileOutputStream(new File(xmlPach)));
//        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | FileNotFoundException ex)
//        {
//            Logger.getLogger(XTextName.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public static Object treatXMLValues(Class<?> type, String xmlValue)
//    {
//        Object vall = null;
//        if(type.equals(Color.class))
//            vall = treatColor(xmlValue);
//        else if(type.equals(Integer.class))
//            vall = treatInteger(xmlValue);
//        else if(type.equals(Short.class))
//            vall = treatShort(xmlValue);
//        else if(type.equals(Byte.class))
//            vall = treatByte(xmlValue);
//        else if(type.equals(Long.class))
//            vall = treatLong(xmlValue);
//        else if(type.equals(Float.class))
//            vall = treatFloat(xmlValue);
//        else if(type.equals(Double.class))
//            vall = treatDouble(xmlValue);
//        else if(type.equals(Number.class))
//            vall = treatNumber(xmlValue);
//        else if(type.equals(Font.class))
//            vall = Font.decode(xmlValue);
//        else vall = type.cast(xmlValue);
//        return vall;
//    }
//
//
//
//    private static void setFieldValue(Field f, Class cla, Object vall, int tryAgain)
//    {
//        try
//        {
//            f.setAccessible(true);
//            f.set(cla, vall);
//        } catch (IllegalArgumentException | IllegalAccessException ex)
//        {
//        }
//    }
//
//    /**
//     * Essa funcão serve para tratar os nomes das variaveis de modo que a mesma seja
//     * mais legivel de ler
//     * Isso so sera invocado para as variaveis que nao tiver um valor definido no ficheiro XML
//     * @param name O nome da variavel a que se seve tratar
//     * @return
//     */
//    private static Object treatText(String name)
//    {
//        String newName = "";
//        String[] campos = name.split("_");
//        char oldChar;
//        char c;
//        int start;
//        boolean hasPreview;
//        boolean frist = true;
//
//        for(String ca: campos)
//        {
//            if(ca.length() >0)
//            {
//                oldChar = ca.charAt(0);
//                start =  0;
//                //Verificara as questoes de maiusculas e minusculas
//                for(int i = 0; i<ca.length(); i++)
//                {
//                    hasPreview = false;
//                    c = ca.charAt(i);
//                    if(i+1 <ca.length())
//                        hasPreview = true;
//
//                    if(oldChar >= 'a' && oldChar <= 'z'
//                            && (c >= 'A' && c <= 'Z'))
//                    {
//                        String aux = ca.substring(start, i);
//                        if(!frist) aux = aux.toLowerCase();
//                        newName = newName + " " +aux;
//                        start = i;
//                        frist = false;
//                    }
//                    else if(!hasPreview)
//                    {
//                        String aux = ca.substring(start, i+1);
//                        if(!frist) aux = aux.toLowerCase();
//                        newName = newName + " " +aux;
//                        start = i;
//                    }
//                    oldChar = c;
//                }
//            }
//        }
//        return newName.trim();
//    }
//
//    private static Color treatColor(String xmlValue)
//    {
//        try
//        {
//            return Color.decode(xmlValue);
//        } catch (Exception e)
//        {
//            return null;
//        }
//    }
//
//    private static Object treatNumber(String xmlValue)
//    {
//        try {
//            return NumberFormat.getInstance().parse(xmlValue);
//        } catch (ParseException ex)
//        {
//            return null;
//        }
//    }
//
//    private static Object treatInteger(String xmlValue) {
//        throw new UnsupportedOperationException("Not supported yet."); //To replace body of generated methods, choose Tools | Templates.
//    }
//
//    private static Object treatShort(String xmlValue) {
//        throw new UnsupportedOperationException("Not supported yet."); //To replace body of generated methods, choose Tools | Templates.
//    }
//
//    private static Object treatByte(String xmlValue) {
//        throw new UnsupportedOperationException("Not supported yet."); //To replace body of generated methods, choose Tools | Templates.
//    }
//
//    private static Object treatLong(String xmlValue) {
//        throw new UnsupportedOperationException("Not supported yet."); //To replace body of generated methods, choose Tools | Templates.
//    }
//
//    private static Object treatFloat(String xmlValue) {
//        throw new UnsupportedOperationException("Not supported yet."); //To replace body of generated methods, choose Tools | Templates.
//    }
//
//    private static Object treatDouble(String xmlValue) {
//        throw new UnsupportedOperationException("Not supported yet."); //To replace body of generated methods, choose Tools | Templates.
//    }
}
