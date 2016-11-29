package br.com.virtualdatabase.verdowth._percurso;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (servicesOK()) {
            setContentView(R.layout.content_percurso_principal);

            if (initMap()) {

                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API).build();
                mGoogleApiClient.connect();
            } else {
                Toast.makeText(this, "Map not connected!", Toast.LENGTH_SHORT).show();
            }
        } else {
            setContentView(R.layout.activity_main);
        }

        edtTextBuscaPorLocalidade = (EditText) findViewById(R.id.editTextBusca);
        edtTextBuscaPorLocalidade.setVisibility(View.INVISIBLE);
        fab_buscaPorEndereco = (FloatingActionButton) findViewById(R.id.item_fab_menu_endereco);
        fam_opcoes = (FloatingActionMenu) findViewById(R.id.fab_menu);

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
        this.map = googleMap;

    }

    protected void onStart(){
        super.onStart();

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
                .snippet("#"+localidade.getProduto()+
                        "#"+localidade.getPreco()+"#");

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

        /*CameraUpdate update = CameraUpdateFactory.newLatLngZoom(localidade.getCoordenadas(), 16);
        map.moveCamera(update);*/

        mostraLocalizacaoAtual();


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

    public View showInfoWindow(Marker marker){
        View v = getLayoutInflater().inflate(R.layout.map_info_window, null);
        ImageView ivProduto = (ImageView)v.findViewById(R.id.ivProduto);
        TextView txNome = (TextView)v.findViewById(R.id.tvNome);
        TextView txQuantidade = (TextView)v.findViewById(R.id.tvQuantidade);
        TextView txPreco = (TextView)v.findViewById(R.id.tvPreco);


        Localidade loc = recuperaSnippet(marker.getSnippet());

        txNome.setText(loc.getProduto());
        txQuantidade.setText("3");
        txPreco.setText(loc.getPreco().toString());

        ivProduto.setImageResource(setImagemFruta(loc.getProduto()));

        return v;

    }

    public int setImagemFruta(String nomeFruta){
        int drawableID = R.drawable.planta;

        switch (nomeFruta){
            case "Morango":
                drawableID = R.drawable.morango_48;
                break;

            case "Tomate":
                drawableID = R.drawable.tomato_48;
                break;

            case "Pera":
                drawableID = R.drawable.pear_48;
                break;
        }

        return drawableID;
    }

    /**
     *
     * @param markerSnippet
     * @return
     */
    public Localidade recuperaSnippet(String markerSnippet){

        String[] parts = markerSnippet.split("#");
        String nomeFruta = parts[1].trim();
        String precoFruta = parts[2].trim();

        Localidade localidadeSnippet = new Localidade(-23.548689,-46.634301,nomeFruta,Double.parseDouble(precoFruta));

        return localidadeSnippet;
    }

    /**
     * Verifica conexão com Google Play Services
     * @return
     */
    public boolean servicesOK(){
        int isAvaliable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if(isAvaliable == ConnectionResult.SUCCESS){
            return true;
        } else if(GooglePlayServicesUtil.isUserRecoverableError(isAvaliable)){
            Dialog dialog =
                    GooglePlayServicesUtil.getErrorDialog(isAvaliable,this,ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to mapping service",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * Faz carregamento do Mapa
     * @return
     */
    private boolean initMap(){
        if (map == null){
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            map = mapFragment.getMap();


            if(map != null){

                //configura o tipo de mapa:
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                //faz aparecer infoWindow:
                map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View infoWindow = showInfoWindow(marker);
                        return infoWindow;
                    }
                });

                //habilita o click no infoWindow
                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        Intent intent = new Intent(Percurso_principal.this, ComprasActivity.class);
                        intent.putExtra("compraSelecionada",recuperaSnippet(marker.getSnippet()));
                        startActivity(intent);

                    }
                });


            }
        }
        return(map != null);

    }

    /**
     * mostra localização atual
     */
    public  void mostraLocalizacaoAtual(){
        Location currentLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if(currentLocation == null){
            Toast.makeText(this, "Culdn't connect!",Toast.LENGTH_SHORT).show();
        } else {
            LatLng latLng = new LatLng(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude()
            );
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                    latLng, 15
            );
            map.moveCamera(update);

        }

    }

}



