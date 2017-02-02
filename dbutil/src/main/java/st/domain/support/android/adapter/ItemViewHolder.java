package st.domain.support.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * Created by xdata on 12/25/16.
 */

public abstract class ItemViewHolder extends RecyclerView.ViewHolder {


    public ItemViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(ItemDataSet dataSet);
}
