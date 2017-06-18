/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package st.domain.support.android.socket;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author AhmedJorge
 */
public class NetIntentConverter implements Converter, Serializable
{

    private static final String TAG_ROOT = "Content", TAG_SENDER= "Sender",
            TAG_RECIVER="Reciver", TAG_MESSAGE="Message", TAG_DATA="Datas",
            ATB_TIMESEND = "TimeCharSequence", TAG_ITEM="Item", ATB_TYPE="type", ATB_INTENT="intent",
            ATB_KEY="key", ATB_SIZE="size", TAG_GROUPDATA="Grupo-Datas",
            ATB_RESULT = "result";
    public static DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String tagSender;
    private String tagRoot;
    private String tagReciver;
    private String tagMessage;
    private String atbSendTime;
    private String tagItem;
    private String atbType;
    private String atbIntent;
    private String atbKey;
    private String atbSize;
    private String tagGroupData;
    private String tagData;
    private String atbResult;
    private DateFormat dateFormatter;


    public NetIntentConverter()
    {
        this.tagRoot = TAG_ROOT;
        this.tagSender = TAG_SENDER;
        this.tagReciver = TAG_RECIVER;
        this.tagMessage = TAG_MESSAGE;
        this.atbSendTime = ATB_TIMESEND;
        this.tagGroupData = TAG_GROUPDATA;
        this.tagData = TAG_DATA;
        this.tagItem = TAG_ITEM;
        this.atbType = ATB_TYPE;
        this.atbIntent = ATB_INTENT;
        this.atbKey = ATB_KEY;
        this.atbSize = ATB_SIZE;
        this.atbResult = ATB_RESULT;
        this.dateFormatter = DATE_FORMATTER;
    }

    public void setTagSender(String tagSender) {
        this.tagSender = tagSender;
    }

    public void setTagRoot(String tagRoot) {
        this.tagRoot = tagRoot;
    }

    public void setTagReciver(String tagReciver) {
        this.tagReciver = tagReciver;
    }

    public void setTagMessage(String tagMessage) {
        this.tagMessage = tagMessage;
    }

    public void setTagTimeSend(String tagTimeSend) {
        this.atbSendTime = tagTimeSend;
    }

    public void setTagItem(String tagItem) {
        this.tagItem = tagItem;
    }

    public void setAtbType(String atbType) {
        this.atbType = atbType;
    }

    public void setAtbIntent(String atbIntent) {
        this.atbIntent = atbIntent;
    }

    public void setAtbKey(String atbKey) {
        this.atbKey = atbKey;
    }

    public void setAtbSize(String atbSize) {
        this.atbSize = atbSize;
    }

    public void setTagGroupData(String tagGroupData) {
        this.tagGroupData = tagGroupData;
    }

    public DateFormat getDateFormatter() {
        return dateFormatter;
    }

    public void setDateFormatter(DateFormat dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public String getTagRoot()
    {
        return  this.tagRoot;
    }

    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext mc) {

        NetIntent intent = (NetIntent) value;

        writer.addAttribute(this.atbType, intent.getType()+"");

        if(intent.getIntent() == null)
        {
            throw new RuntimeException("Invalid Transfer - Falta Intent");
        }

        writer.addAttribute(this.atbIntent, intent.getIntent().name());
        writer.addAttribute(this.atbSendTime, getDateFormatter().format(intent.getSendTime()));
        writer.addAttribute(this.atbResult, intent.getResult().name());

        writer.startNode(tagSender);
        writer.setValue(intent.getSender());
        writer.endNode();

        writer.startNode(tagReciver);
        writer.setValue(intent.getReciver());
        writer.endNode();

        writer.startNode(tagMessage);
        writer.setValue(intent.getMessage());
        writer.endNode();

        List<? extends Map<String, String>> map = intent.getListMaps();

        writer.startNode(tagGroupData);
        writer.addAttribute(atbSize, map.size()+"");
        for (Map<String, String> datas : map)
        {
            writer.startNode(tagData);
            writer.addAttribute(this.atbSize, datas.size()+"");

            for (Map.Entry<String, String> item :datas.entrySet())
            {
                writer.startNode(tagItem);
                writer.addAttribute(atbKey, item.getKey()+"");
                writer.setValue(item.getValue()+"");
                writer.endNode();
            }

            writer.endNode();
        }

        writer.endNode();
        writer.close();
    }


    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        NetIntent transfer = new NetIntent();

