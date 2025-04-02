package com.example.lab2_gles;

import android.view.MotionEvent;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;

public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer renderer;
    private final GestureDetector gestureDetector;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2); // OpenGL ES 2.0 kullan

        renderer = new MyGLRenderer();
        setRenderer(renderer);

        // 💖 Gestures: çift tıklama = renk değiştir, uzun bas = şekil değiştir
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                renderer.changeColorPalette(); // 🎨 Renk paleti değiştir
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                renderer.nextShape(); // 🌀 Şekil değiştir
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 🎯 Gesture’ları kontrol et (çift tık, uzun bas)
        gestureDetector.onTouchEvent(event);

        // 👆 Tek tıklama: ribbon dur/dön
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            renderer.toggleRotation();
            return true;
        }

        return super.onTouchEvent(event);
    }
}
