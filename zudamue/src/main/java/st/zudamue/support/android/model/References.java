/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package st.zudamue.support.android.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Essa classe representa a estrutura de XML das referencia
 * E a classe onde possui as regras de referencias das classe e os seu atributos e relacao ao XML
 *
 * Created by xdaniel
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
public class References
{
    private final ArrayList<ClassClass> listClass;
    private final HashMap<String, ClassClass> mapClass;
    private boolean upgraded;
    private final Consumer<Boolean> onUpgrad;
    
    public References()
    {
        this.listClass = new ArrayList<>();
        this.mapClass = new HashMap<>();
        this.onUpgrad = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean b) {
                if(b) upgraded = true;
            }
        };
    }
    
    /**
     * Addicionar umna nova classe a estrutura
     * @param className O nome da classe a ser adicionada
     * @return 
     */
    public ClassClass addClass(String className)
    {
        if(mapClass.containsKey(className)) return null;
        this.onUpgrad.accept(true);
        ClassClass c;
        mapClass.put(className, c =new ClassClass(className, this.onUpgrad));
        listClass.add(c);
        return  c;
    }

    /**
     * Verificar a existe a classe ja esta adicionada
     * @param canonicalName
     * @return 
     */
    boolean contains(String canonicalName)
    {
        return this.mapClass.containsKey(canonicalName);
    }

    /**
     * Obter a classe a paritir da ordem de adicionamento
     * @param i O index onde a classe encontra-se
     * @return 
     */
    public ClassClass get(int i) 
    {
        return this.listClass.get(i);
    }
    
    /**
     * Obter a classe a partir o seu nome canonical
     * @param name
     * @return 
     */
    public ClassClass get(String name) 
    {
        return this.mapClass.get(name);
    }

    /**
     * Remover uma classe da estrutura
     * @param i 
     */
    void remove(int i) 
    {
        ClassClass o = listClass.remove(i);
        mapClass.remove(o.getName());
        this.onUpgrad.accept(true);
    }

    /**
     * Revalidar se as antigas classe que estavam no XML correspondem ainda estão na nova estrutura
     * Quando nao mais estiver na estrutura então a classe sera removida da estrutura
     */
    void reValidEtructure()
    {
        for(int i =0; i<this.countClass(); i++)
        {
            ClassClass cla = this.get(i);
            for(int j = 0; j<cla.countFields(); j++)
            {
                FieldClass f= cla.get(j);
                if(!f.isValid())
                    cla.remove(j);
            }
            if(!cla.isValid())
                this.remove(i);
        }
    }

    /**
     * Obter a quantidade de classe que esta na referencias
     * @return 
     */
    public int countClass() 
    {
        return this.listClass.size();
    }

    /**
     * Stransitar uma calsse de posicao no ficheiro para estar da emsma ordem que esta na classe
     * @param refClass A referencia da classe
     * @param indexTo A posicao onde devera ocupar
     */
    public void trasfer(ClassClass refClass, int indexTo)
    {
        int currentIndex = this.listClass.indexOf(refClass);
        if(indexTo != currentIndex)
        {
            ClassClass aux = this.get(indexTo);
            this.listClass.set(indexTo, refClass);
            this.listClass.set(currentIndex, aux);
            this.onUpgrad.accept(true);
        }
    }

    /**
     * Ignorar as alateracoes na dectectadas na estruturas
     */
    void ignoreUpgrads()
    {
        this.upgraded = false;
    }

    /**
     * Verificar se ouve alteracao na estrutura da classe em relacao a estrutura do XML
     * @return 
     */
    public boolean isUpgraded()
    {
        return this.upgraded;
    }
    
    public class FieldClass
    {
        String name;
        String value;
        private boolean valid;

        private FieldClass(String fieldName, String fieldValue) 
        {
            this.name = fieldName;
            this.value = fieldValue;
            if(value == null)
                this.value = "";
        }

        /**
         * Obter o nome do atributo
         * @return 
         */
        String getName()
        {
            return this.name;
        }

        /**
         * Obter o valor de valor
         * @return 
         */
        public String getValue()
        {
            return this.value;
        }

        @Override
        public String toString()
        {
            return "FieldClass{" + "name=" + name + ", value=" + value + '}';
        }

        /**
         * Validar o estado do atributo no ficheiro
         */
        public void validate()
        {
            this.valid = true;
        }

        /**
         * Verificar o atributo esta valido
         * @return 
         */
        boolean isValid()
        {
            return this.valid;
        }
        
        
    }
    
    public class ClassClass
    {
        String className;
        ArrayList<FieldClass> listField = new  ArrayList<>();  
        private final HashMap<String, FieldClass> mapField = new HashMap<>();
        private boolean valid;
        private final Consumer<Boolean> onUpgrad;

        private ClassClass(String className, Consumer<Boolean> onUpgrad)
        {
            this.className = className;
            this.onUpgrad = onUpgrad;
        }
        
        /**
         * Adicionar o atributo na classe
         * @param fieldName nome do atributo e o seu respectivo valor
         * @param fieldValue O valor do atributo
         * @return 
         */
        public boolean addFiel(String fieldName, String fieldValue)
        {
            if(mapField.containsKey(fieldName)) return false;
            FieldClass c;
            mapField.put(fieldName, c =new FieldClass(fieldName, fieldValue));
            listField.add(c);
            this.onUpgrad.accept(true);
            return true;
        }
        
        /**
         * Obter o nome da classe
         * @return 
         */
        public String getName() 
        {
            return this.className;
        }

        @Override
        public String toString() {
            return "ClassClass{" + "className=" + className + ", listField=" + listField + '}';
        }

        /**
         * Obter um atributo da classe apartir da ordem em que foi adicionado
         * @param index
         * @return 
         */
        public FieldClass get(int index)
        {
            return this.listField.get(index);
        }
        
        /**
         * 
         * @param name
         * @return 
         */
        public FieldClass get(String name)
        {
            return this.mapField.get(name);
        }

        /**
         * Remover um field de da classe
         * @param index 
         */
        void remove(int index)
        {
            FieldClass field = this.listField.remove(index);
            this.mapField.remove(field.name);
            this.onUpgrad.accept(true);
        }

        /**
         * Virificar se nessa classe posui un dado field
         * @param name
         * @return 
         */
        boolean contains(String name) 
        {
            return this.mapField.containsKey(name);
        }

        /**
         * Validar o field do ficheiro XML
         */
        public void validate()
        {
            this.valid = true;
        }
        
        /**
         * Verificar se esse field ainda e valido no ficheiro xim
         * @return 
         */
        public boolean isValid()
        {
            return this.valid;
        }
       
        /**
         * Trocar o field da posicao
         * @param refField
         * @param indexTo 
         */
        void transfer(FieldClass refField, int indexTo)
        {
            int currentIndex = this.listField.indexOf(refField);
            if(currentIndex != indexTo)
            {
                FieldClass aux = this.get(indexTo);
                this.listField.set(indexTo, refField);
                this.listField.set(currentIndex, aux);
            }
        }

        /**
         * Obter o numero de atributo declarado na classe
         * @return 
         */
        public int countFields()
        {
            return this.listField.size();
        }
    }

    @Override
    public String toString() {
        return "ReferenceEtructure{" + "listClass=" + listClass + '}';
    }
}
