package com.getit.vivek.yes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;


public class ProductActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView image;
    private ProgressBar spinner;
    String name, phone, type, lattitude, longitude, imagepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product2);
        Bundle extras = getIntent().getExtras();
        imagepath = extras.getString("BeanImage");
        name = extras.getString("BeanName");
        phone = extras.getString("BeanPhone");
        type = extras.getString("BeanType");
        lattitude = extras.getString("BeanLattitude");
        longitude = extras.getString("BeanLongitude");
        customfonts.MyTextView typebean = (customfonts.MyTextView) findViewById(R.id.typebean);
        typebean.setText(type);
        customfonts.MyTextView phonebean = (customfonts.MyTextView) findViewById(R.id.phonebean);
        phonebean.setText(phone);
        //http://cdn.arstechnica.net/wp-content/uploads/2016/02/5718897981_10faa45ac3_b-640x624.jpg
        image = (ImageView) findViewById(R.id.image);
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(ProductActivity.this));
        imageLoader.displayImage(imagepath, image);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.getApplicationContext())
                .imageDownloader(new BaseImageDownloader(this.getApplicationContext(), 5 * 1000, 20 * 1000))
                .build();
        ImageLoader.getInstance().init(config);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar1));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(name);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.background_dark));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.background_dark));
        //setPalette(image);

    }

    public void callproduct(View view){

        String phone = ((customfonts.MyTextView) view.findViewById(R.id.typebean)).getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    private void setPalette(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int primaryDark = getResources().getColor(R.color.primary_dark);
                int primary = getResources().getColor(R.color.primary);
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(primaryDark));
            }
        });

    }

}