package tv.tipsee.vr.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by liqingyi on 16/6/22.
 */


/**
 * An image view which always remains square with respect to its width.
 */
public final class SquaredImageView extends ImageView {
    public SquaredImageView(Context context) {
        super(context);
    }

    public SquaredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = (int) (width * 3 / 4.0);
        setMeasuredDimension(width, height);
    }
}
