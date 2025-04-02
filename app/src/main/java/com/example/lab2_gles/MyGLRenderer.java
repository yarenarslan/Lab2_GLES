package com.example.lab2_gles;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Ribbon ribbon;
    private Heptagon heptagon;
    private Triangle triangle;

    private boolean isRotating = true;
    private int currentShapeIndex = 0; // 0: Heptagon, 1: Triangle

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1.0f); // siyah arka plan
        ribbon = new Ribbon();
        heptagon = new Heptagon();  // ✅ Tek kez yeter
        triangle = new Triangle();
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if (isRotating) {
            ribbon.update();
        }

        ribbon.draw();

        // 🔄 SADECE SEÇİLİ ŞEKLİ ÇİZ
        switch (currentShapeIndex) {
            case 0:
                heptagon.draw(); // 🟢 SADECE burası
                break;
            case 1:
                triangle.draw(); // 💖
                break;
        }
    }


    public void toggleRotation() {
        isRotating = !isRotating;
    }

    public void changeColorPalette() {
        ribbon.nextColorPalette();
    }

    public void nextShape() {
        currentShapeIndex = (currentShapeIndex + 1) % 2;
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    // Shader helper
    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
