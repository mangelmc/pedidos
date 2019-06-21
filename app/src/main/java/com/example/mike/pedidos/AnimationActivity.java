package com.example.mike.pedidos;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mike.pedidos.utils.Constants;

import static android.util.Pair.create;

public class AnimationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textExample;
    private ImageView imgUser, imgLogo;
    private Button btnExplode, btnSlide, btnFade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        loadComponents();

    }


    private void loadComponents() {
        textExample = findViewById(R.id.textExample);
        imgUser = findViewById(R.id.imgUser);
        imgLogo = findViewById(R.id.imgLogo);
        btnExplode = findViewById(R.id.btnExplode);
        btnSlide = findViewById(R.id.btnSlide);
        btnFade = findViewById(R.id.btnFade);

        btnExplode.setOnClickListener(this);
        btnSlide.setOnClickListener(this);
        btnFade.setOnClickListener(this);


    }

    public void nextActivity(View view) {

        sharedElementTransition();
    }

    private void sharedElementTransition() {


        Intent intent = new Intent(this, Animation2Activity.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Pair pair1, pair2, pair3;
            pair1 = create(imgLogo, "img_logo_shared");
            pair2 = create(imgUser, "img_user_shared");
            pair3 = create(textExample, "text_shared");

            ActivityOptions options;

            options = ActivityOptions.makeSceneTransitionAnimation(this, pair1, pair2, pair3);

            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AnimationsActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        switch (v.getId()) {
            case R.id.btnExplode: {

                intent.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.ExplodeJava);
                intent.putExtra(Constants.KEY_TITLE, "Explode by Java");
                break;
            }
            case R.id.btnSlide: {
                intent.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                intent.putExtra(Constants.KEY_TITLE, "Slide by Java");
                break;
            }
            case R.id.btnFade: {
                intent.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.FadeJava);
                intent.putExtra(Constants.KEY_TITLE, "Fade by Java");
                break;
            }
            default: {
                Toast.makeText(this, "No seleccionó nada", Toast.LENGTH_SHORT).show();
            }
        }

        startActivity(intent, options.toBundle());
    }
}
