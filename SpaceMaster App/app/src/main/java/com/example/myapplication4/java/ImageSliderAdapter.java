package com.example.myapplication4.java;

import android.content.Context;
import android.view.LayoutInflater;
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
    private List<Integer> images;
    private LinearLayout dotsLayout;
    private Boolean once=true;

    public ImageSliderAdapter(Context context, List<Integer> images, LinearLayout dotsLayout) {
        this.context = context;
        this.images = images;
        this.dotsLayout = dotsLayout;
    }

    @Override
    public int getCount() {
        return images.size();
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


        ImageView imageViewBackground = slideView.findViewById(R.id.imageViewBackground);
        ImageView imageViewForeground = slideView.findViewById(R.id.imageViewForeground);


//        dotsLayout= slideView.findViewById(R.id.dotsLayout);
        imageViewBackground.setImageResource(images.get(position));
        imageViewForeground.setImageResource(R.drawable.p2_ship_default3);


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

