package sbname.sbcamera;

import android.hardware.Camera;
import android.view.SurfaceView;

import java.io.IOException;

import sbname.sbcamera.listener.SbCameraListener;

/**
 * Created by WindPush on 2017/2/6.
 */

public class SbCamera {
    public static final int ERROR_CODE_CAMERA_OPEN_FAIL = -1;
    public static final int ERROR_CODE_CAMERA_PREVIEW_FAIL = -2;

    private SbCameraListener mSbCameraListener;

    private Camera mCamera;

    private static class SbCameraHolder {
        private static SbCamera INSTANCE = new SbCamera();
    }

    private SbCamera() {
    }

    public static SbCamera getInstance() {
        return SbCameraHolder.INSTANCE;
    }

    public void startPreviewWithSurface(SurfaceView surfaceView){
        boolean openSuccess = openCamera();
        if (!openSuccess) {
            if (mSbCameraListener != null) {
                mSbCameraListener.error(ERROR_CODE_CAMERA_OPEN_FAIL);
            }
            return;
        }

        try {
            mCamera.setPreviewDisplay(surfaceView.getHolder());
        } catch (IOException e) {
            if (mSbCameraListener != null) {
                mSbCameraListener.error(ERROR_CODE_CAMERA_PREVIEW_FAIL);
            }
            e.printStackTrace();
        }
    }

    private boolean openCamera() {
        if (mCamera == null) {
            mCamera = Camera.open();
        }

        return mCamera == null ;
    }

    public void release() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
            mSbCameraListener.cameraDestory();
        }
    }

    public void setSbCameraListener(SbCameraListener sbCameraListener) {
        mSbCameraListener = sbCameraListener;
    }

}
