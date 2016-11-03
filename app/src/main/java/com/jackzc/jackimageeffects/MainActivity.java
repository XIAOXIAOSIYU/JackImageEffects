package com.jackzc.jackimageeffects;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    ImageView imageView;
    Drawable imageFace;
    Bitmap bitmap,newBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image_panel);
        imageFace = ContextCompat.getDrawable(this, R.drawable.face);
        bitmap = ((BitmapDrawable) imageFace).getBitmap();

        // Effect photo

        newBitmap = convertImage(bitmap);
        imageView.setImageBitmap(newBitmap);

        // Save this new photo into the users devices
        // Write permission needed
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        }

        // Filter photo
//        Drawable[] layers = new Drawable[2];
//        layers[0] = ContextCompat.getDrawable(this, R.drawable.face);
//        layers[1] = ContextCompat.getDrawable(this, R.drawable.background);
//
//        LayerDrawable layerDrawable = new LayerDrawable(layers);
//        imageView.setImageDrawable(layerDrawable);

    }

    private static Bitmap convertImage(Bitmap _bitmap) {

        int A, R, G, B;
        int pixelColor;
        int _height, _width;

        _height = _bitmap.getHeight();
        _width = _bitmap.getWidth();

        Bitmap convertedImage = Bitmap.createBitmap(_width, _height, _bitmap.getConfig());

        for (int y = 0; y < _height; y++) {
            for (int x = 0; x < _width; x++) {

                pixelColor = _bitmap.getPixel(x, y);
                A = Color.alpha(pixelColor);
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);

                convertedImage.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return convertedImage;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MediaStore.Images.Media.insertImage(getContentResolver(), newBitmap, "Photo Effects", "My first effects photo");
                }
            }
        }
    }
}
