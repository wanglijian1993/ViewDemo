package com.gaode.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gaode.R;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    private Button bn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bn = (Button) findViewById(R.id.bn);

    }


    /**
     * 跳转到BlurActivity
     *
     * @param v
     */
    public void toBLur(View v) {
        startActivity(new Intent(this, BlurActivity.class));
    }

    public void toIndex(View v) {
        startActivity(new Intent(this, IndexActivity.class));
    }

}
