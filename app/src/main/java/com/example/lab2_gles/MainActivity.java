package com.example.lab2_gles;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MyGLSurfaceView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glView = new MyGLSurfaceView(this);
        setContentView(glView);  // ✨ BU satır OpenGL sahnesini gösterir
    }
}
