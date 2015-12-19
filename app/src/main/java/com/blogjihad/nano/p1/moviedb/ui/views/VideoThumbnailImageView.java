package com.blogjihad.nano.p1.moviedb.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ImageView that retains the correct aspect ratio of movieDB posters
 */
public class VideoThumbnailImageView extends ImageView {
    public VideoThumbnailImageView(Context context){
        super(context);
    }
    public VideoThumbnailImageView(Context context, AttributeSet attr){
        super(context, attr);
    }
    public VideoThumbnailImageView(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth() * .75));
    }
}
