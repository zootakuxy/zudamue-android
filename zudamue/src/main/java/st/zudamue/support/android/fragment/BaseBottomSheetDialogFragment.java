package st.zudamue.support.android.fragment;

import android.support.design.widget.BottomSheetDialogFragment;

import java.util.Map;

/**
 * Created by dchost on 05/03/17.
 */

public class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment  implements CallbackFragmentManager.BaseFragment {


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
