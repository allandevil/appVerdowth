package br.com.virtualdatabase.verdowth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import br.com.virtualdatabase.verdowth._percurso.Percurso_principal;
import br.com.virtualdatabase.verdowth.visao_agricultor.MainActivity_VisaoDoAgricultor;

/**
 * Created on 06/11/16.
 */

public class MainActivity extends Activity {
    //ImageButton btn_plantar;
    LinearLayout plantar_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_start);
        plantar_layout = (LinearLayout) findViewById(R.id.layout_plantar);

      //  btn_plantar = (ImageButton) findViewById(R.id.plantar_btn);


    }

    public void onClickPlantar(View v) {

        Intent intent = new Intent(MainActivity.this, MainActivity_VisaoDoAgricultor.class);
        startActivity(intent);

    }

    public void onClickComprar(View c){
        Intent intent = new Intent(MainActivity.this, Percurso_principal.class);
        startActivity(intent);
    }

    // Testeeeeeeeeeeeeeeeeeeeeeeeeeeeeee




}
