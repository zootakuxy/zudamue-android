package st.domain.support.android.fragment;

import android.support.v4.app.Fragment;

import java.util.Map;

/**
 * Created by dchost on 04/03/17.
 */

public class BaseFragment extends Fragment implements CallbackFragmentManager.BaseFragment {


    private CallbackFragmentManager callbackManager = new CallbackFragmentManager( this );

    @Override
    public CallbackFragmentManager getCallback() {
        if( this.callbackManager == null )
            this.callbackManager = new CallbackFragmentManager( this );
        return this.callbackManager;
    }

    @Override
    public Map<String, Object> callback(CallbackFragmentManager.FragmentCallback callback) {
        return this.getCallback().callback( callback );
    }
}
