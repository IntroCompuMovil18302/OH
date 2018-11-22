package oh.javeriana.co.oh;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class RutearActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double goalLat;
    private double goalLong;
    private Marker usrMark;
    private Marker goalMark;
    private double latitudUsuario;
    private double longitudUsuario;
    private JSONObject jso;


    private final static int LOCATION_PERMISSION = 0;

    LatLng goal;
    LatLng userLatLng;
    FusedLocationProviderClient mFusedLocationClient;



    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutear);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String explanation = "Necesitamos acceder a tu ubicación para ubicarte en el mapa";
        tools.requestPermission(RutearActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, explanation, LOCATION_PERMISSION);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.i("LOCATION", "onSuccess location");
                if (location != null) {
                    Log.i(" LOCATION ",
                            "Longitud: " + location.getLongitude());
                    Log.i(" LOCATION ", "Latitud: " + location.getLatitude());

                    latitudUsuario = location.getLatitude();
                    longitudUsuario = location.getLongitude();

                    userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(userLatLng)
                            .title("Tu ubicación"));


                    usrMark = mMap.addMarker(new MarkerOptions().position(userLatLng).title("Tu ubicación"));
                    goalMark = mMap.addMarker(new MarkerOptions().position(goal).title("Alojamiento"));

                    String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+usrMark.getPosition().latitude+","+usrMark.getPosition().longitude+"&destination="+goalMark.getPosition().latitude+","+goalMark.getPosition().longitude+ "&key=" + "AIzaSyBhHTgsUynwhanscYcaDWNTpGQSbdZiAhI";
                    Log.i("URL:", url);
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i("LOCATION", response);
                                jso = new JSONObject(response);
                                trazarRuta(jso);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queue.add(stringRequest);


                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));


                }
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        goalLat = getIntent().getExtras().getDouble("latitud");
        goalLong = getIntent().getExtras().getDouble("longitud");
        goal = new LatLng(goalLat, goalLong);


        // Add a marker in Sydney and move the camera
    }

    private void trazarRuta(JSONObject jso) {

        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {
            jRoutes = jso.getJSONArray("routes");
            for (int i=0; i<jRoutes.length();i++){
                jLegs = ((JSONObject)(jRoutes.get(i))).getJSONArray("legs");

                for (int j=0; j<jLegs.length();j++){
                    jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");

                    for (int k = 0; k<jSteps.length();k++){
                        String polyline = ""+((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        Log.i("end",""+polyline);
                        List<LatLng> list = PolyUtil.decode(polyline);
                        mMap.addPolyline(new PolylineOptions().addAll(list).color(Color.BLUE).width(10));
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
