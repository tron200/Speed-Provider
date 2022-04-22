package com.speed.provider.helper;

import android.graphics.Bitmap;

/**
 * To be used with bitmap task worker for creating short scaled images
 *
 * @author Rajesh sharma
 */
public interface BitmapCompletion {

    void onBitmapScaleComplete(Bitmap bmp);

}
