package com.example.sahilsa.gallerycamerademo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zhihu.matisse.Matisse;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_GALLERY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);
    }

    public void pickMedia(View view) {
        Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GALLERY) {
            if (resultCode == RESULT_OK) {
                List<Uri> uriList = Matisse.obtainResult(data);
                List<String> pathList = Matisse.obtainPathResult(data);
                Toast.makeText(MainActivity.this, "Got the data " + uriList.size() + " - - " + pathList.size(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
