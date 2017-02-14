package sbname.sbcamera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.opengles.GL10;

import sbname.sbcamera.opengl.SbRender;


/**
 * Created by WindPush on 2017/2/6.
 * choose the glsurface in order to support with some cool feature like prisma style filter .scale or other interesting preview looks.
 */

public class SbCameraView extends GLSurfaceView implements  SbCameraPresenter {
    private int mPreviewWith = 0;
    private int mPreviewHeight = 0;
    private int mTextureId = -1;
    private SbRender mSbRender;
    private SurfaceTexture mSurfaceTexture;
    /**
     * Standard View constructor. In order to render something, you
     * must call {@link #setRenderer} to register a renderer.
     *
     * @param context
     */
    public SbCameraView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mSbRender  = new SbRender(this);
        setRenderer(mSbRender);
        //RENDERMODE_WHEN_DIRTY  means it will clear only when render.request called.
        setRenderMode(RENDERMODE_WHEN_DIRTY);
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
        setEGLContextClientVersion(2);
        mSbRender  = new SbRender(this);
        setRenderer(mSbRender);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
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


    public void release(){
        SbCamera.getInstance().releaseCamera();
    }

    private int getGlTextureId(){
        int[] texture = new int[1];
        GLES20.glGenTextures(1,texture,0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }
}
