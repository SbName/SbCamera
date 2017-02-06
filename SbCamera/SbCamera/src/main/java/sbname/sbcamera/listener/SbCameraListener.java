package sbname.sbcamera.listener;

/**
 * Created by WindPush on 2017/2/6.
 */

public interface SbCameraListener {
    void error(int errorCode);
    void cameraOpened();
    void cameraDestory();
}
