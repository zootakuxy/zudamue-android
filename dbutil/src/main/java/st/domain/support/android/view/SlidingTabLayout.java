/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package st.domain.support.android.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import st.domain.support.android.R;
import st.domain.support.android.model.ImageTextResource;

/**
 * To be used with ViewPager to provide a tab indicator component which give constant feedback as to
 * the user's scroll progress.
 * <p>
 * To use the component, simply addFragment it to your view hierarchy. Then in your
 * {@link android.app.Activity} or {@link android.support.v4.app.Fragment} call
 * {@link #setViewPager(ViewPager)} providing it the ViewPager this layout type being used for.
 * <p>
 * The colors can be customized in two ways. The first and simplest type to provide an array of colors
 * via {@link #setSelectedIndicatorColors(int...)}. The
 * alternative type via the {@link TabColorizer} interface which provides you complete control over
 * which color type used for any individual position.
 * <p>
 * The views used as tabs can be customized by calling {@link #setCustomTabView(int, int)},
 * providing the layout ID of your custom layout.
 */
public class SlidingTabLayout extends HorizontalScrollView
{
    private int mTabViewImageView;
    private boolean useColorizerOnTextColor;
    private boolean definedTextColor;
    private int colorSelectedTrue;
    private int colorSelectedFalse;
    private InternalViewPagerListener internalViewPagerListener;

    /**
     * Allows complete control over the colors drawn in the tab layout. Set with
     * {@link #setCustomTabColorizer(TabColorizer)}.
     */
    public interface TabColorizer {

        /**
         * @return return the color of the indicator used when {@code position} type selected.
         */
        int getIndicatorColor(int position);

    }

    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private boolean mDistributeEvenly;

    private ViewPager mViewPager;
    private SparseArray<String> mContentDescriptions = new SparseArray<>();
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;
    private OnAbaCreated onAbaCreated;
    private TypeAba typeAba;

    private final SlidingTabStrip mTabStrip;

    public SlidingTabLayout(Context context)
    {
        this(context, null);
        this.typeAba = TypeAba.TEXT_ONLY;
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.typeAba = TypeAba.TEXT_ONLY;
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);

        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new SlidingTabStrip(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.typeAba = TypeAba.TEXT_ONLY;
    }


    /**
     * Set the custom {@link TabColorizer} to be used.
     *
     * If you only require simple custmisation then you can use
     * {@link #setSelectedIndicatorColors(int...)} to achieve
     * similar effects.
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer)
    {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    public void setTypeAba(TypeAba typeAba) {
        this.typeAba = typeAba;
        if(typeAba == null)
        {
            this.mTabViewLayoutId = 0;
            this.mTabViewTextViewId = 0;
        }
        else if(typeAba == TypeAba.TEXT_ONLY)
        {
            this.mTabViewLayoutId = R.layout.lib_aba_text_only;
            this.mTabViewTextViewId = R.id.lib_aba_text;
        }
        else if(typeAba == TypeAba.ICON_ONLY)
        {
            this.mTabViewLayoutId = R.layout.lib_aba_icon;
            this.mTabViewImageView = R.id.lib_aba_icon;
        }
        else if(typeAba == TypeAba.TEXT_AND_ICON)
        {
            this.mTabViewLayoutId = R.layout.lib_aba_icon_text;
            this.mTabViewTextViewId = R.id.lib_aba_text;
            this.mTabViewImageView = R.id.lib_aba_icon;
        }
    }

    public SlidingTabStrip getTabStrip()
    {
        return mTabStrip;
    }


    public void setOnAbaCreated(OnAbaCreated onAbaCreated)
    {
        this.onAbaCreated = onAbaCreated;
    }

    public void setDistributeEvenly(boolean distributeEvenly)
    {
        mDistributeEvenly = distributeEvenly;
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Set the color for text in stat selected and state unselected
     * @param idColorSelectedTrue the color for selected state
     * @param idColorSelectedFalse the color for unselected state
     */
    public void setTextColorSelection(int idColorSelectedTrue, int idColorSelectedFalse)
    {
        this.colorSelectedTrue = getContext().getResources().getColor(idColorSelectedTrue);
        this.colorSelectedFalse = getContext().getResources().getColor(idColorSelectedFalse);
        this.definedTextColor = true;
    }

    /**
     * Set true for use the color of indicator in text view
     * @param useColorizerOnTextColor
     */
    public void useColorizerOnTextColor(boolean useColorizerOnTextColor)
    {
        this.useColorizerOnTextColor = useColorizerOnTextColor;
    }

    public boolean isUseColorizerOnTextColor() {
        return useColorizerOnTextColor;
    }

    public int getColorSelectedTrue() {
        return colorSelectedTrue;
    }

