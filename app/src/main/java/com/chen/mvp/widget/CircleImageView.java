package com.chen.mvp.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by chen on 2017/8/2.
 */

public class CircleImageView extends ImageView {
    private Paint mPant;
    private Bitmap bitmap;

    private void initData() {
        mPant = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (null != drawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap b = getCircleBitmap(bitmap, 14); final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight()); final Rect rectDest = new Rect(0,0,getWidth(),getHeight());
            mPant.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, mPant);

        } else {
            super.onDraw(canvas);
        }

    }

    private Bitmap getCircleBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mPant.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        mPant.setColor(color); int x = bitmap.getWidth();

        canvas.drawCircle(x / 2, x / 2, x / 2, mPant);
        mPant.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, mPant);
        return output;
    }

    public CircleImageView(Context context) {
        super(context);
        initData();
    }



    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    public void setBackground(int portrait) {
        this.setImageResource(portrait);
        invalidate();
    }
}
