package sbname.sbcamera;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

import sbname.sbcamera.listener.SbCameraListener;

import static android.hardware.Camera.getCameraInfo;
import static android.hardware.Camera.getNumberOfCameras;

/**
 * Created by WindPush on 2017/2/6.
 */

public class SbCamera {

    public static final int ERROR_CODE_CAMERA_OPEN_FAIL = -1;
    public static final int ERROR_CODE_CAMERA_PREVIEW_FAIL = -2;

    private int mPreviewWidth;
    private int mPreviewHeight;

    private int mCurrentCameraId = -1;
    private int mFrontCameraId = -1;
    private int mBackCameraId = -1;
    private int mTotalCameraNums = 0;

    private SbCameraListener mSbCameraListener;
    private Camera mCamera;
    private Camera.CameraInfo mCameraInfo;
    private final int mImageFormat = ImageFormat.NV21;

    private static class SbCameraHolder {
        private static SbCamera INSTANCE = new SbCamera();
    }

    private SbCamera() {
        initCameraInfo();
    }

    private void initCameraInfo() {
        int numberOfCameras = getNumberOfCameras();
        mTotalCameraNums = numberOfCameras;
        mCameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            getCameraInfo(i, mCameraInfo);
            if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mBackCameraId = i;
            }

            if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mFrontCameraId = i;
            }
        }
    }

    public static SbCamera getInstance() {
        return SbCameraHolder.INSTANCE;
    }

    public void startPreviewWithSurface(SurfaceView surfaceView) {
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

        initDefaultParamsForCamera();
    }

    private void initDefaultParamsForCamera() {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            parameters.setRecordingHint(true);
        }

        parameters.setPreviewFormat(mImageFormat);

        //set focus mode
        final List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        } else if(focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        } else {
        }

        mCamera.setParameters(parameters);
    }

    private void adjustRotationForCamera() {

    }


    private boolean openCamera() {
        if (mCamera == null) {
            mCamera = Camera.open();
        }

        return mCamera == null;
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

    public int getPreviewWidth() {
        return mPreviewWidth;
    }

    public void setPreviewWidth(int previewWidth) {
        mPreviewWidth = previewWidth;
    }

    public int getPreviewHeight() {
        return mPreviewHeight;
    }

    public void setPreviewHeight(int previewHeight) {
        mPreviewHeight = previewHeight;
    }
}
