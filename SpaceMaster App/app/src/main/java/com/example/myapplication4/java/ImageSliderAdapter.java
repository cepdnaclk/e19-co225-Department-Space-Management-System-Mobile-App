package com.example.myapplication4.java;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.myapplication4.R;

import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {
    private Context context;
    private List<List<Integer>> images;
    private LinearLayout dotsLayout;
    private Boolean once=true;
    private  NewBookingFragment newBookingFragment;


    public ImageSliderAdapter(Context context, List<List<Integer>> images, LinearLayout dotsLayout,NewBookingFragment newBookingFragment) {
        this.context = context;
        this.images = images;
        this.dotsLayout = dotsLayout;
        this.newBookingFragment=newBookingFragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View slideView = inflater.inflate(R.layout.item_slide, container, false);


        ImageView imageViewBase= slideView.findViewById(R.id.imageViewBase);
        ImageView imageViewImage = slideView.findViewById(R.id.imageViewImage);
        ImageView imageView1Green= slideView.findViewById(R.id.imageView1Green);
        ImageView imageView1Red = slideView.findViewById(R.id.imageView1Red);
        ImageView imageView2Green= slideView.findViewById(R.id.imageView2Green);
        ImageView imageView2Red = slideView.findViewById(R.id.imageView2Red);


//        dotsLayout= slideView.findViewById(R.id.dotsLayout);

        imageViewBase.setImageResource(images.get(position).get(0));
        imageViewImage.setImageResource(images.get(position).get(1));
        imageView1Green.setImageResource(images.get(position).get(2));
        imageView1Red.setImageResource(images.get(position).get(3));
        imageView2Green.setImageResource(images.get(position).get(4));
        imageView2Red.setImageResource(images.get(position).get(5));

        newBookingFragment.updateImageView(position,imageViewBase,imageViewImage,imageView1Green,imageView1Red,imageView2Green,imageView2Red);



        container.addView(slideView);
        if(once) {
            setupDots(position);
            once=false;
        }
        return slideView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void setupDots(int position) {
        dotsLayout.removeAllViews();
        for (int i = 0; i < getCount(); i++) {
            ImageView dot = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            int margin = context.getResources().getDimensionPixelSize(R.dimen.dot_margin);
            params.setMargins(margin, 0, margin, 0);
            dot.setLayoutParams(params);
            dot.setImageResource(i == position ? R.drawable.indicator_dot_selected : R.drawable.indicator_dot);
            dotsLayout.addView(dot);
        }
    }
}

