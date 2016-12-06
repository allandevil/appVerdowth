package br.com.virtualdatabase.verdowth.visao_agricultor;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import br.com.virtualdatabase.verdowth.R;
import br.com.virtualdatabase.verdowth.adapters.AgricultorPageAdapter;

/**
 * Created on 06/11/16.
 */
public class MainActivity_VisaoDoAgricultor extends FragmentActivity {

    private TextView txt_produzir;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_visao_do_agricultor);

        mPager = (ViewPager) findViewById(R.id.pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        PagerAdapter pagerAdapter = new AgricultorPageAdapter(fragmentManager);
        mPager.setAdapter(pagerAdapter);

//        txt_produzir = (TextView) findViewById(R.id.txt_vamos_produzir);
//
//        txt_produzir.setText(Html.fromHtml("Seja em sua <b>varanda</b>, <b>quintal</b> ou até uma <b>parede</b>. Para todos esses espaços, temos a solução ideal."));
    }






}
