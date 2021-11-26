package com.johnny.chorus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageActivity extends AppCompatActivity {
    private static final String SHARED_ELEMENT = "sharedElement";
    private static final String IMAGE_ID = "imageId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        findViewById(R.id.image).setTransitionName(SHARED_ELEMENT);

        ((ImageView) findViewById(R.id.image)).setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(getIntent().getIntExtra(IMAGE_ID, 0))));
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
    }

    public static String getSharedElement() {
        return SHARED_ELEMENT;
    }

    @NonNull
    public static Intent newIntent(Context context, int imageId) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(IMAGE_ID, imageId);
        return intent;
    }
}