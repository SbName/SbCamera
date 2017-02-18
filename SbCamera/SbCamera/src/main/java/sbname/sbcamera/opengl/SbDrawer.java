package sbname.sbcamera.opengl;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by WindPush on 2017/2/13.
 */

public class SbDrawer {
    private final String mVertexShader =
            "uniform mat4 uMVPMatrix;\n" +
                    "attribute vec4 aPosition;\n" +
                    "attribute vec2 aTextureCoord;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "void main() {\n" +
                    "  gl_Position = uMVPMatrix * aPosition;\n" +
                    "  vTextureCoord = aTextureCoord;\n" +
                    "}\n";

    private final String mFragmentShader =
            "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n";

    private static final float[] VERTICES = {
            1.0f, 1.0f,
            -1.0f, 1.0f,
            1.0f, -1.0f,
            -1.0f, -1.0f
    };
    private static final float[] TEXCOORD = {
            1.0f, 1.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f
    };


    private final FloatBuffer mVerticesFloatBuffer;
    private static final int FLOAT_SZ = Float.SIZE / 8;

    /**
     * create external texture
     *
     * @return texture ID
     */

    public SbDrawer() {
        mVerticesFloatBuffer = ByteBuffer.allocateDirect(8 * FLOAT_SZ)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVerticesFloatBuffer.put(VERTICES);
        mVerticesFloatBuffer.flip();
    }

    public static int initTex() {
        //初始化texture
        final int[] texId = new int[1];
        //拿到第一个Texure 并且存在texId 的texId[0]
        GLES20.glGenTextures(1, texId, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texId[0]);
        //设置一堆参数  不卵他
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        return texId[0];
    }
}
