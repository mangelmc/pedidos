package com.example.mike.pedidos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnticipateOvershootInterpolator;

import android.widget.Button;

import com.example.mike.pedidos.utils.Constants;

import java.util.Objects;

public class AnimationsActivity extends AppCompatActivity {
    Constants.TransitionType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animations);

        initPage();
        initAnimation();


    }


    private void initPage() {
        type = (Constants.TransitionType) getIntent().getSerializableExtra(Constants.KEY_ANIM_TYPE);
        String toolbarTitle = getIntent().getExtras().getString(Constants.KEY_TITLE);

        Button btnExit = findViewById(R.id.btnExitAs);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(toolbarTitle);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }

    private void initAnimation() {
        switch (type) {
            case ExplodeJava: {
                Explode enterTransition = new Explode();
                enterTransition.setDuration(getResources().getInteger(R.integer.anim_long));
                getWindow().setEnterTransition(enterTransition);
                break;
            }
            case SlideJava:{
                Slide enterTransition = new Slide();
                enterTransition.setSlideEdge(Gravity.LEFT);
                enterTransition.setDuration(getResources().getInteger(R.integer.anim_very_long));
                enterTransition.setInterpolator(new AnticipateOvershootInterpolator());
                //enterTransition.setInterpolator(new BounceInterpolator());
                getWindow().setEnterTransition(enterTransition);

                break;

            }
            case FadeJava: {
                Fade enterTransition = new Fade();
                enterTransition.setDuration(getResources().getInteger(R.integer.anim_medium));
                getWindow().setEnterTransition(enterTransition);
                break;
            }

        }


    }
}
