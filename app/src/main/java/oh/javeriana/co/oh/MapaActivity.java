package oh.javeriana.co.oh;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static oh.javeriana.co.oh.tools.requestPermission;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {

    Huesped huesped = null;
    Anfitrion anfitrion = null;

    private GoogleMap mMap;
    public final Calendar c = Calendar.getInstance();
    private final int mes = c.get(Calendar.MONTH);
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int anio = c.get(Calendar.YEAR);
    private double latitudUsuario;
    private double longitudUsuario;
    public static final String PATH_ALOJAMIENTOS = "alojamientos/";

    private final static int LOCATION_PERMISSION = 0;
    private static final double RADIUS_OF_EARTH_KM = 6371;
    private static final double ARRIBADERLAT = 4.792509;
    private static final double ARRIBADERLONG = -73.909356;
    private static final double ABAJOIZQLAT = 4.548875;
    private static final double ABAJOIZQLONG = -74.271749;

    private static int voy;

    ImageButton botonFechaInicial;
    ImageButton botonFechaFinal;
    TextView fechaInicial;
    TextView fechaFinal;
    FloatingActionButton botonBuscar;
    FloatingActionButton botonPosActual;
    EditText textDir;

    Marker oldmark;
    Marker newmark;
    Marker lastmark;

    EditText mAddress;
    TextView distance;

    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    //LocationCallback mLocationCallback;
    LocationCallback mLocationCallback2;
    //private LocationResult locationResult2;
    List<String> listalocations;
    JSONObject jso;

    FirebaseDatabase database;
    DatabaseReference myRef;

    ArrayList<Pair<String, Alojamiento>> listaAlojamientos = new ArrayList<>();
    ArrayList<Pair<String, Negocio>> listaNegocios = new ArrayList<>();


    LatLng userLatLng;
    Map<Marker, Pair<String, Alojamiento>> markers = new HashMap<>();
    Map<Marker, Pair<String, Negocio>> markersNeg = new HashMap<>();

    String rol = "";
    String idUsr;
    boolean isSearch = false;
    boolean goCurrent = false;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(PATH_ALOJAMIENTOS);
        rol = getIntent().getSerializableExtra("usr").getClass().getName();
        idUsr = (String) getIntent().getExtras().getString("idUsr");

        if (rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") == 0) {
            huesped = (Huesped) getIntent().getSerializableExtra("usr");
        } else if (rol.compareToIgnoreCase("oh.javeriana.co.oh.Anfitrion") == 0) {
            anfitrion = (Anfitrion) getIntent().getSerializableExtra("usr");
        }

        botonFechaInicial = findViewById(R.id.botonFechaInicial);
        botonFechaFinal = findViewById(R.id.botonFechaFinal);
        fechaInicial = findViewById(R.id.fechaInicial);
        fechaFinal = findViewById(R.id.fechaFinal);
        botonBuscar = findViewById(R.id.buscarDir);
        textDir = findViewById(R.id.textDireccion);
        botonPosActual = findViewById(R.id.btnActualPos);

        String explanation = "Necesitamos acceder a tu ubicación para ubicarte en el mapa";
        tools.requestPermission(MapaActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, explanation, LOCATION_PERMISSION);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        botonFechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSearch = true;
                obtenerFecha(1);
            }
        });

        botonFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSearch = true;
                obtenerFecha(2);
            }
        });

        botonPosActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSearch = false;
                goCurrent = true;
                loadSites();

                //loadSites();
            }
        });

        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textDir.getVisibility() == View.INVISIBLE)
                    textDir.setVisibility(View.VISIBLE);
                else
                    textDir.setVisibility(View.INVISIBLE);
            }
        });

        textDir.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    if(actionId == EditorInfo.IME_ACTION_DONE) {
                        isSearch = true;
                        Geocoder mGeocoder = new Geocoder(getBaseContext());
                        List<Address> addresses = mGeocoder.getFromLocationName(textDir.getText().toString(), 2, ABAJOIZQLAT, ABAJOIZQLONG, ARRIBADERLAT, ARRIBADERLONG);
                        if (addresses != null && !addresses.isEmpty()) {

                            Address addressResult = addresses.get(0);
                            userLatLng = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                            latitudUsuario = userLatLng.latitude;
                            longitudUsuario = userLatLng.longitude;
                            loadSites();
                            mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
                        }

                        textDir.setVisibility(View.INVISIBLE);
                        textDir.setText("");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        String rol = getIntent().getSerializableExtra("usr").getClass().getName();
        Log.i("ROL", rol);
        if (rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") == 0) {
            huesped = (Huesped) getIntent().getSerializableExtra("usr");
        } else if (rol.compareToIgnoreCase("oh.javeriana.co.oh.Anfitrion") == 0) {
            anfitrion = (Anfitrion) getIntent().getSerializableExtra("usr");
        }

        mLocationRequest = createLocationRequest();

        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION,
                "Se necesita acceder a la ubicacion", LOCATION_PERMISSION);

        LocationSettingsRequest.Builder builder = new
                LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        /*task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates(); //Todas las condiciones para recibir localizaciones
            }
        });*/

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                        try {// Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(MapaActivity.this, LOCATION_PERMISSION);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. No way to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });

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

                    mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));


                }
            }
        });

        loadSites();

        /*mLocationCallback = new LocationCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                Location lastLocation = locationResult.getLastLocation();
                lastLocation.setLatitude(location.getLatitude() + 0.0005);
                lastLocation.setLongitude(location.getLongitude() + 0.0005);
                Log.i("LOCATION1", "Location update in the callback: " + location);
                Log.i("LOCATION2", "Location update in the callback: " + lastLocation);

                if (location != null && mMap != null) {
                    if(location.getLatitude() > lastLocation.getLongitude()+0.0005 || location.getLatitude() < lastLocation.getLongitude()-0.0005) {
                        if(location.getLongitude() > lastLocation.getLongitude()+0.0005 || location.getLongitude() < lastLocation.getLongitude()-0.0005) {
                            mMap.clear();
                            if (voy == 0) {
                                LatLng user = new LatLng(location.getLatitude(), location.getLongitude());
                                latitudUsuario = location.getLatitude();
                                longitudUsuario = location.getLongitude();
                                Log.i("LatitudDelUsuario", String.valueOf(latitudUsuario));
                                Log.i("LongitudDelUsuario", String.valueOf(longitudUsuario));
                                oldmark = mMap.addMarker(new MarkerOptions().position(user).title("Ubicación actual"));
                                newmark = oldmark;

                                mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(user));
                                voy++;
                            } else {
                                LatLng user = new LatLng(location.getLatitude(), location.getLongitude());
                                if (oldmark != null)
                                    oldmark.remove();
                                newmark = mMap.addMarker(new MarkerOptions().position(user).title("Usted"));
                                oldmark = newmark;
                                mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(user));
                            }
                        }
                    }
                }

                //listaAlojamientos = loadSites();
                paintMarker();


                for(Alojamiento a: listaAlojamientos) {
                    if (distancepoint(a.getLatitud(), a.getLongitud(), latitudUsuario, longitudUsuario) <= 2) {
                        try {
                            Log.i("FECHAS","HOLAAAAAA");
                            Log.i("FECHAS",String.valueOf(fechaInicial.getText().toString().isEmpty()));
                            if (fechaInicial.getText().toString().compareToIgnoreCase("dd/mm/yyyy") != 0 && fechaFinal.getText().toString().compareToIgnoreCase("dd/mm/yyyy") != 0) {
                                Log.i("FECHAS","HOLAAAAAA222222222");
                                if(String.valueOf(fechaInicial.getText().toString().isEmpty()).equals("true") && String.valueOf(fechaFinal.getText().toString().isEmpty()).equals("true")){
                                    Log.i("FECHAS","HOLAAAA 333333");
                                    LatLng loc = new LatLng(a.getLatitud(), a.getLongitud());
                                    Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title("Alojamiento"));
                                    markers.put(marker, a);
                                }

                                else if (a.getFechaInicialDate().compareTo(tools.getFechaDate(fechaInicial.getText().toString())) <= 0 && a.getFechaFinalDate().compareTo(tools.getFechaDate(fechaFinal.getText().toString())) >= 0) {
                                    LatLng loc = new LatLng(a.getLatitud(), a.getLongitud());
                                    Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title("Alojamiento"));
                                    markers.put(marker, a);
                                }

                                else{
                                    Log.i("FECHAS","HOLAAAA 44444444");
                                }
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };*/

    }

    @SuppressLint("MissingPermission")
    public void clearMap() {
        mMap.clear();

        if(!isSearch) {
            String explanation = "Necesitamos acceder a tu ubicación para ubicarte en el mapa";
            tools.requestPermission(MapaActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, explanation, LOCATION_PERMISSION);
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

                        if(goCurrent) {
                            mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
                            goCurrent = false;
                        }
                        paintMarkerSites();
                    /*mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));*/
                    }
                }
            });
        }
        else {
            mMap.addMarker(new MarkerOptions().position(userLatLng).title("Tu ubicación"));
            isSearch = false;
            paintMarkerSites();
        }

    }

    public void paintMarkerSites() {
        Log.i("CURRENT", String.valueOf(latitudUsuario) + " - " + String.valueOf(longitudUsuario));
        Log.i("LISTA", String.valueOf(listaAlojamientos.size()));


        for(Pair p: listaAlojamientos) {

            Alojamiento a = (Alojamiento) p.second;

            LatLng loc = new LatLng(a.getLatitud(), a.getLongitud());

           if (distancepoint(a.getLatitud(), a.getLongitud(), latitudUsuario, longitudUsuario) <= 2) {
                try {
                    Log.i("FECHAS","HOLAAAAAA");
                    Log.i("FECHAS",String.valueOf(fechaInicial.getText().toString().isEmpty()));
                    if (fechaInicial.getText().toString().compareToIgnoreCase("") != 0 && fechaFinal.getText().toString().compareToIgnoreCase("") != 0) {
                        if (fechaInicial.getText().toString().compareToIgnoreCase("dd/mm/yyyy") != 0 && fechaFinal.getText().toString().compareToIgnoreCase("dd/mm/yyyy") != 0) {
                            Log.i("FECHAS", "HOLAAAAAA222222222");
                            /*if (String.valueOf(fechaInicial.getText().toString().isEmpty()).equals("true") && String.valueOf(fechaFinal.getText().toString().isEmpty()).equals("true")) {
                                Log.i("FECHAS", "HOLAAAA 333333");
                                markers.put(marker, p);
                            } else */
                            Log.i("FECHAS", fechaInicial.getText().toString() + " --- " + fechaFinal.getText().toString());
                            Log.i("FECHAS", fechaInicial.getText().toString() + " vs " + a.getFechaInicial() + " --- " + fechaFinal.getText().toString() + " vs " + a.getFechaFinal());
                            Log.i("FECHAS", String.valueOf(a.getFechaInicialDate().compareTo(tools.getFechaDate(fechaInicial.getText().toString())) <= 0 && a.getFechaFinalDate().compareTo(tools.getFechaDate(fechaFinal.getText().toString())) >= 0));
                            if (a.getFechaInicialDate().compareTo(tools.getFechaDate(fechaInicial.getText().toString())) <= 0 &&
                                    a.getFechaFinalDate().compareTo(tools.getFechaDate(fechaFinal.getText().toString())) >= 0 &&
                                    tools.getFechaDate(fechaInicial.getText().toString()).compareTo((tools.getFechaDate(fechaFinal.getText().toString()))) <= 0) {

                                Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title("Alojamiento"));
                                markers.put(marker, p);

                            } else if (tools.getFechaDate(fechaInicial.getText().toString()).compareTo((tools.getFechaDate(fechaFinal.getText().toString()))) > 0){

                                Toast.makeText(this, "La fecha final es menor a la inicial", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Log.i("MARRKER", "AGREGADO");
                            Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title("Alojamiento"));
                            markers.put(marker, p);
                        }
                    }else {
                        Log.i("MARRKER", "AGREGADO");
                        Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title("Alojamiento"));
                        markers.put(marker, p);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        paintMarkerNegocios();

    }

    public void paintMarkerNegocios() {
        Log.i("CURRENT", String.valueOf(latitudUsuario) + " - " + String.valueOf(longitudUsuario));
        Log.i("LISTA", String.valueOf(listaNegocios.size()));

        for(Pair p: listaNegocios) {

            Negocio n = (Negocio) p.second;

            LatLng loc = new LatLng(n.getLatitud(), n.getLongitud());

            //if (distancepoint(n.getLatitud(), n.getLongitud(), latitudUsuario, longitudUsuario) <= 2) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title("Negocio").icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    markersNeg.put(marker, p);
            //}
        }
    }


    public ArrayList<Pair<String, Negocio>> loadNegocios() {

        listaNegocios.clear();
        myRef = database.getReference("negocios");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("la lista", String.valueOf(dataSnapshot.getChildrenCount()));
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    //usr = singleSnapshot.getValue(Huesped.class);
                    double latitud = Double.parseDouble(singleSnapshot.child("latitud").getValue().toString());
                    double longitud = Double.parseDouble(singleSnapshot.child("longitud").getValue().toString());
                    String nombre =  singleSnapshot.child("nombre").getValue().toString();
                    Negocio negocio = singleSnapshot.getValue(Negocio.class);

                    listaNegocios.add(new Pair<>(singleSnapshot.getKey(), negocio));
                    Log.i("Added", singleSnapshot.getKey() + singleSnapshot.getValue().toString());
                }

                clearMap();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ERROR", "error en la consulta", databaseError.toException());
            }
        });

        return(listaNegocios);
    }


    public ArrayList<Pair<String, Alojamiento>> loadSites() {

        listaAlojamientos.clear();
        myRef = database.getReference("alojamientos");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("la lista", String.valueOf(dataSnapshot.getChildrenCount()));
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    //usr = singleSnapshot.getValue(Huesped.class);
                    double latitud = Double.parseDouble(singleSnapshot.child("latitud").getValue().toString());
                    double longitud = Double.parseDouble(singleSnapshot.child("longitud").getValue().toString());
                    String nombre =  singleSnapshot.child("nombre").getValue().toString();
                    Alojamiento alojamiento = singleSnapshot.getValue(Alojamiento.class);

                    listaAlojamientos.add(new Pair<>(singleSnapshot.getKey(), alojamiento));
                    Log.i("Added", singleSnapshot.getKey() + singleSnapshot.getValue().toString());
                }

             //   clearMap();
                loadNegocios();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ERROR", "error en la consulta", databaseError.toException());
            }
        });

        return(listaAlojamientos);
    }

    public double distancepoint(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;

        return Math.round(result * 100.0) / 100.0;

    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000); //tasa de refresco en milisegundos
        mLocationRequest.setFastestInterval(5000); //máxima tasa de refresco
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOCATION_PERMISSION: {
                if (resultCode == RESULT_OK) {
                    //startLocationUpdates(); //Se encendió la localización!!!
                } else {
                    Toast.makeText(this,
                            "Sin acceso a localización, hardware deshabilitado!",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    /*private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //startLocationUpdates();
        //paintMarker();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //startLocationUpdates();
        //paintMarker();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }


    public void obtenerFecha(final int codigo) {

        DatePickerDialog.OnDateSetListener dateList = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);
                if (codigo == 1)
                    fechaInicial.setText(diaFormateado + "/" + mesFormateado + "/" + year);
                else
                    fechaFinal.setText(diaFormateado + "/" + mesFormateado + "/" + year);

                loadSites();
            }

        };

        DatePickerDialog recogerFecha = new DatePickerDialog(this, dateList, anio, mes, dia);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recogerFecha.setOnDateSetListener(dateList);
        }

        recogerFecha.show();
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

        LatLng bogota2 = new LatLng(4.397908 , -74.076066);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bogota2));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if(!marker.getPosition().equals(userLatLng)) {
                    Bundle bundle = new Bundle();
                    if (huesped != null)
                        bundle.putSerializable("usr", huesped);
                    else if (anfitrion != null)
                        bundle.putSerializable("usr", anfitrion);

                    bundle.putString("idUsr", idUsr);

                    Log.i("MARKERS", String.valueOf(markers.size()));

                    if(markers.containsKey(marker)) {
                        bundle.putString("idAloj", markers.get(marker).first);
                        bundle.putSerializable("alojamiento", markers.get(marker).second);
                        Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                    else if(markersNeg.containsKey(marker)) {
                        bundle.putString("idNeg", markersNeg.get(marker).first);
                        bundle.putSerializable("negocio", markersNeg.get(marker).second);
                        Intent intent = new Intent(getApplicationContext(), NegocioActivity.class);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }



                }
                return false;

            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            Bundle bundle = new Bundle();

            bundle.putString("idUsr", idUsr);

            if(anfitrion != null)
                bundle.putSerializable("usr", anfitrion);
            else if (huesped != null)
                bundle.putSerializable("usr", huesped);

            switch (item.getItemId()) {
                case R.id.navigationExplore:
                    return true;
                case R.id.navigationRecord:
                    intent = new Intent(getApplicationContext(),HistorialActivity.class);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    return true;
                case R.id.navigationProfile:
                    intent = new Intent(getApplicationContext(),PerfilActivity.class);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };


}
