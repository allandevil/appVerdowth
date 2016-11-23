package br.com.virtualdatabase.verdowth._percurso;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;

import br.com.virtualdatabase.verdowth.Localidade;
import br.com.virtualdatabase.verdowth.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Percurso_principal extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback{

    private String TAG = "IGuia / Percurso";
    private static GoogleMap map;
    private SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    OkHttpClient client;
    public int contador=0;
    private Localidade[] arrayLocalidades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_percurso_principal);

            // Abrindo a Fragment de Mapas:
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Configurando o objeto GoogleApiClient:

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();


        // Conectando ao repositório e pegando informações da DB (Online)

        if (getNetworkClass(this) != "2G") {

            SecondThread secondThread = new SecondThread();
            secondThread.execute();


        } else {
            Toast.makeText(this, "Prezado usuário, sua conexão não está adequada! Favor tentar mais tarde.", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
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

        if (getNetworkClass(this) != "2G"){
            // Conectar com o Google Play Services:
            mGoogleApiClient.connect();

        }
    }

    @Override
    protected void onStop() {
        if (getNetworkClass(this) != "2G"){
        // Desconecta do Google Play Services:
        mGoogleApiClient.disconnect();

        }

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
                    .snippet("Preco: R$ "+localidade.getPreco())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.planta));

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



