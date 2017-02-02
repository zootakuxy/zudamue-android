package st.domain.support.android.adapter;

import android.support.v4.app.Fragment;

import java.util.List;
import java.util.Map;

import st.domain.support.android.model.ItemFragment;

/**
 *
 * Created by dchost on 30/01/17.
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
