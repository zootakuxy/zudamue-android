package com.st.dbutil.android.model;

import java.io.Serializable;

/**
 * Created by xdata on 8/7/16.
 */
public class Divice  implements Serializable, CharSequence{
    private String mac;

    public Divice(String mac) {
        this.mac = mac;
    }

    public String getMac()
    {
        return mac;
    }

    @Override
    public int length() {
        return toString().length();
    }

    @Override
    public char charAt(int index) {
        return toString().charAt(index);
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        return toString().subSequence(beginIndex, endIndex);
    }

    @Override
    public String toString() {
        return this.mac;
    }
}
