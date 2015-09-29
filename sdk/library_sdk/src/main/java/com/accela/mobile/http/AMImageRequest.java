package com.accela.mobile.http;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.accela.mobile.http.volley.AuthFailureError;
import com.accela.mobile.http.volley.Response;
import com.accela.mobile.http.volley.toolbox.ImageRequest;

import java.util.Map;

/**
 * Created by eyang on 8/25/15.
 */
public class AMImageRequest extends ImageRequest{
    private Map<String, String> customHttpHeader;

    /**
     * Creates a new image request, decoding to a maximum specified width and
     * height. If both width and height are zero, the image will be decoded to
     * its natural size. If one of the two is nonzero, that dimension will be
     * clamped and the other one will be set to preserve the image's aspect
     * ratio. If both width and height are nonzero, the image will be decoded to
     * be fit in the rectangle of dimensions width x height while keeping its
     * aspect ratio.
     *
     * @param url           URL of the image
     * @param listener      Listener to receive the decoded bitmap
     * @param maxWidth      Maximum width to decode this bitmap to, or zero for none
     * @param maxHeight     Maximum height to decode this bitmap to, or zero for
     *                      none
     * @param scaleType     The ImageViews ScaleType used to calculate the needed image size.
     * @param decodeConfig  Format to decode the bitmap to
     * @param errorListener Error listener, or null to ignore errors
     */
    public AMImageRequest(String url, Map<String, String> customHttpHeader, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
        this.customHttpHeader = customHttpHeader;
        this.setRetryPolicy(new AMRetryPolicy());
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return customHttpHeader;
    }
}
