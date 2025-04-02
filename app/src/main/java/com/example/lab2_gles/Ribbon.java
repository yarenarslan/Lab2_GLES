package com.example.lab2_gles;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Ribbon {

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;

    private final int vertexCount = 7;
    private final int COORDS_PER_VERTEX = 3;
    private final int COLOR_COMPONENTS = 4;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    private float angle = 0.0f;

    private float[] coords = new float[vertexCount * COORDS_PER_VERTEX];
    private float[] colors = new float[vertexCount * COLOR_COMPONENTS];

    // 🎨 RENK PALETLERİ
    private final float[][] colorPalettes = {
            {1f, 0f, 0f},   // Kırmızı
            {0f, 1f, 0f},   // Yeşil
            {0f, 0f, 1f},   // Mavi
            {1f, 1f, 0f},   // Sarı
            {1f, 0f, 1f},   // Mor
            {0f, 1f, 1f}    // Camgöbeği
    };

    private int currentPaletteIndex = 0;
    private final int totalPaletteCount = colorPalettes.length;

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "attribute vec4 aColor;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "  vColor = aColor;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final int mProgram;

    public Ribbon() {
        ByteBuffer vb = ByteBuffer.allocateDirect(coords.length * 4);
        vb.order(ByteOrder.nativeOrder());
        vertexBuffer = vb.asFloatBuffer();

        ByteBuffer cb = ByteBuffer.allocateDirect(colors.length * 4);
        cb.order(ByteOrder.nativeOrder());
        colorBuffer = cb.asFloatBuffer();

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    // 💫 RENK PALETİ DEĞİŞTİR
    public void nextColorPalette() {
        currentPaletteIndex = (currentPaletteIndex + 1) % totalPaletteCount;
    }

    // 🌈 ANİMASYONU GÜNCELLE
    public void update() {
        angle += 0.01f;

        float[] baseColor = colorPalettes[currentPaletteIndex];

        for (int i = 0; i < vertexCount; i++) {
            double a = angle + (2 * Math.PI * i / vertexCount);
            coords[i * 3] = (float) Math.cos(a) * 0.6f;
            coords[i * 3 + 1] = (float) Math.sin(a) * 0.6f;
            coords[i * 3 + 2] = 0f;

            // Tüm noktaları seçilen palete göre boyuyoruz
            colors[i * 4] = baseColor[0];
            colors[i * 4 + 1] = baseColor[1];
            colors[i * 4 + 2] = baseColor[2];
            colors[i * 4 + 3] = 1.0f; // Opaklık
        }

        vertexBuffer.put(coords).position(0);
        colorBuffer.put(colors).position(0);
    }

    // 🎯 ÇİZİM METODU
    public void draw() {
        GLES20.glUseProgram(mProgram);

        int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        int colorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(colorHandle, COLOR_COMPONENTS,
                GLES20.GL_FLOAT, false, COLOR_COMPONENTS * 4, colorBuffer);

        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);
    }
}
