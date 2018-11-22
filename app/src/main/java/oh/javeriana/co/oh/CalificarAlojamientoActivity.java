package oh.javeriana.co.oh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CalificarAlojamientoActivity extends AppCompatActivity {

    public static final String PATH_CALIFICACIONES="calificaciones/";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    Huesped huesped = null;
    String rol = "";
    String nombreAlojamiento = "";
    private FirebaseAuth mAuth;
    RatingBar estrellas;
    EditText comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar_alojamiento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database= FirebaseDatabase.getInstance();

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Button enviar = findViewById(R.id.enviar);
        comentario =  findViewById(R.id.comentario);
        estrellas = findViewById(R.id.ratingBar);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float numeroEstrellas;
                numeroEstrellas = estrellas.getRating();
                String comentarioS = comentario.getText().toString();
                //Toast.makeText(getApplicationContext(),"Estrelllitas#"+String.valueOf(numeroEstrellas)+" Comentario: "+comentarioS,Toast.LENGTH_LONG).show();//Log.i("ESTRELLITAS", String.valueOf(numeroEstrellas));
                //Toast.makeText(getApplicationContext(),"NOMBREUSUARIO#"+huesped.getNombre()+" NOMBREALOJA: "+nombreAlojamiento,Toast.LENGTH_LONG).show();
                //Log.i("COMENTARIO", comentarioS);


                mAuth = FirebaseAuth.getInstance();
                mStorageRef = FirebaseStorage.getInstance().getReference();
                myRef = database.getReference(PATH_CALIFICACIONES);
                String key = myRef.push().getKey();
                myRef = database.getReference(PATH_CALIFICACIONES + key);
                Calificacion calificacion = new Calificacion(huesped.getNombre(),nombreAlojamiento, numeroEstrellas,comentarioS);
                myRef.setValue(calificacion);
                //Alojamiento alojamiento = new Alojamiento(nombreET.getText().toString(), descripcionET.getText().toString(), ubicacionET.getText().toString(),
                //cant, precio, anfitrion.getId(), tipoAlojamiento, latitud, longitud, fechaInicial.getText().toString(), fechaFinal.getText().toString() );
                //myRef.setValue();

                //finish();

            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(0).setCheckable(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        rol = getIntent().getSerializableExtra("usr").getClass().getName();
        nombreAlojamiento = getIntent().getStringExtra("nombreAlo");
        Log.i("ROL", rol);
        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") == 0) {
            huesped = (Huesped) getIntent().getSerializableExtra("usr");
            //nombreAlojamiento =(String) getIntent().getSerializableExtra("nombreAlo");
            Log.i("NOMBREALOJA", nombreAlojamiento);
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigationExplore:
                    intent = new Intent(getApplicationContext(),MapaActivity.class);
                    // intent.putExtra("rol","huesped");
                    if (huesped != null)
                        intent.putExtra("usr", huesped);
                    startActivity(intent);
                    return true;
                case R.id.navigationRecord:
                    intent = new Intent(getApplicationContext(),HistorialActivity.class);
                    // intent.putExtra("rol","huesped");
                    if (huesped != null)
                        intent.putExtra("usr", huesped);
                    startActivity(intent);
                    return true;
                case R.id.navigationProfile:
                    intent = new Intent(getApplicationContext(),PerfilActivity.class);
                    if (huesped != null)
                        intent.putExtra("usr", huesped);
                    // intent.putExtra("rol","huesped");
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

}
