package st.zudamue.support.android.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

/**
 *
 * Created by dchost on 27/01/17.
 */

public class ShapDrawableBuilder {

    private final Context context;
    private int solidColorId;
    private int strokeWidth;
    private int strokeColorId;
    private float metric;

    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBotton;

    private int radiusBottomLeft;
    private int radiusBottomRigth;
    private int radiusTopLeft;
    private int radiusTopRight;

    private  int width;
    private int height;
    private Shap shap;


    public ShapDrawableBuilder(Context context) {
        this.context = context;
        this.metric = this.context.getResources().getDisplayMetrics().density;
        GradientDrawable drawable = new GradientDrawable();
        this.width = -1;
        this.height = -1;
    }

    public ShapDrawableBuilder shap(Shap shap) {
        this.shap = shap;
        return this;
    }

    public ShapDrawableBuilder solidColor(int colorId) {
        this.solidColorId = colorId;
        return this;
    }

    /**
     * @param strokeWidth Width of stroke
     * @param strokeColorId color of stroke
     * @return this
     */
    public ShapDrawableBuilder stroke(int strokeWidth, int strokeColorId) {
        this.strokeWidth = strokeWidth;
        this.strokeColorId = strokeColorId;
        return this;
    }

    public ShapDrawableBuilder padding(int padding){
        this.paddingBotton = padding;
        this.paddingRight = padding;
        this.paddingLeft = padding;
        this.paddingTop = padding;
        return this;
    }

    public ShapDrawableBuilder paddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
        return this;
    }

    public ShapDrawableBuilder paddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
        return this;
    }

    public ShapDrawableBuilder paddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
        return this;
    }

    public ShapDrawableBuilder paddingBotton(int paddingBotton) {
        this.paddingBotton = paddingBotton;
        return this;
    }

    public ShapDrawableBuilder radius(int radius){
        this.radiusBottomLeft = radius;
        this.radiusBottomRigth = radius;
        this.radiusTopLeft = radius;
        this.radiusTopRight = radius;
        return this;
    }

    public ShapDrawableBuilder radiusBottomLeft(int radiusBottomLeft) {
        this.radiusBottomLeft = radiusBottomLeft;
        return this;
    }

    public ShapDrawableBuilder radiusBottomRigth(int radiusBottomRigth) {
        this.radiusBottomRigth = radiusBottomRigth;
        return this;
    }

    public ShapDrawableBuilder radiusTopLeft(int radiusTopLeft) {
        this.radiusTopLeft = radiusTopLeft;
        return this;
    }

    public ShapDrawableBuilder radiusTopRight(int radiusTopRight) {
        this.radiusTopRight = radiusTopRight;
        return this;
    }

    public ShapDrawableBuilder width(int width) {
        this.width = width;
        return this;
    }

    public ShapDrawableBuilder height(int height) {
        this.height = height;
        return this;
    }

    /**
     * Build the shap
     * @return
     */
    public Drawable build() {
        GradientDrawable drawable = new GradientDrawable();

        drawable.setShape(this.shap.shap);

        drawable.setColor(this.context.getResources().getColor(solidColorId));
        drawable.setStroke(this.strokeWidth, this.strokeColorId);
        drawable.setStroke(this.strokeWidth, this.strokeColorId);

        int left = (int) (this.metric * paddingLeft);
        int right = (int) (this.metric * paddingRight);
        int top = (int) (this.metric * paddingTop);
        int botton = (int) (this.metric * paddingBotton);

        /**
         * Specifies radii for each of the 4 corners. For each corner, the array contains 2 values,
         * [X_radius, Y_radius]. The corners are ordered
         * top-left,
         * top-right,
         * bottom-right,
         * bottom-left.
         *
         * This property is honored only when the shape is of type RECTANGLE.
         Note: changing this property will affect all instances of a drawable loaded from a resource. It is recommended to invoke mutate() before changing this property.
         */
        drawable.setBounds(left, top, right, botton);
        float[] radius = new float[] {radiusTopLeft, radiusTopLeft,
                radiusTopRight, radiusTopRight,
                radiusBottomLeft, radiusBottomLeft,
                radiusBottomRigth, radiusBottomRigth};

        drawable.setCornerRadii(radius);
        drawable.setSize(this.width, this.height);
        return drawable;
    }


    public enum Shap {
        RETANGLE(GradientDrawable.RECTANGLE),
        OVAL(GradientDrawable.OVAL),
        LINE(GradientDrawable.LINE),
        RING(GradientDrawable.RING);

        private final int shap;
        Shap( int shap ){
            this.shap = shap;
        }
    }

    /*

        android:angle=""
        android:centerColor=""
        android:centerX=""
        android:centerY=""
        android:endColor=""
        android:gradientRadius=""
        android:startColor=""
        android:type=""
        android:useLevel=""
     */
}
