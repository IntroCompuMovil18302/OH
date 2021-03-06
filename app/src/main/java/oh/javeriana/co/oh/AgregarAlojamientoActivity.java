package oh.javeriana.co.oh;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.location.Geocoder;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgregarAlojamientoActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String PATH_ALOJAMIENTOS="alojamientos/";
    public static final String PATH_USERS="usuarios/";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private String tipoAlojamiento;
    private static final double ARRIBADERLAT = 4.792509;
    private static final double ARRIBADERLONG = -73.909356;
    private static final double ABAJOIZQLAT = 4.548875;
    private static final double ABAJOIZQLONG = -74.271749;
    private GoogleMap mMap;
    private Marker marker;

    private final int IMAGE_PICKER_REQUEST = 1;
    private final int REQUEST_IMAGE_CAPTURE = 2;


    EditText nombreET;
    EditText descripcionET;
    EditText precioET;
    EditText ubicacionET;
    Spinner spinnerTipo;
    ArrayAdapter<CharSequence>adapter;
    EditText cantHuespedesET;
    Button botonAgregar;
    ImageButton next;
    ImageButton next2;
    ImageButton prev;
    ImageButton prev2;
    ImageButton agrearFecha;
    TextView datosAlojamiento;
    TextView textHuespedes;
    TextView textTipo;
    TextView textPrecio;
    TextView textNombre;
    TextView textDescripcion;
    Uri imageUri[];
    ImageView fotos[];
    ImageView foto1;
    ImageView foto2;
    ImageView foto3;
    ImageView foto4;
    String idUsr;
    int contFoto;
    LinearLayout linearLayout;
    LinearLayout linerFechas;
    ConstraintLayout constraintLayout;
    TextView mTextView;
    ScrollView scroll;

    Double latMarcador;
    Double longMarcador;

    final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    List<String> listaFechas;

    Anfitrion anfitrion = null;
    String rol = "";
    String unaFecha="";


    public void obtenerFecha(final TextView fecha){
        //final String unaFecha;
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10)? "0" + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10)? "0" + String.valueOf(mesActual):String.valueOf(mesActual);

                fecha.setText(diaFormateado + "/" + mesFormateado + "/" + year);

                Log.i("Tagggggg222", fecha.getText().toString());
                unaFecha = fecha.getText().toString();
                listaFechas.add(fecha.getText().toString());





            }
        },anio, mes, dia);

        recogerFecha.show();
        // return fecha.setText(diaFormateado + "/" + mesFormateado + "/" + year);
        //Log.i("Tagggggg",unaFecha);
        // return unaFecha;
    }

    public String obtenerTodasFechas(){
        int i;
        String cadena = "";
        for(i=0; i<listaFechas.size();i++){

            cadena=cadena+listaFechas.get(i);
            if(i % 2 == 0){
                cadena=cadena+"-";
            }
            else {
                cadena=cadena+",";
            }

        }
        return  cadena;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_alojamiento);

        anfitrion = (Anfitrion) getIntent().getSerializableExtra("usr");
        idUsr = (String) getIntent().getExtras().getString("idUsr");

        Log.i("IdUsr", idUsr);


        database= FirebaseDatabase.getInstance();


        nombreET = findViewById(R.id.nombreET);
        descripcionET = findViewById(R.id.descripcionET);
        precioET = findViewById(R.id.precioET);
        ubicacionET = findViewById(R.id.ubicacionET);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        cantHuespedesET =  findViewById(R.id.cantHuespedesET);
        botonAgregar = findViewById(R.id.botonAgregar);
        next = findViewById(R.id.next);
        next2 = findViewById(R.id.next2);
        prev = findViewById(R.id.prev);
        prev2 = findViewById(R.id.prev2);
        adapter = ArrayAdapter.createFromResource(this, R.array.alojamientos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);
        datosAlojamiento = findViewById(R.id.datosAlojamiento);
        textHuespedes = findViewById(R.id.textHuespedes);
        textTipo = findViewById(R.id.textTipo);
        textPrecio = findViewById(R.id.textPrecio);
        textNombre = findViewById(R.id.textNombre);
        textDescripcion = findViewById(R.id.textDescripcion);
        linearLayout = findViewById(R.id.linearLayoutFotos);
        linerFechas = findViewById(R.id.linearFechas);
        constraintLayout = findViewById(R.id.constraintLayout);
        agrearFecha = findViewById(R.id.agregarFecha);
        mTextView = new TextView(getApplicationContext());
        scroll = findViewById(R.id.scroll);
        listaFechas = new ArrayList<>();

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        mapFragment.getView().setVisibility(View.GONE);


        imageUri = new Uri[4];
        fotos = new ImageView[4];
        fotos[0] = findViewById(R.id.foto1);
        fotos[1] = findViewById(R.id.foto2);
        fotos[2] = findViewById(R.id.foto3);
        fotos[3] = findViewById(R.id.foto4);
        contFoto = 0;

        rol = getIntent().getSerializableExtra("usr").getClass().getName();
        Log.i("ROL", rol);
        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Anfitrion") == 0) {
            anfitrion = (Anfitrion) getIntent().getSerializableExtra("usr");
        }

        for(int i=0; i<fotos.length; i++){
            fotos[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent pickImage = new Intent(Intent.ACTION_PICK);
                    pickImage.setType("image/*");
                    startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
                }
            });
        }

        /*foto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotoAux = foto1;
                Intent pickImage = new Intent(Intent.ACTION_PICK);
                pickImage.setType("image/*");
                startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
            }
        });
        foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotoAux = foto1;
                Intent pickImage = new Intent(Intent.ACTION_PICK);
                pickImage.setType("image/*");
                startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
            }
        });
        foto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotoAux = foto1;
                Intent pickImage = new Intent(Intent.ACTION_PICK);
                pickImage.setType("image/*");
                startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
            }
        });
        foto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotoAux = foto1;
                Intent pickImage = new Intent(Intent.ACTION_PICK);
                pickImage.setType("image/*");
                startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
            }
        });*/


        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoAlojamiento = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(0).setCheckable(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mStorageRef = FirebaseStorage.getInstance().getReference();
                myRef = database.getReference(PATH_ALOJAMIENTOS+"/"+idUsr);

                Log.i("eddefr",obtenerTodasFechas());
                if(!nombreET.getText().toString().isEmpty()) {
                    if(!tools.esNumero(String.valueOf(nombreET.getText()))){
                        if (!descripcionET.getText().toString().isEmpty()){
                            if (!tools.esNumero(String.valueOf(descripcionET.getText()))){
                                if (!precioET.getText().toString().isEmpty()){
                                    if(tools.esNumero(String.valueOf(precioET.getText()))){
                                        if (!ubicacionET.getText().toString().isEmpty()){
                                            if (!cantHuespedesET.getText().toString().isEmpty()){
                                                if (tools.esNumero(String.valueOf(cantHuespedesET.getText()))){
                                                    Geocoder mGeocoder = new Geocoder(getBaseContext());
                                                    String key = myRef.push().getKey();
                                                    myRef = database.getReference(PATH_ALOJAMIENTOS + key);
                                                    LatLng position=null;
                                                    try {
                                                        List<Address> addresses = mGeocoder.getFromLocationName(ubicacionET.getText().toString(), 2, ABAJOIZQLAT, ABAJOIZQLONG, ARRIBADERLAT, ARRIBADERLONG);
                                                        if (addresses != null && !addresses.isEmpty()) {

                                                            Address addressResult = addresses.get(0);
                                                            position = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                                                            Log.i("LATITUD", String.valueOf(position.latitude));
                                                            Log.i("LONGITUD", String.valueOf(position.longitude));
                                                        }

                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                    int cant = Integer.parseInt(cantHuespedesET.getText().toString());
                                                    double precio = Double.parseDouble( precioET.getText().toString());
                                                    double latitud = position.latitude;
                                                    double longitud = position.longitude;


                                                    Alojamiento alojamiento = new Alojamiento(nombreET.getText().toString(), descripcionET.getText().toString(), ubicacionET.getText().toString(),
                                                            cant, precio, idUsr, tipoAlojamiento, latitud, longitud, obtenerTodasFechas() );
                                                    myRef.setValue(alojamiento);

                                                    for(int i=0; i<fotos.length; i++){
                                                        if(imageUri[i] != null) {
                                                            StorageReference imagesProfile = mStorageRef.child(idUsr).child( key + "/image" + (i+1));
                                                            imagesProfile.putFile(imageUri[i]);
                                                        }
                                                    }

                                                    Toast.makeText(AgregarAlojamientoActivity.this, "Alojamiento creado exitosamente", Toast.LENGTH_SHORT).show();
                                                    AgregarAlojamientoActivity.this.finish();
                                                }else{
                                                    Toast.makeText(AgregarAlojamientoActivity.this, "La cantidad de huéspedes corresponde a un valor numérico", Toast.LENGTH_SHORT).show();
                                                }

                                            }else{
                                                Toast.makeText(AgregarAlojamientoActivity.this, "La cantidad de huéspedes no puede ser vacío", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(AgregarAlojamientoActivity.this, "La ubicación del alojamiento no puede ser vacío", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(AgregarAlojamientoActivity.this, "El precio del alojamiento corresponde a un valor numérico", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(AgregarAlojamientoActivity.this, "El precio del alojamiento no puede ser vacío", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(AgregarAlojamientoActivity.this, "La descripción del alojamiento no puede ser un valor numérico", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(AgregarAlojamientoActivity.this, "La descripción del alojamiento no puede ser vacía", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(AgregarAlojamientoActivity.this, "El nombre del alojamiento no puede ser un valor numérico", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(AgregarAlojamientoActivity.this, "El nombre del alojamiento no puede estar vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datosAlojamiento.setVisibility(View.GONE);
                textHuespedes.setVisibility(View.GONE);
                cantHuespedesET.setVisibility(View.GONE);
                textTipo.setVisibility(View.GONE);
                spinnerTipo.setVisibility(View.GONE);
                textPrecio.setVisibility(View.GONE);
                precioET.setVisibility(View.GONE);
                textNombre.setVisibility(View.GONE);
                nombreET.setVisibility(View.GONE);
                textDescripcion.setVisibility(View.GONE);
                descripcionET.setVisibility(View.GONE);
                botonAgregar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);

                prev.setVisibility(View.VISIBLE);
                next2.setVisibility(View.VISIBLE);
                // mapa.setVisibility(View.VISIBLE);
                // mMap.setVisibility(view.gone)
                next.setVisibility(View.GONE);
                //mapFragment.setVi;

                ubicacionET.setVisibility(View.VISIBLE);
                mapFragment.getView().setVisibility(View.VISIBLE);

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datosAlojamiento.setVisibility(View.VISIBLE);
                textHuespedes.setVisibility(View.VISIBLE);
                cantHuespedesET.setVisibility(View.VISIBLE);
                textTipo.setVisibility(View.VISIBLE);
                spinnerTipo.setVisibility(View.VISIBLE);
                textPrecio.setVisibility(View.VISIBLE);
                precioET.setVisibility(View.VISIBLE);
                textNombre.setVisibility(View.VISIBLE);
                nombreET.setVisibility(View.VISIBLE);
                textDescripcion.setVisibility(View.VISIBLE);
                descripcionET.setVisibility(View.VISIBLE);


                //botonAgregar.setVisibility(View.VISIBLE);
                //foto1.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);

                mapFragment.getView().setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                prev.setVisibility(View.GONE);
                next2.setVisibility(View.GONE);
                // mapa.setVisibility(View.GONE);
                prev2.setVisibility(View.GONE);

                ubicacionET.setVisibility(View.GONE);


            }
        });

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datosAlojamiento.setVisibility(View.GONE);
                textHuespedes.setVisibility(View.GONE);
                cantHuespedesET.setVisibility(View.GONE);
                textTipo.setVisibility(View.GONE);
                spinnerTipo.setVisibility(View.GONE);
                textPrecio.setVisibility(View.GONE);
                precioET.setVisibility(View.GONE);
                textNombre.setVisibility(View.GONE);
                nombreET.setVisibility(View.GONE);
                textDescripcion.setVisibility(View.GONE);
                descripcionET.setVisibility(View.GONE);



                botonAgregar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);

                mapFragment.getView().setVisibility(View.GONE);
                ubicacionET.setVisibility(View.GONE);
                prev.setVisibility(View.GONE);
                agrearFecha.setVisibility(View.VISIBLE);
                next2.setVisibility(View.GONE);
                prev2.setVisibility(View.VISIBLE);
                botonAgregar.setVisibility(View.VISIBLE);
                linerFechas.setVisibility(View.VISIBLE);

            }
        });

        prev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mapFragment.getView().setVisibility(View.VISIBLE);
                prev.setVisibility(View.VISIBLE);
                next2.setVisibility(View.VISIBLE);
                prev2.setVisibility(View.GONE);
                linerFechas.setVisibility(View.GONE);
                agrearFecha.setVisibility(View.GONE);
                botonAgregar.setVisibility(View.GONE);
            }
        });

        agrearFecha.setOnClickListener(new View.OnClickListener() {
            String miFecha1, miFecha2;
            @Override
            public void onClick(View view) {
                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                LinearLayout linearTitulos = new LinearLayout(getApplicationContext());
                LinearLayout linear = new LinearLayout(getApplicationContext());
                final TextView fechaIni = new TextView(getApplicationContext());
                final TextView fechaFin = new TextView(getApplicationContext());
                final TextView guion = new TextView(getApplicationContext());
                final TextView nFechaIni = new TextView(getApplicationContext());
                final TextView nFechaFin = new TextView(getApplicationContext());
                fechaIni.setText("___/___/____/");
                fechaFin.setText("___/___/____/");
                guion.setText("-");
                fechaIni.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        obtenerFecha(fechaIni);
                        Log.i("fffffff",unaFecha);
                        miFecha1= unaFecha;

                    }

                });
                //  Log.i("FEchaaaaaaa",fechaIni.getText().toString());
                fechaFin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("gggggg",unaFecha);
                        obtenerFecha(fechaFin);
                        Log.i("-----",fechaFin.getText().toString());
                        miFecha2=unaFecha;
                        //     Log.i("FEchaaaaaaa",fechaFin.getText().toString());
                    }
                });

                fechaIni.setInputType(4);
                fechaFin.setInputType(4);
                //fechaIni.setTextColor(@colors/white");
                //fechaFin.setTextColor(Integer.parseInt("@android:color/white"));
                fechaIni.setTextSize(20);
                fechaFin.setTextSize(21);




                nFechaIni.setText("Fecha Inicial    ");
                nFechaFin.setText("Fecha Final");
                nFechaIni.setTextColor(Color.WHITE);
                nFechaFin.setTextColor(Color.WHITE);
                fechaFin.setTextColor(Color.WHITE);
                fechaIni.setTextColor(Color.WHITE);


                //nFechaFin.setTe



                linearTitulos.setOrientation(LinearLayout.HORIZONTAL);
                linearTitulos.addView(nFechaIni);
                linearTitulos.addView(nFechaFin);
                //fecha.setOnClickListener();
                //fecha.inpu
                //editText.setText("ffrfrfrffr");
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.addView(fechaIni);
                linearLayout.addView(guion);
                linearLayout.addView(fechaFin);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(30, 20, 30, 0);



                linerFechas.addView(linearTitulos,layoutParams);
               // linerFechas.addView(guion,layoutParams);
                linerFechas.addView(linearLayout,layoutParams);
                //linerFechas

                String miFechita=miFecha1+"-"+miFecha2;
                Log.i("fechitaaaaaa",miFechita);
                //listaFechas.add(miFechita);

            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case IMAGE_PICKER_REQUEST:
                if(resultCode == RESULT_OK){
                    try {
                        imageUri[contFoto] = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri[contFoto]);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        fotos[contFoto].setImageBitmap(selectedImage);
                        fotos[contFoto].setMaxHeight(106);
                        fotos[contFoto].setMaxWidth(106);
                        contFoto++;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            /*case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    Log.i("URI", imageUri.toString());
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    foto.setImageBitmap(imageBitmap);
                    foto.setMaxHeight(106);
                    foto.setMaxWidth(106);
                }
                break;*/

        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigationRecord:
                    intent = new Intent(getApplicationContext(),HistorialActivity.class);
                    //intent.putExtra("rol","propietarioAlojamiento");
                    if (anfitrion != null)
                        intent.putExtra("usr", anfitrion);
                    startActivity(intent);
                    return true;
                case R.id.navigationProfile:
                    intent = new Intent(getApplicationContext(),PerfilActivity.class);
                    // intent.putExtra("rol","propietarioAlojamiento");
                    if (anfitrion != null)
                        intent.putExtra("usr", anfitrion);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final Geocoder geo = new Geocoder(getBaseContext());


        LatLng bogota = new LatLng(4.65, -74.05);
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));

        // MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng);


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(marker != null){
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions().position(latLng));
                latMarcador=marker.getPosition().latitude;
                longMarcador=marker.getPosition().longitude;
                try {
                    List<Address> addresses = geo.getFromLocation(latMarcador,longMarcador,2);
                    Address addressResult = addresses.get(0);
                    ubicacionET.setText(addressResult.getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //String direccion=obtenenerDireccion(latMarcador,longMarcador);

            }
        });
    }
}