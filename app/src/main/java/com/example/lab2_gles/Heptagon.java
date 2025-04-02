package com.example.lab2_gles;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Heptagon {

    private FloatBuffer vertexBuffer;
    private final int vertexCount = 7;
    private final int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    private float[] coords = new float[vertexCount * COORDS_PER_VERTEX];

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final int mProgram;
    private final float[] color = { 0.4f, 0.6f, 1.0f, 1.0f }; // mavi ton

    public Heptagon() {
        for (int i = 0; i < vertexCount; i++) {
            double angle = 2 * Math.PI * i / vertexCount;
            coords[i * 3] = (float) Math.cos(angle) * 0.3f;
            coords[i * 3 + 1] = (float) Math.sin(angle) * 0.3f;
            coords[i * 3 + 2] = 0.0f;
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coords);
        vertexBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public void draw() {
        GLES20.glUseProgram(mProgram);

        int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        int colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
