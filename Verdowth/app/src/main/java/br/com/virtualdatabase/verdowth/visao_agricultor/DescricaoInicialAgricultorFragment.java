package br.com.virtualdatabase.verdowth.visao_agricultor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.virtualdatabase.verdowth.R;

/**
 * Created by marcoscesteves on 01/12/16.
 */

public class DescricaoInicialAgricultorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.visao_agricultor__descricao_inicial, container, false);

        //Todo: Lembrar de citar o site icons8.com

        return view;
    }
}
