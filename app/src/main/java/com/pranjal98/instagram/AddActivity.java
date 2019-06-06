package com.pranjal98.instagram;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddActivity extends AppCompatActivity {
    Bitmap bit;
    Uri img;

    public void uploadImg(View view){

        if(img != null){

            Intent uploadImg = new Intent(getApplicationContext(), UploadImg.class);
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.PNG, 100, bStream);
            byte[] byteArray = bStream.toByteArray();
            uploadImg.putExtra("image", byteArray);
            uploadImg.putExtra("img", img.toString());
            startActivity(uploadImg);
            finish();

        }

        else {

            Toast.makeText(this, "Please Choose an image first !!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backFunc(View view){

        Intent back = new Intent(getApplicationContext(), NewsFeed.class);
        startActivity(back);
        finish();
    }

    public void addImg(){

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        else {

            intentFunc();
        }
    }

    public void addImg(View view){

        addImg();
    }

    public void intentFunc(){

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                intentFunc();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addImg();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            Uri selectImg = data.getData();
            img = selectImg;
            Bitmap bitmap = null;

            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectImg);

            }
            catch (IOException e) {

                e.printStackTrace();
            }

            ImageView imageView = (ImageView) findViewById(R.id.addImgId);
            bit = bitmap;
            imageView.setImageBitmap(bitmap);
        }
    }
}
