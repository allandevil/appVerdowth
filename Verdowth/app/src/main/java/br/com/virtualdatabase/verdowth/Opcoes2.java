package br.com.virtualdatabase.verdowth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created on 06/11/16.
 */
public class Opcoes2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opcoes2);
    }

    public void onClickTenho(View v){
        Intent intent = new Intent(this,TenhoTerra.class);
        startActivity(intent);

    }

    public void onClickTrabalho(View v){
        Intent intent = new Intent(this,TenhoTrabalho.class);
        startActivity(intent);

    }
}
