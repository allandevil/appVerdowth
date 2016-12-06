package br.com.virtualdatabase.verdowth;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

import br.com.virtualdatabase.verdowth._percurso.Percurso_principal;
import br.com.virtualdatabase.verdowth.adapters.CompraAdapter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by marcoscesteves on 20/11/16.
 */

public class ComprasActivity extends Activity {

    private ListView lista_de_compras;
    private ArrayList<Compra> listaDeCompras = new ArrayList<>();
    private Button btn_prosseguir;
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.compras);


        Localidade loc = (Localidade) this.getIntent().getSerializableExtra("compraSelecionada");
        Toast.makeText(ComprasActivity.this, "getSerializableExtra: "+loc.getProduto()+" e "
                +loc.getPreco(), Toast.LENGTH_SHORT).show();


        lista_de_compras = (ListView) findViewById(R.id.lista_de_compras);
        btn_prosseguir = (Button) findViewById(R.id.btn_prosseguir);

        /**
         * Objeto para testes (Início) :
         * Criando objeto compra apenas para testes. Este objeto virá da activity 'Percurso_principal'
         */

        LatLng fornecedor = new LatLng(-23.548689, -46.634301);
        LatLng cliente = new LatLng(-23.560369,-46.686511);

        final Compra compra1 = new Compra("Morango", 2, 3.1, fornecedor, cliente);
        //Compra compra2 = new Compra("Tomate", 1, 7.0);
        //Compra compra3 = new Compra("Pera", 1, 12.0);
        listaDeCompras.add(compra1);
        //listaDeCompras.add(compra2);
        //listaDeCompras.add(compra3);

        /**
         * Objeto para testes (Fim)
         */


        // Utilizando um custom adapter:
        CompraAdapter adapter = new CompraAdapter(this, R.layout.linha_lista_cesta_de_compras, listaDeCompras);
        lista_de_compras.setAdapter(adapter);

        //Adicionando um Header à ListView:

        View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.linha_inicial_cesta_de_compras, null, false);
        lista_de_compras.addHeaderView(headerView);


        //Adicionando Footers à ListView:

        View footerViewFrete = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.linha_frete_cesta_de_compras, null, false);

        TextView footer_valorDoFrete = (TextView) footerViewFrete.findViewById(R.id.linha_valorFrete);
        footer_valorDoFrete.setText("R$ " + calculaValorFrete().toString());

        lista_de_compras.addFooterView(footerViewFrete);

        View footerViewTotal = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.linha_final_cesta_de_compras, null, false);
        TextView footer_totalDaCompra = (TextView) footerViewTotal.findViewById(R.id.totalDaCompra);
        footer_totalDaCompra.setText("R$ " + calculaValorTotal().toString());

        lista_de_compras.addFooterView(footerViewTotal);

        /**
         * Clique nos Botões (Início)
         */

        btn_prosseguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Envio de transação ao WebService (Início):

                Gson gson = new Gson();
                json = gson.toJson(compra1);
                ThirdThread thirdThread = new ThirdThread();
                thirdThread.execute();




                // Envio de transação ao WebService (Fim).

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
     * @return - Devolve o valor calculado da compra (Frete + Somatório(Qtde*Valor))
     */
    private Double calculaValorTotal() {
        Double valorCalculado = 0.0;
        Double frete = calculaValorFrete();

        for (int i = 0; i < listaDeCompras.size(); i++) {
            valorCalculado += listaDeCompras.get(i).getUnitaryPrice() * listaDeCompras.get(i).getQuantity();
        }

        valorCalculado += frete;
        return valorCalculado;
    }

    /**
     * @return - Retorna valor do frete baseado nas localizações.
     */
    private Double calculaValorFrete() {
        // Inicialmente este está como um valor fixo. Com o tempo, vamos torná-lo variável dependendo
        // das localizações
        return 10.0;
    }

    class ThirdThread extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... teste) {

            Log.e("Teste de Json: ", json);


            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String saida = "padrão";

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url("http://appverdowth.mybluemix.net/cp")
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                saida = response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return saida;

        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(ComprasActivity.this, s, Toast.LENGTH_LONG).show();

        }
    }

    public void testeSusi(){

    }

}


