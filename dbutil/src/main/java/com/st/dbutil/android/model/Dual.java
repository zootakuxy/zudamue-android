package com.st.dbutil.android.model;

import java.io.Serializable;

/**
 *  Created by xdata on 8/3/16.
 * @param <F>
 * @param <S>
 */
public class Dual <F extends Serializable, S extends Serializable> implements Serializable
{
    public F first;
    public S second;

    public Dual(F first, S second)
    {
        this.first = first;
        this.second = second;
    }
}
