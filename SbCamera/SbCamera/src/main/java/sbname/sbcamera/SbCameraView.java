package sbname.sbcamera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by WindPush on 2017/2/6.
 */

public class SbCameraView  extends SurfaceView implements SbCameraPresenter{
    private int mPreviewWith = 0;
    private int mPreviewHeight= 0;

    public SbCameraView(Context context) {
        super(context);
    }

    public SbCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SbCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public SbCameraView setCameraPreviewSize(int previewWidth, int previewHeight) {
        return this;
    }

    @Override
    public SbCameraView setPreviewScaleType(int scaleType) {
        return this;
    }

    @Override
    public void build() {

    }
}
