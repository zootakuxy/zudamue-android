package st.zudamue.support.android.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

public interface ItemView extends Serializable
{
	public View createView(int position, LayoutInflater inflater, View view, ViewGroup viewGroup);

	public Object getObject();
}
