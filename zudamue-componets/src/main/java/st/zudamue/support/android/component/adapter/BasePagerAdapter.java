package st.zudamue.support.android.component.adapter;

import android.support.v4.app.Fragment;

import java.util.List;
import java.util.Map;

import st.zudamue.support.android.component.model.ItemFragment;

/**
 * Created by xdaniel on 30/01/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public interface BasePagerAdapter{

    Fragment getItem(String tag);

    int getItemPosition(String key);

    ItemFragment getItemFragment(int position);

    void addFragment(ItemFragment itemFragment);

    void clear();

    List<ItemFragment> getListFragments();

    Map<CharSequence, ItemFragment> getMap();

}
