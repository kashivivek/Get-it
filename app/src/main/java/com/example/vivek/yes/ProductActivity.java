package com.example.vivek.yes;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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


public class ProductActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView image;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product2);
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        String url = "http://farm1.static.flickr.com/114/298125983_0e4bf66782_b.jpg";
        //http://cdn.arstechnica.net/wp-content/uploads/2016/02/5718897981_10faa45ac3_b-640x624.jpg
        image = (ImageView) findViewById(R.id.image);
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(ProductActivity.this));
        imageLoader.displayImage(url, image);
        spinner.setVisibility(View.GONE);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar1));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Product");
       // collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.background_dark));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.background_dark));
        //setPalette(image);

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