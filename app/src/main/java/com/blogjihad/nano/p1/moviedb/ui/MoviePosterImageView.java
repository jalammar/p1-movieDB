package com.blogjihad.nano.p1.moviedb.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ImageView that retains the correct aspect ratio of movieDB posters
 */
public class MoviePosterImageView extends ImageView {
    public MoviePosterImageView(Context context){
        super(context);
    }
    public MoviePosterImageView(Context context, AttributeSet attr){
        super(context, attr);
    }
    public MoviePosterImageView(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth() * 1.5));
    }
}
