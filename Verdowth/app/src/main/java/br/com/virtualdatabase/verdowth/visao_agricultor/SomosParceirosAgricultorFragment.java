package br.com.virtualdatabase.verdowth.visao_agricultor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.virtualdatabase.verdowth.R;

/**
 * Created by marcoscesteves on 01/12/16.
 */

public class SomosParceirosAgricultorFragment extends Fragment {

    Button goVerdowth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.visao_agricultor__somos_verdowth, container, false);
        goVerdowth = (Button) view.findViewById(R.id.goVerdowthBtn);
        //Todo: Lembrar de citar o site icons8.com

        goVerdowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SecondAcitivity_EscolhasPlantar.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
