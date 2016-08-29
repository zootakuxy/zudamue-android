package com.st.dbutil.android.process;

import android.os.AsyncTask;

import com.st.dbutil.android.model.CallbackClient;

/**
 * Created by xdata on 7/25/16.
 */
public class BackgroudProcess<WaitResult> extends AsyncTask<Object, Double, WaitResult>
{
    private final CallbackClient client;
    public Backgroud <WaitResult> backgroud;

    public  BackgroudProcess(Backgroud<WaitResult> backgroud, CallbackClient clinet)
    {
        this.backgroud = backgroud;
        this.client = clinet;
    }

    @Override
    protected WaitResult doInBackground(Object... inParamsn)
    {
        return backgroud.onExecute(inParamsn);
    }

    @Override
    protected void onPostExecute(WaitResult waitResult)
    {
        super.onPostExecute(waitResult);
        backgroud.posExecute(waitResult, this.client);
    }

    public interface Backgroud <Result>
    {
        Result onExecute(Object... paramns);

        void posExecute(Result result, CallbackClient clinet);
    }
}
