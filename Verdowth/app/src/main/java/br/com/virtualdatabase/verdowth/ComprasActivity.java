package br.com.virtualdatabase.verdowth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.virtualdatabase.verdowth.adapters.CompraAdapter;

/**
 * Created by marcoscesteves on 20/11/16.
 */

public class ComprasActivity extends Activity {

    ListView lista_de_compras;
    ArrayList<Compra> listaDeCompras = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.compras);

        lista_de_compras = (ListView) findViewById(R.id.lista_de_compras);

        // Criando objeto compra apenas para testes. Este objeto virá da activity 'Percurso_principal'
        Compra compra1 = new Compra("Morango", 2, 3.1);
        Compra compra2 = new Compra("Tomate", 1, 7.0);
        Compra compra3 = new Compra("Pera", 1, 12.0);
        listaDeCompras.add(compra1);
        listaDeCompras.add(compra2);
        listaDeCompras.add(compra3);

        // Utilizando um custom adapter:
        CompraAdapter adapter = new CompraAdapter(this, R.layout.linha_lista_cesta_de_compras, listaDeCompras);
        lista_de_compras.setAdapter(adapter);

        //Adicionando um Footer à ListView:

        View footerView =  ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.linha_final_dalista_cesta_de_compras, null, false);
        lista_de_compras.addFooterView(footerView);



    }
}
