package br.com.virtualdatabase.verdowth._percurso;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;

import br.com.virtualdatabase.verdowth.ComprasActivity;
import br.com.virtualdatabase.verdowth.Localidade;
import br.com.virtualdatabase.verdowth.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Percurso_principal extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback{

    private String TAG = "Verdowth";
    private static GoogleMap map;
    private SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    private OkHttpClient client;
    private EditText edtTextBuscaPorLocalidade;
    private Localidade[] arrayLocalidades;
    private FloatingActionButton fab_buscaPorEndereco;
    private FloatingActionMenu fam_opcoes;
    private boolean isVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_percurso_principal);

        edtTextBuscaPorLocalidade = (EditText) findViewById(R.id.editTextBusca);
        edtTextBuscaPorLocalidade.setVisibility(View.INVISIBLE);
        fab_buscaPorEndereco = (FloatingActionButton) findViewById(R.id.item_fab_menu_endereco);
        fam_opcoes = (FloatingActionMenu) findViewById(R.id.fab_menu);



            // Abrindo a Fragment de Mapas:
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Configurando o objeto GoogleApiClient:

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();


        // Conectando ao repositório e pegando informações da DB (Online)

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWiFi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWiFi.isConnected() || getNetworkClass(this) != "2G"){
            SecondThread secondThread = new SecondThread();
            secondThread.execute();
        } else {
            Toast.makeText(this, "Prezado usuário, sua conexão não está adequada! Favor tentar mais tarde.", Toast.LENGTH_SHORT).show();
            finish();
        }



        /**
         * Adicionando ações ao FLoattingActionMenu (FAM) e ao FloatingActionButton (FAB)
         */

        /*fab_buscaPorEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibilityEdtText();
            }
        });*/

        fab_buscaPorEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibilityEdtText();
            }
        });

        fab_buscaPorEndereco.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Percurso_principal.this, ComprasActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });

        fam_opcoes.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (!opened && edtTextBuscaPorLocalidade.getVisibility() == View.VISIBLE ){
                    toggleVisibilityEdtText();
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     *
     */
    public void toggleVisibilityEdtText(){
        // Condição para ver se o campo de busca está visível ou não.
        isVisible = edtTextBuscaPorLocalidade.getVisibility() == View.VISIBLE;
        edtTextBuscaPorLocalidade.setVisibility(isVisible ? View.INVISIBLE : View.VISIBLE);
    }


    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Toast.makeText(Percurso_principal.this, "Minha coordenada atual Latitude: "
                    +String.valueOf(mLastLocation.getLatitude())
                    +" Longitude "+
                    String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(Percurso_principal.this, "Conectado ao Google Play Services", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(Percurso_principal.this, "Conexão intererrompida!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(Percurso_principal.this, "Erro ao conectar: "+ connectionResult, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG , "onMapReady: "+ map );

        this.map = googleMap;
        //configura o tipo de mapa:
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    protected void onStart(){
        super.onStart();

        // Testar conexão do usuário


            // Conectar com o Google Play Services:
            mGoogleApiClient.connect();


    }

    @Override
    protected void onStop() {
        super.onStop();
        // Desconecta do Google Play Services:
        mGoogleApiClient.disconnect();



    }

    /**
     * This method set in MAP the locations
     * @param local1
     * @param local2
     */
    public void setLocationInMapIntent(String local1, String local2){
        String url = "http://maps.google.com/maps?f=d&saddr="+local1+"&daddr="+local2+"&hl=pt";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    /**
     * Método que adiciona um Marcador no mapa
     * @param localidade -  Dados geográficos de determinada localidade (Lontitude e Latitude LatLng)
     */
    public void adicionarMarcador(Localidade localidade){
        MarkerOptions markerOptions = new MarkerOptions();

        Marker marker;

        markerOptions.position(localidade.getCoordenadas()).title(localidade.getProduto())
                .snippet("Preco: R$ "+localidade.getPreco());



        switch (localidade.getProduto()){


            case "Morango":
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.morango_48));
                break;

            case "Tomate":
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.tomato_48));
                break;

            case "Pera":
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pear_48));
                break;

            default:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.planta));
                break;
            }

            marker = map.addMarker(markerOptions);

            //contador++;


        // Fazendo com que a camera tenha uma posição boa a ser exibida:

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(localidade.getCoordenadas(), 16);
        map.moveCamera(update);


    }

    /**
    * Método para testar a conexão do usuário */
    public static String getNetworkClass(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "Unknown";
        }
    }


    class SecondThread extends AsyncTask<String, String, String> {

        private OkHttpClient client = new OkHttpClient();
        private String saida;


        @Override
        protected String doInBackground(String... params) {
            try {
                Request request = new Request.Builder()
                        .url("http://appverdowth.mybluemix.net/Myloc").build();
                       // .url("http://www.virtualdatabase.com.br/db_info/plantas_db/plantas_db.php").build();

                Response response = client.newCall(request).execute();
                String json = response.body().string();
                saida = json;
                //Log.e("saida", saida);
                // Pegando o GSON e transformando em objetos:
                Localidade localidade1;
                Gson gson = new Gson();
                arrayLocalidades = gson.fromJson(json, Localidade[].class);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return saida.toString();
        }

        @Override
        protected void onPostExecute(String s) {


            // Incluindo localidades no Mapa:

            try {
                for (Localidade local : arrayLocalidades) {
                    //Log.e("JSON", "" + local.getLatitude());
                    adicionarMarcador(local);
                }

            } catch (Exception e){}


        }
    }

}



