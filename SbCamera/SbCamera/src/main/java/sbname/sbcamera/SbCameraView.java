package sbname.sbcamera;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by WindPush on 2017/2/6.
 * choose the glsurface in order to support with some cool feature like prisma style filter .scale or other interesting preview looks.
 */

public class SbCameraView  extends GLSurfaceView implements SbCameraPresenter{
    private int mPreviewWith = 0;
    private int mPreviewHeight= 0;

    /**
     * Standard View constructor. In order to render something, you
     * must call {@link #setRenderer} to register a renderer.
     *
     * @param context
     */
    public SbCameraView(Context context) {
        super(context);
    }

    /**
     * Standard View constructor. In order to render something, you
     * must call {@link #setRenderer} to register a renderer.
     *
     * @param context
     * @param attrs
     */
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
