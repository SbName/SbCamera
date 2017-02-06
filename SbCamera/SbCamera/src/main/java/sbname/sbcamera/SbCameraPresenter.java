package sbname.sbcamera;

/**
 * Created by WindPush on 2017/2/6.
 */

public interface SbCameraPresenter {
    SbCameraView setCameraPreviewSize(int previewWidth,int previewHeight);
    SbCameraView setPreviewScaleType(int scaleType);
    void build();


}
