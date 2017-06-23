package st.zudamoe.support.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by dchost on 05/03/17.
 */

public final class CallbackFragmentManager {

    private Fragment fragment;

    protected CallbackFragmentManager(Fragment fragment ){
        this.fragment = fragment;
    }

    public void setFragment( Fragment fragment ){
        this.fragment = fragment;
    }

    public Map<String, Object> callback( FragmentCallback callback ) {
        return this.callbackObject( callback, null );
    }

    public void callbackObjectPushMap(FragmentCallback callback, Map<String, Object> pushMap) {
        this.callbackObject( callback, null, pushMap );
    }

    public Map<String, Object> callbackObject(FragmentCallback callback, Map<String, Object> objectsParam) {
        Map<String, Object> push = new LinkedHashMap<>();
        this.callbackObject( callback, objectsParam, push );
        return push;
    }


    public void callbackObject(FragmentCallback callback, Map<String, Object> objectsParam, Map<String, Object> pushMap) {
        this.callbackComplete( callback, objectsParam, null, pushMap );
    }

    public Map<String, Object> callbackExtra(FragmentCallback callback, Bundle param) {
        Map<String, Object> push = new LinkedHashMap<>();
        this.callbackExtra( callback, push );
        return push;
    }

    public void callbackExtra(FragmentCallback callback, Map<String, Object> pushMap) {
        this.callbackExtra( callback, null, pushMap );
    }

    public void callbackExtra(FragmentCallback callback, Bundle extraParam, Map<String, Object> pushMap) {
        this.callbackComplete( callback, null, extraParam, pushMap );
    }

    public void callbackComplete(FragmentCallback callback, Map<String, Object> objectsParam, Bundle extraParam, Map<String, Object> pushMap) {
        if( callback != null )
            callback.onCallback( this.fragment, objectsParam, extraParam, pushMap );
    }

    public interface FragmentCallback extends Serializable {

        /**
         * @param fragment
         * @param objectsParam
         * @param extraParam
         * @param pushMap
         */
        void  onCallback ( Fragment fragment, Map<String, Object> objectsParam, Bundle extraParam, Map<String, Object> pushMap );
    }

    public interface BaseFragment {

        public CallbackFragmentManager getCallback();

        public Map<String, Object> callback(FragmentCallback callback );

    }
}