        String xmlTag,value, xmlSize , xmlType, xmlIntent, xmlSendTime, xmlResult;
        int i=0;

        //Pegar os atributos geral do XML
        xmlTag = reader.getNodeName();
        xmlType = reader.getAttribute(atbType);
        xmlIntent = reader.getAttribute(atbIntent);
        xmlSendTime = reader.getAttribute(atbSendTime);
        xmlResult = reader.getAttribute(atbResult);

        if(xmlTag.equals(tagRoot)&&xmlType!=null&&xmlIntent!=null)
        {
            try
            {
                transfer.setType(Integer.valueOf(xmlType));
                transfer.setIntent(NetIntent.Intent.valueOf(xmlIntent));
                transfer.setSendTime(getDateFormatter().parse(xmlSendTime));
                transfer.setResult(NetIntent.Result.valueOf(xmlResult));
            } catch (ParseException ex) {
                Logger.getLogger(NetIntentConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else throw new RuntimeException("XML invalido!");

        //Ler O sender
        reader.moveDown();
        xmlTag = reader.getNodeName();
        value= reader.getValue();
        if(xmlTag.equals(tagSender))
            transfer.setSender(value);
        else throw new RuntimeException("XML invalido!");
        reader.moveUp();

        //Pegar o sender
        reader.moveDown();
        xmlTag = reader.getNodeName();
        value= reader.getValue();
        if(xmlTag.equals(tagReciver))
            transfer.setReciver(value);
        else throw new RuntimeException("XML invalido!");
        reader.moveUp();

        //Lendo a mensagem...
        reader.moveDown();
        xmlTag = reader.getNodeName();
        value= reader.getValue();
        if(xmlTag.equals(tagMessage)) transfer.setMessage(value);
        else throw new RuntimeException("XML invalido!");
        reader.moveUp();

        //Lendo o grupo geral e os seus atribtos
        reader.moveDown();
        xmlTag = reader.getNodeName();
        xmlSize = reader.getAttribute(atbSize);
        if(xmlTag!=null&&xmlTag.equals(tagGroupData))
        {
            //Lendo os dados dos HasMahp do grupo
            while (reader.hasMoreChildren())
            {
                //Lendo as datas
                reader.moveDown();
                xmlTag = reader.getNodeName();
                if (xmlTag != null)
                {
                    if(xmlTag.equals(tagData))
                    {
                        //Lendo os datos internos da data
                        transfer.getListMaps().add(readerMaps(reader));
                        i++;
                    }
                    else
                    {
                        throw new RuntimeException("XML Invalido! Tag n�o esperada no momento. Taga esperada = "+tagData+" | Tag recebida = "+xmlTag);
                    }

                }
                reader.moveUp();
            }
        }
        else
            throw new RuntimeException("XML invalido!");

        if(i!=Integer.valueOf(xmlSize))
            throw new RuntimeException("XML invalido!");

        reader.moveUp();
        return transfer;
    }


    public boolean canConvert(Class type) {
        return type.equals(NetIntent.class);
    }

    private Map<String,String> readerMaps(HierarchicalStreamReader reader)
    {
        boolean validator;
        String tag,value,keyName, size;
        int i=0;
        HashMap<String,String> map = new HashMap();
        size = reader.getAttribute(ATB_SIZE);
        while (reader.hasMoreChildren()) {
            validator = false;
            reader.moveDown();
            tag = reader.getNodeName();
            keyName = reader.getAttribute(ATB_KEY);
            value = reader.getValue();
            if (tag != null && keyName != null)
            {
                if (tag.equals(TAG_ITEM))
                {
                    validator = true;
                    i++;
                }
            }
            if(validator)
                map.put(keyName, value);
            reader.moveUp();

        }

        if(i!=Integer.valueOf(size))
            throw new RuntimeException("XML invalido!");

        return map;
    }
}
