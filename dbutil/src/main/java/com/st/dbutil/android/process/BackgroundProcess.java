package com.st.dbutil.android.process;

import android.os.AsyncTask;


/**
 * Created by xdata onResultProcess 7/25/16.
 */
public class BackgroundProcess extends AsyncTask<Object, Double, ProcessResult>
{
    private final OnProcessResult onResultProcess;
    public Background<? extends ProcessResult> backgroud;

    public BackgroundProcess(Background<? extends ProcessResult> backgroud, OnProcessResult onResultProcess)
    {
        this.backgroud = backgroud;
        this.onResultProcess = onResultProcess;
    }

    @Override
    protected ProcessResult doInBackground(Object... inParamsn)
    {
        return backgroud.onExecute(inParamsn);
    }

    @Override
    protected void onPostExecute(ProcessResult result)
    {
        super.onPostExecute(result);
        Background <ProcessResult> background = (Background<ProcessResult>) this.backgroud;
        background.accept(result, onResultProcess);
    }

    public interface Background<R extends ProcessResult>
    {
        R onExecute(Object... paramns);

        void accept(R result, OnProcessResult onProcessResult);
    }

}
