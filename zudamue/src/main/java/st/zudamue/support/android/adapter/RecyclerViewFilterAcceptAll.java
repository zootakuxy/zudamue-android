package st.zudamue.support.android.adapter;

/**
 * Created by xdaniel on 6/26/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com/>
 */

public class RecyclerViewFilterAcceptAll implements RecyclerViewFilter  {
    @Override
    public boolean accept(ItemDataSet itemDataSet) {
        return true;
    }
}
