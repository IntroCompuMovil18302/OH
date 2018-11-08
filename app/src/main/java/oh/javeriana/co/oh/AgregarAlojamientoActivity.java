package oh.javeriana.co.oh;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
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
import android.widget.ImageView;
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


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    TextView fechaInicial;
    TextView fechaFinal;
    Uri imageUri[];
    ImageView fotos[];
    ImageView foto1;
    ImageView foto2;
    ImageView foto3;
    ImageView foto4;
    int contFoto;

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


                                                    Alojamiento alojamiento = new Alojamiento(key, nombreET.getText().toString(), descripcionET.getText().toString(), ubicacionET.getText().toString(),
                                                            cant, precio, anfitrion.getId(), tipoAlojamiento, latitud, longitud, fechaInicial.getText().toString(), fechaFinal.getText().toString() );
                                                    myRef.setValue(alojamiento);

                                                    for(int i=0; i<fotos.length; i++){
                                                        if(imageUri[i] != null) {
                                                            StorageReference imagesProfile = mStorageRef.child(key).child( key + "/image" + (i+1));
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

}