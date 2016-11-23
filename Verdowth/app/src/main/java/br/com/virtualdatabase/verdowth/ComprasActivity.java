package br.com.virtualdatabase.verdowth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.virtualdatabase.verdowth.adapters.CompraAdapter;

/**
 * Created by marcoscesteves on 20/11/16.
 */

public class ComprasActivity extends Activity {

    ListView lista_de_compras;
    ArrayList<Compra> listaDeCompras = new ArrayList<>();
    Button btn_prosseguir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.compras);

        lista_de_compras = (ListView) findViewById(R.id.lista_de_compras);
        btn_prosseguir = (Button) findViewById(R.id.btn_prosseguir);

        /**
         * Objeto para testes (Início) :
         * Criando objeto compra apenas para testes. Este objeto virá da activity 'Percurso_principal'
          */

        final Compra compra1 = new Compra("Morango", 2, 3.1);
        Compra compra2 = new Compra("Tomate", 1, 7.0);
        Compra compra3 = new Compra("Pera", 1, 12.0);
        listaDeCompras.add(compra1);
        listaDeCompras.add(compra2);
        listaDeCompras.add(compra3);

        /**
         * Objeto para testes (Fim)
         */


        // Utilizando um custom adapter:
        CompraAdapter adapter = new CompraAdapter(this, R.layout.linha_lista_cesta_de_compras, listaDeCompras);
        lista_de_compras.setAdapter(adapter);

        //Adicionando um Header à ListView:

        View headerView =  ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.linha_inicial_cesta_de_compras, null, false);
        lista_de_compras.addHeaderView(headerView);


        //Adicionando Footers à ListView:

        View footerViewFrete =  ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.linha_frete_cesta_de_compras, null, false);

        TextView footer_valorDoFrete = (TextView) footerViewFrete.findViewById(R.id.linha_valorFrete);
        footer_valorDoFrete.setText("R$ "+calculaValorFrete().toString());

        lista_de_compras.addFooterView(footerViewFrete);

        View footerViewTotal =  ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.linha_final_cesta_de_compras, null, false);
        TextView footer_totalDaCompra = (TextView) footerViewTotal.findViewById(R.id.totalDaCompra);
        footer_totalDaCompra.setText("R$ "+calculaValorTotal().toString());

        lista_de_compras.addFooterView(footerViewTotal);

        /**
         * Clique nos Botões (Início)
         */

        btn_prosseguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bundle bundle = new Bundle();
                bundle.putSerializable("listaDeCompras", listaDeCompras);
                Intent intent = new Intent(this, Other.class);
                intent.putExtras(bundle);
                startActivity(intent);*/

                /* Para pegar os dados na Other.class, use (com as devidas substituições):
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                en = (EN)getIntent().getSerializableExtra("en"); //Obtaining data
                }
                 */
            }
        });


        /**
         * Clique nos Botões (Fim)
         */

        }

    /**
     *
     * @return - Devolve o valor calculado da compra (Frete + Somatório(Qtde*Valor))
     */
    private Double calculaValorTotal() {
        Double valorCalculado = 0.0;
        Double frete = calculaValorFrete();

        for (int i=0; i<listaDeCompras.size(); i++){
        valorCalculado += listaDeCompras.get(i).getUnitaryPrice()*listaDeCompras.get(i).getQuantity();
        }

        valorCalculado += frete;
        return valorCalculado;
    }

    /**
     *
     * @return - Retorna valor do frete baseado nas localizações.
     */
    private Double calculaValorFrete() {
        // Inicialmente este está como um valor fixo. Com o tempo, vamos torná-lo variável dependendo
        // das localizações
        return 10.0;
    }


}
