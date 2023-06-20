package com.example.myapplication4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        FirebaseUser currentUser = mAuth.getCurrentUser();
        ImageView b=findViewById(R.id.image_1);
        ImageView c=findViewById(R.id.image_2);

        Button button = findViewById(R.id.supa2button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent send = new Intent(MainActivity2.this, SignInActivity.class);
                startActivity(send);
            }
        });
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        ImageView a=findViewById(R.id.image);
        Toast.makeText(MainActivity2.this, Integer.toString(a.getMaxWidth()), Toast.LENGTH_SHORT).show();
        a.setOnTouchListener(new View.OnTouchListener()

        {
            @Override
            public boolean onTouch (View v, MotionEvent ev){
                final int action = ev.getAction();
                // (1)
                final int evX = (int) ev.getX();
                final int evY = (int) ev.getY();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_UP:
                        ImageView img = findViewById (R.id.image_areas);
                        img.setDrawingCacheEnabled(true);
                        Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
                        img.setDrawingCacheEnabled(false);
                        int tolerance = 25;
                        int color=hotspots.getPixel(evX, evY);
                        if(Math.abs(color+1172700)<100){
                            b.setVisibility(View.VISIBLE);
                            c.setVisibility(View.INVISIBLE);
                        } else if (Math.abs(color+8421505)<100) {
                            b.setVisibility(View.INVISIBLE);
                            c.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(MainActivity2.this, Integer.toString(Color.RED)+" "+Integer.toString(hotspots.getPixel(evX, evY)), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }


}