package sbname.sbcamera;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceView;

import java.io.IOException;
import java.lang.ref.WeakReference;
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
    private WeakReference<Activity> mCurrentWeakActivity;
    private WeakReference<SurfaceView> mCurrentSurfaceView;
    private final int mImageFormat = ImageFormat.NV21;

    private static class SbCameraHolder {
        private static SbCamera INSTANCE = new SbCamera();
    }

    private SbCamera() {
        initCameraInfo();
    }

    public static SbCamera getInstance() {
        return SbCameraHolder.INSTANCE;
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

    private boolean openCamera() {
        if (mCamera == null) {
            mCamera = Camera.open(mCurrentCameraId);
        }
        return mCamera == null;
    }

    public void openBackCamera() {
        releaseCamera();
        if (mBackCameraId >= 0) {
            mCurrentCameraId = mBackCameraId;
        }
        startPreviewWithSurface();
    }

    public void openFrontCamera() {
        releaseCamera();
        if (mFrontCameraId >= 0) {
            mCurrentCameraId = mFrontCameraId;
        }
        startPreviewWithSurface();
    }

    public void bind(SurfaceView surfaceView, Activity currentActivity) {
        mCurrentWeakActivity = new WeakReference<Activity>(currentActivity);
        mCurrentSurfaceView = new WeakReference<SurfaceView>(surfaceView);
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
            mSbCameraListener.cameraDestory();
        }
    }

    public void startPreviewWithSurface() {
        boolean openSuccess = openCamera();
        if (!openSuccess) {
            if (mSbCameraListener != null) {
                mSbCameraListener.error(ERROR_CODE_CAMERA_OPEN_FAIL);
            }
            return;
        }

        try {
            mCamera.setPreviewDisplay(mCurrentSurfaceView.get().getHolder());
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

        List<Integer> formatList = parameters.getSupportedPreviewFormats();
        for (Integer format : formatList) {
            if (format == mImageFormat) {
                parameters.setPreviewFormat(mImageFormat);
                break;
            }
        }
        //set focus mode
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        mCamera.setParameters(parameters);
        adjustRotationForCamera();

    }

    private void adjustRotationForCamera() {
        Activity activity = mCurrentWeakActivity.get();
        if (activity == null) {
            return;
        }
        int currentActivityRotation = getDisplayRotation(activity);

        int result;
        if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (mCameraInfo.orientation + currentActivityRotation) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (mCameraInfo.orientation - currentActivityRotation + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }


    private int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
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
