package oh.javeriana.co.oh;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.location.Geocoder;


import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class AgregarAlojamientoActivity extends Activity {
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


    EditText nombreET;
    EditText descripcionET;
    EditText precioET;
    EditText ubicacionET;
    Spinner spinnerTipo;
    ArrayAdapter<CharSequence>adapter;
    EditText cantHuespedesET;
    Button botonAgregar;
    TextView fechaInicial;
    TextView fechaFinal;
    final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    Anfitrion anfitrion = null;
    String rol = "";

    public void obtenerFecha(final int codigo){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10)? "0" + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10)? "0" + String.valueOf(mesActual):String.valueOf(mesActual);
                if (codigo==1)
                    fechaInicial.setText(diaFormateado + "/" + mesFormateado + "/" + year);
                else
                    fechaFinal.setText(diaFormateado + "/" + mesFormateado + "/" + year);
            }
        },anio, mes, dia);

        recogerFecha.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_alojamiento);

        anfitrion = (Anfitrion) getIntent().getSerializableExtra("usr");

        database= FirebaseDatabase.getInstance();

        nombreET = findViewById(R.id.nombreET);
        descripcionET = findViewById(R.id.descripcionET);
        precioET = findViewById(R.id.precioET);
        ubicacionET = findViewById(R.id.ubicacionET);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        cantHuespedesET =  findViewById(R.id.cantHuespedesET);
        botonAgregar = findViewById(R.id.botonAgregar);
        adapter = ArrayAdapter.createFromResource(this, R.array.alojamientos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);
        fechaInicial = findViewById(R.id.fechaInic);
        fechaFinal = findViewById(R.id.fechaFinal);

        fechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha(1);
            }
        });
        fechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha(2);
            }
        });


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

        rol = getIntent().getSerializableExtra("usr").getClass().getName();
        Log.i("ROL", rol);
        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Anfitrion") == 0) {
            anfitrion = (Anfitrion) getIntent().getSerializableExtra("usr");
        }


        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mStorageRef = FirebaseStorage.getInstance().getReference();
                //myRef2 = database().ref('users');
                //myRef2 = database.getReference(PATH_USERS);
                //String key = myRef.push().getKey();
                myRef = database.getReference(PATH_ALOJAMIENTOS);


                if(!nombreET.getText().toString().isEmpty()) {
                    if(!tools.esNumero(String.valueOf(nombreET.getText()))){
                        myRef = database.getReference(PATH_ALOJAMIENTOS + nombreET.getText().toString());
                        if (!descripcionET.getText().toString().isEmpty()){
                            if (!tools.esNumero(String.valueOf(descripcionET.getText()))){
                                if (!precioET.getText().toString().isEmpty()){
                                    if(tools.esNumero(String.valueOf(precioET.getText()))){
                                        if (!ubicacionET.getText().toString().isEmpty()){
                                            if (!cantHuespedesET.getText().toString().isEmpty()){
                                                if (tools.esNumero(String.valueOf(cantHuespedesET.getText()))){

                                                    Geocoder mGeocoder = new Geocoder(getBaseContext());
                                                    LatLng position=null;
                                                    try {
                                                        List<Address> addresses = mGeocoder.getFromLocationName(ubicacionET.getText().toString(), 2, ABAJOIZQLAT, ABAJOIZQLONG, ARRIBADERLAT, ARRIBADERLONG);
                                                        Log.i("LATITUD", "HOLAAAAAAAAAAAAAAA");

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
                                                            cant, precio, anfitrion.getId(), tipoAlojamiento, latitud, longitud, fechaInicial.getText().toString(), fechaFinal.getText().toString() );
                                                    myRef.setValue(alojamiento);

                                                    Log.i("ROL", "ESTA AGREGANDO UN ALOJAMIENTO");

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

}