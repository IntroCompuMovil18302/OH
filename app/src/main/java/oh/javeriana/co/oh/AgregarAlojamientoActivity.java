package oh.javeriana.co.oh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.location.Geocoder;



import java.text.ParseException;

public class AgregarAlojamientoActivity extends Activity {
    EditText nombreET;
    EditText descripcionET;
    EditText precioET;
    EditText ubicacionET;
    Spinner spinnerTipo;
    ArrayAdapter<CharSequence>adapter;
    EditText cantHuespedesET;
    Button botonAgregar;
    public static final String PATH_ALOJAMIENTOS="alojamientos/";
    public static final String PATH_USERS="usuarios/";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private String tipoAlojamiento;

    Anfitrion anfitrion = null;
    String rol = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_alojamiento);

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


                if(!nombreET.getText().equals("")) {
                    myRef = database.getReference(PATH_ALOJAMIENTOS + nombreET.getText().toString());
                    if (!descripcionET.getText().equals("")){
                        if (!precioET.getText().equals("")){
                            if (!ubicacionET.getText().equals("")){
                                if (!cantHuespedesET.getText().equals("")){
                                    int cant = Integer.parseInt(cantHuespedesET.getText().toString());
                                    double precio = Double.parseDouble( precioET.getText().toString());
                                    Alojamiento alojamiento = new Alojamiento(nombreET.getText().toString(), descripcionET.getText().toString(), ubicacionET.getText().toString(), cant,precio, mAuth.getCurrentUser().getDisplayName(),tipoAlojamiento );
                                    myRef.setValue(alojamiento);
                                    Log.i("ROL", "ESTA AGREGANDO UN SUPER ALOJAMIENTO LA PUTA MADRE QUE ME PARIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");

                                }
                            }
                        }
                    }
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