    public int getColorSelectedFalse() {
        return colorSelectedFalse;
    }

    public TypeAba getTypeAba() {
        return typeAba;
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using {@link SlidingTabLayout} you are
     * required to set any {@link ViewPager.OnPageChangeListener} through this method. This type so
     * that the layout can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener)
    {
        mViewPagerPageChangeListener = listener;
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
        this.typeAba = null;
        this.mTabViewTextViewId = 0;
    }

    /**
     * Sets the associated view pager. Note that the assumption here type that the pager content
     * (number of tabs and tab titles) does not replace after this call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(this.internalViewPagerListener = new InternalViewPagerListener());
            populateTabStrip();
        }
    }



    /**
     * Create a default view to be used for tabs. This type called if a custom tab view type not set via
     * {@link #setCustomTabView(int, int)}.
     */
    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        textView.setBackgroundResource(outValue.resourceId);
        textView.setAllCaps(true);

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    private void populateTabStrip()
    {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int position = 0; position < adapter.getCount(); position++)
        {
            View tabView = null;
            TextView tabTitleView = null;
            ImageView imageView = null;

            if (typeAba != null)
            {
                // If there type a custom tab view layout id set, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip, false);

                if(this.typeAba == TypeAba.TEXT_AND_ICON)
                {
                    imageView = (ImageView) tabView.findViewById(this.mTabViewImageView);
                    tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
                }
                else if(typeAba == TypeAba.ICON_ONLY)
                    imageView = (ImageView) tabView.findViewById(this.mTabViewImageView);
                else if(typeAba == TypeAba.TEXT_ONLY)
                    tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);

            }
            else if(mTabViewLayoutId != 0)
            {
                // If there type a custom tab view layout id set, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip, false);
                tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
            }

            if (tabView == null && imageView == null)
            {
                tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null
                    && imageView == null
                    && TextView.class.isInstance(tabView))
            {
                tabTitleView = (TextView) tabView;
            }

            if (mDistributeEvenly)
            {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.width = 0;
                lp.weight = 1;
            }

            CharSequence title = adapter.getPageTitle(position);

            if(tabTitleView != null)
            {
                tabTitleView.setText(title);
            }

            if(title instanceof ImageTextResource
                    && imageView != null)
            {
                ImageTextResource imageTitle = (ImageTextResource) title;
                if(imageTitle.getIdImageResource() != 0)
                    imageView.setImageResource(imageTitle.getIdImageResource());
            }

            tabView.setOnClickListener(tabClickListener);
            String desc = mContentDescriptions.get(position, null);
            if (desc != null)
            {
                tabView.setContentDescription(desc);
            }

            if(this.onAbaCreated != null) this.onAbaCreated.onAbaCreated(tabView, position);

            mTabStrip.addView(tabView);
            if (position == mViewPager.getCurrentItem())
            {
                tabView.setSelected(true);
            }
        }
        this.internalViewPagerListener.onPageSelected(this.mViewPager.getCurrentItem());
    }

    public void setContentDescription(int i, String desc) {
        mContentDescriptions.put(i, desc);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position)
        {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }
            for (int i = 0; i < mTabStrip.getChildCount(); i++)
            {
                TextView tv = null;
                View view  = mTabStrip.getChildAt(i);
                if(typeAba == null && mTabViewLayoutId == 0 && view instanceof TextView)
                    tv = (TextView) view;
                else if(mTabViewLayoutId != 0 && mTabViewTextViewId != 0)
                    tv = (TextView) view.findViewById(mTabViewTextViewId);

                if(tv != null )
                {
                    if(i == position && useColorizerOnTextColor)
                        tv.setTextColor(mTabStrip.getTabColorizer().getIndicatorColor(position));
                    else if(i == position && SlidingTabLayout.this.definedTextColor)
                        tv.setTextColor(SlidingTabLayout.this.colorSelectedTrue);
                    else if(i == position)
                        tv.setTextColor(getContext().getResources().getColor(android.R.color.white));
                    else if(i != position && SlidingTabLayout.this.definedTextColor)
                        tv.setTextColor(SlidingTabLayout.this.colorSelectedFalse);
                    else if(i != position)
                        tv.setTextColor(getResources().getColor(R.color.lib_aba_unselected));
                }
            }
            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }

        }



    }


    private class TabClickListener implements OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            for (int i = 0; i < mTabStrip.getChildCount(); i++)
            {
                if (view == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    public enum TypeAba
    {
        TEXT_ONLY,
        TEXT_AND_ICON,
        ICON_ONLY
    }

    /**
     * Quando a aba for creada
     */
    public interface OnAbaCreated
    {
        public  void onAbaCreated(View view, int position);
    }
}
