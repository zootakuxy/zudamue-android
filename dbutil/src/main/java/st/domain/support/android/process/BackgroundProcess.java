package st.domain.support.android.process;

import android.os.AsyncTask;


/**
 * Created by xdata onResultProcess 7/25/16.
 */
public class BackgroundProcess extends AsyncTask<Object, Double, ProcessResult>
{
    private final OnProcessResult onResultProcess;
    public Background<? extends ProcessResult> background;

    public BackgroundProcess(Background<? extends ProcessResult> backgroud, OnProcessResult onResultProcess)
    {
        this.background = backgroud;
        this.onResultProcess = onResultProcess;
    }

    @Override
    protected ProcessResult doInBackground(final Object... inParamsn)
    {
        ProcessResult result = background.onExecute(inParamsn);
        return result;
    }

    @Override
    protected void onPostExecute(ProcessResult result)
    {
        super.onPostExecute(result);
        this.onResultProcess.processedResult(result);
    }

    public interface Background<R extends ProcessResult>
    {
        R onExecute(Object... paramns);
    }

}
