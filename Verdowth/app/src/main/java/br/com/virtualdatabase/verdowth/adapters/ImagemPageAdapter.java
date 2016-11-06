package br.com.virtualdatabase.verdowth.adapters;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import br.com.virtualdatabase.verdowth.R;


/**
 * Created by marcoscesteves on 27/04/16.
 */
public class ImagemPageAdapter extends PagerAdapter {

    private Context context;
    private final int[] imagens;
    View view;

    public ImagemPageAdapter(Context c, int[] imagens) {
        this.context = c;
        this.imagens = imagens;
    }


    @Override
    public int getCount() {
        return imagens != null ? imagens.length : 0;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        view = LayoutInflater.from(this.context).inflate(R.layout.adapter_imagem,container,false);
        ImageView img = (ImageView) view.findViewById(R.id.imageView_adapter_viewPager);
        img.setImageResource(imagens[position]);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(view);
    }
}



