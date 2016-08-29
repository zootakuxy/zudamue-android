/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.st.dbutil.android.socket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Servidor
 */
public class NetIntent
{
    String sender;
    String reciver;
    int type;
    Intent intent;
    String message;
    private Result result;
    private Date sendTime;

    List<Map<String, String>> listMaps;

    public NetIntent(String sender, String receiver, int type, Intent intent, String message)
    {
        this.sender = sender;
        this.reciver = receiver;
        this.type = type;
        this.intent = intent;
        this.message = message;
        this.listMaps = new ArrayList<>();
    }


    public Result getResult() {
        return result;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public NetIntent(String sender, String receiver, Intent intent)
    {
        this.sender = sender;
        this.reciver = receiver;
        this.intent = intent;
        this.message = message;
        this.listMaps = new ArrayList<>();
    }

    public NetIntent() {
        listMaps = new ArrayList<>();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setListMaps(List<? extends Map<String, String>> listMaps)
    {
        this.listMaps.addAll(listMaps);
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getSender() {
        return sender;
    }

    public String getReciver() {
        return reciver;
    }

    public int getType() {
        return type;
    }

    public Intent getIntent() {
        return intent;
    }

    public String getMessage() {
        return message;
    }

    public List< Map<String, String>> getListMaps() {
        return listMaps;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    @Override
    public String toString() {
        return "NetIntent{" +
                "sender='" + sender + '\'' +
                ", reciver='" + reciver + '\'' +
                ", type=" + type +
                ", intent=" + intent +
                ", message='" + message + '\'' +
                ", result=" + result +
                ", sendTime=" + sendTime +
                ", listMaps=" + listMaps +
                '}';
    }
    /**
     * Efetura uma troca entre o destino e o rementent
     */
    public void redirect()
    {
        String auxSender = this.sender;
        this.sender = this.reciver;
        this.reciver = auxSender;
    }

    public Date getSendTime()
    {
        return this.sendTime;
    }

    public enum Intent
    {
        VALIDATE,
        GET,
        SEND,
        REG,
        RESPONSE,
        CONNECT,
        DISCONNECT,
        SEND_SMS
    }

    public enum Result
    {
        WAIT,
        SUCESS,
        FAILED
    }

    public enum ConnectionStatus
    {
        SUCCESS,
        FAILED,
        LOSTED,
        REESTABLISHED
    }

}
