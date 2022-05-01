package com.example.projectem8.Glide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.projectem8.MainActivity;
import com.example.projectem8.R;

import java.util.ArrayList;
import java.util.Objects;

public class SlidingAdapter extends PagerAdapter {
    private ArrayList<String> urls;
    private Context context;

    public SlidingAdapter(Context context, ArrayList<String> urls) {
        this.context = context;
        this.urls = urls;
    }
    //destroyItem ens servirà per eliminar l’item actual
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

    //isViewFromObject determina si una vista de pàgina està associada a un objecte clau específic tal com retorna instantiateItem(ViewGroup, int).
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    //getCount determina la quantitat d’elements a mostrar, per tant ha de fer un return de la mida de l’ArrayList
    @Override
    public int getCount() {
        return urls.size();
    }

    //instantiateItem crea la vista per la posició donada
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = LayoutInflater.from(view.getContext()).inflate(R.layout.image_item, view, false);

        final ImageView imageView = imageLayout.findViewById(R.id.itemImage);

        GlideApp.with(context)
                .load(urls.get(position))
                .into(imageView);

        Objects.requireNonNull(view).addView(imageLayout);
        return imageLayout;
    }
}
