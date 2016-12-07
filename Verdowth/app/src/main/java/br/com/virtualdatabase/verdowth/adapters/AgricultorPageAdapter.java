package br.com.virtualdatabase.verdowth.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.virtualdatabase.verdowth.visao_agricultor.DescricaoInicialAgricultorFragment;
import br.com.virtualdatabase.verdowth.visao_agricultor.SomosColaborativosAgricultorFragment;
import br.com.virtualdatabase.verdowth.visao_agricultor.SomosParceirosAgricultorFragment;
import br.com.virtualdatabase.verdowth.visao_agricultor.SomosPraticidadeAgricultorFragment;
import br.com.virtualdatabase.verdowth.visao_agricultor.SomosSaudeAgricultorFragment;

/**
 * Created by marcoscesteves on 01/12/16.
 */

public class AgricultorPageAdapter extends FragmentPagerAdapter {

    public AgricultorPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DescricaoInicialAgricultorFragment();
            case 1:
                return new SomosSaudeAgricultorFragment();
            case 2:
                return new SomosPraticidadeAgricultorFragment();
            case 3:
                return new SomosColaborativosAgricultorFragment();
            case 4:
                return new SomosParceirosAgricultorFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
