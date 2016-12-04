package com.galante.martin.opentendsapplication.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.galante.martin.opentendsapplication.R;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int EXTRA_PARAM_ID = 0;
    public static final String EXTRA_PARAM_NAME = "HeroName";
    public static final String EXTRA_PARAM_DESCRIPTION = "HeroDescription";
    public static final String EXTRA_PARAM_IMAGE = "HeroImage";
    private LinearLayout mainInformationHolder;
    private ImageView imageView;
    private TextView textViewName;
    private TextView textViewDescription;
    private TextView txtvwLinks;
    private Button btnDetails;
    private Button btnWiki;
    private Button btnComics;
    private TextView txtvwResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViews();
        loadHeroInfo();

    }


    private void findViews() {
        mainInformationHolder = (LinearLayout) findViewById(R.id.main_information_holder);
        imageView = (ImageView) findViewById(R.id.image_view);
        textViewName = (TextView) findViewById(R.id.text_view_name);
        textViewDescription = (TextView) findViewById(R.id.text_view_description);
        txtvwLinks = (TextView) findViewById(R.id.txtvw_links);
        btnDetails = (Button) findViewById(R.id.btn_details);
        btnWiki = (Button) findViewById(R.id.btn_wiki);
        btnComics = (Button) findViewById(R.id.btn_comics);
        txtvwResources = (TextView) findViewById(R.id.txtvw_resources);

        btnDetails.setOnClickListener(this);
        btnWiki.setOnClickListener(this);
        btnComics.setOnClickListener(this);


    }

    private void loadHeroInfo() {
        textViewName.setText(getIntent().getExtras().getString(EXTRA_PARAM_NAME));
        textViewDescription.setText(getIntent().getExtras().getString(EXTRA_PARAM_DESCRIPTION));
        Bitmap _bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra(EXTRA_PARAM_IMAGE), 0, getIntent().getByteArrayExtra(EXTRA_PARAM_IMAGE).length);
        imageView.setImageBitmap(_bitmap);
        //TODO change this?
        if (_bitmap != null) {
            colorize(_bitmap);
        }
        //TODO delete this
        //AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        //alphaAnimation.setDuration(1000);
        //imageView.startAnimation(alphaAnimation);
    }


    private void colorize(Bitmap _bitmap) {
        int default_color = getResources().getColor(R.color.light_gray);
        Palette mPalette = Palette.from(_bitmap).generate();
        mainInformationHolder.setBackgroundColor(mPalette.getLightVibrantColor(default_color));
        //View view = this.getWindow().getDecorView();
        //view.setBackgroundColor(mPalette.getLightMutedColor(default_color));
    }

    @Override
    public void onClick(View v) {
        if (v == btnDetails) {
            // Handle clicks for btnDetails
        } else if (v == btnWiki) {
            // Handle clicks for btnWiki
        } else if (v == btnComics) {
            // Handle clicks for btnComics
        }
    }
}
