package oh.javeriana.co.oh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;

public class AgregarAlojamientoActivity extends Activity {
    EditText nombreET;
    EditText descripcionET;
    EditText precioET;
    EditText ubicacionET;
    Spinner spinnerTipo;
    EditText cantHuespedesET;
    Button botonAgregar;
    public static final String PATH_ALOJAMIENTOS="alojamientos/";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_alojamiento);

        nombreET = findViewById(R.id.nombreET);
        descripcionET = findViewById(R.id.descripcionET);
        precioET = findViewById(R.id.precioET);
        ubicacionET = findViewById(R.id.ubicacionET);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        cantHuespedesET =  findViewById(R.id.cantHuespedesET);
        botonAgregar = findViewById(R.id.botonAgregar);

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mStorageRef = FirebaseStorage.getInstance().getReference();
                myRef = database.getReference(PATH_ALOJAMIENTOS);
                String key = myRef.push().getKey();
                myRef = database.getReference(PATH_ALOJAMIENTOS + key);

                if(!nombreET.getText().equals("")) {
                   if (!descripcionET.getText().equals("")){
                        if (!precioET.getText().equals("")){
                            if (!ubicacionET.getText().equals("")){
                                    if (!cantHuespedesET.getText().equals("")){
                                        Alojamiento alojamiento = new Alojamiento(nombreET.getText(), descripcionET.getText(), ubicacionET.getText(), cantHuespedesET.getText(),  precioET.getText() );
                                        myRef.setValue(alojamiento);

                                    }
                            }
                        }
                   }
                }


            }
        });



    }

}
