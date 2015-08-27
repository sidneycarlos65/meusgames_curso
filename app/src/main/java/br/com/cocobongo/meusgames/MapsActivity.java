package br.com.cocobongo.meusgames;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.koushikdutta.async.future.FutureCallback;

import java.util.List;

import br.com.cocobongo.meusgames.api.MeusGamesAPI;
import br.com.cocobongo.meusgames.modelos.Usuario;

public class MapsActivity extends BaseActivity implements RoutingListener {

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private ProgressDialog progressDialog;
    private List<Usuario> usuarios;
    private boolean hasAddAmigos;
    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initToolbar();

        progressDialog = ProgressDialog.show(this, getString(R.string.app_name), "Aguarde...");
        getAmigos();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_maps);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsActivity.this.googleMap = googleMap;
                initGoogleMap(googleMap);
                addAmigosToMap();
            }
        });
    }

    private void getAmigos() {
        MeusGamesAPI meusGamesAPI = new MeusGamesAPI(this);
        meusGamesAPI.getAmigos(new FutureCallback<List<Usuario>>() {
            @Override
            public void onCompleted(Exception e, List<Usuario> result) {
                if (null != result) {
                    usuarios = result;
                    addAmigosToMap();
                } else {
                    Toast.makeText(MapsActivity.this, "Os amigos sumiram", Toast.LENGTH_SHORT)
                            .show();
                }

                if (null != progressDialog && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void addAmigosToMap(){
        if(null == googleMap || usuarios == null || hasAddAmigos){
            return;
        }

        hasAddAmigos = true;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (Usuario usuario: usuarios) {
            LatLng latLng = new LatLng(usuario.getLatitude(), usuario.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(usuario.getNome());
            markerOptions.snippet(usuario.getEndereco());
            markerOptions.position(latLng);

            googleMap.addMarker(markerOptions);

            builder.include(markerOptions.getPosition());
        }

        LatLngBounds bounds = builder.build();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        googleMap.moveCamera(cameraUpdate);
    }

    private void initGoogleMap(final GoogleMap googleMap){
        googleMap.setMyLocationEnabled(true);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng myLocation = new LatLng(googleMap.getMyLocation().getLatitude(),
                        googleMap.getMyLocation().getLongitude());
                criarRota(myLocation, marker.getPosition());
            }
        });
    }

    private void criarRota(LatLng myLocation, LatLng position) {
        Routing routing = new Routing.Builder().travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this).waypoints(myLocation, position).build();
        routing.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRoutingFailure() {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(PolylineOptions polylineOptions, Route route) {
        PolylineOptions polylineOptions1 = new PolylineOptions();
        polylineOptions1.color(Color.BLUE);
        polylineOptions1.width(10);
        polylineOptions1.addAll(polylineOptions.getPoints());

        if(polyline != null){
            polyline.remove();
        }

        polyline = googleMap.addPolyline(polylineOptions1);
    }

    @Override
    public void onRoutingCancelled() {

    }
}
