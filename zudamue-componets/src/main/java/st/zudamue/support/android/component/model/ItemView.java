package st.zudamue.support.android.component.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

/**
 * Created by xdaniel
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
public interface ItemView extends Serializable
{
	public View createView(int position, LayoutInflater inflater, View view, ViewGroup viewGroup);

	public Object getObject();
}
