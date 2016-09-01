package com.st.dbutil.android.process;

/**
 * Created by Daniel Costa at 8/29/16.
 * Using user computer xdata
 */
public abstract class ProcessResult <R>
{
    R result;

    public ProcessResult (R result)
    {
        this.result = result;
    }

    public R getResult()
    {
        return  result;
    }
}
