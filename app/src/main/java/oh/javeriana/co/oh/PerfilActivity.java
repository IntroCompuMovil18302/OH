package oh.javeriana.co.oh;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PerfilActivity extends Activity {
    FirebaseAuth mAuth;

    Button botonGestionar;
    Button botonAgregar;
    Button explorar;
    Button historial;
    Button perfil;
    Button btnSignOut;
    TextView txNombre;
    TextView txCorreo;
    TextView txRol;
    ImageView imgProfile;
    String rol="";
    Huesped huesped=null;
    Anfitrion anfitrion=null;
    Propietario propietario = null;
    String pathImg="";

    FirebaseStorage storage = FirebaseStorage.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        botonGestionar=findViewById(R.id.gestionar);
        botonAgregar=findViewById(R.id.agregar);
        btnSignOut=findViewById(R.id.btnSignOut);
        txNombre=findViewById(R.id.nombre);
        txCorreo=findViewById(R.id.correoElectronico);
        txRol=findViewById(R.id.rol);
        imgProfile=findViewById(R.id.imageProfile);

        mAuth = FirebaseAuth.getInstance();

        rol = getIntent().getSerializableExtra("usr").getClass().getName();

        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") == 0) {
            huesped = (Huesped) getIntent().getSerializableExtra("usr");
            txNombre.setText(txNombre.getText().toString() + huesped.getNombre());
            txCorreo.setText(txCorreo.getText().toString() + huesped.getEmail());
            txRol.setText(txRol.getText().toString() + huesped.getRol());
            pathImg=huesped.getId();
        }
        else if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Anfitrion") == 0) {
            anfitrion = (Anfitrion) getIntent().getSerializableExtra("usr");
            txNombre.setText(txNombre.getText().toString() + anfitrion.getNombre());
            txCorreo.setText(txCorreo.getText().toString() + anfitrion.getEmail());
            txRol.setText(txRol.getText().toString() + anfitrion.getRol());
            pathImg=anfitrion.getId();
        }
        else if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Propietario") == 0) {
            //anfitrion = (Anfitrion) getIntent().getSerializableExtra("usr");
            propietario = (Propietario) getIntent().getSerializableExtra("usr");
            txNombre.setText(txNombre.getText().toString() + propietario.getNombre());
            txCorreo.setText(txCorreo.getText().toString() + propietario.getEmail());
            txRol.setText(txRol.getText().toString() + propietario.getRol());
            pathImg=propietario.getId();
        }


        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child(pathImg+"/imageProfile");
        final long ONE_MEGABYTE = 1024 * 1024;
        imagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imgProfile.setImageBitmap(bitmap);
                imgProfile.setMaxHeight(106);
                imgProfile.setMaxWidth(106);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("ERROR: ", "Error al cargar la foto");
            }
        });



        BottomNavigationView guest_navigation = (BottomNavigationView) findViewById(R.id.guest_navigation);
        BottomNavigationView host_navigation = (BottomNavigationView) findViewById(R.id.host_navigation);;

        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") != 0){
            guest_navigation.setVisibility(View.GONE);
            host_navigation.setSelectedItemId(R.id.navigationProfile);
            host_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
           // if(rol.equals("propietarioAlojamiento")){
            if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Anfitrion") == 0){
                botonAgregar.setText("Agregar alojamiento");
                botonGestionar.setText("Gestionar alojamientos");
            }else{
                botonAgregar.setText("Agregar Negocio");
                botonGestionar.setText("Gestionar Negocio");
            }
        }else{
            botonAgregar.setVisibility(View.GONE);
            botonGestionar.setVisibility(View.GONE);
            host_navigation.setVisibility(View.GONE);
            guest_navigation.setSelectedItemId(R.id.navigationProfile);
            guest_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Anfitrion")== 0){
                    intent =new Intent(getApplicationContext(),AgregarAlojamientoActivity.class );
                }else if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Propietario")== 0){
                    intent =new Intent(getApplicationContext(),CrearNegocioActivity.class );
                }
                if(anfitrion != null)
                    intent.putExtra("usr", anfitrion);
                else if (huesped != null)
                    intent.putExtra("usr", huesped);
                else if (propietario != null)
                    intent.putExtra("usr", propietario);
                startActivity(intent);
            }
        });

        botonGestionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),EditarAlojamientoActivity.class);
                if(anfitrion != null)
                    intent.putExtra("usr", anfitrion);
                else if (huesped != null)
                    intent.putExtra("usr", huesped);
                else if (propietario != null)
                    intent.putExtra("usr", propietario);
                startActivity(intent);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), InicioActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigationExplore:
                    /*intent = new Intent(getApplicationContext(),MapaActivity.class);
                    if(anfitrion != null)
                        intent.putExtra("usr", anfitrion);
                    else if (huesped != null)
                        intent.putExtra("usr", huesped);

                    startActivity(intent);*/
                    PerfilActivity.this.finish();
                    return true;

                case R.id.navigationRecord:
                    intent = new Intent(getApplicationContext(),HistorialActivity.class);

                    if(anfitrion != null)
                        intent.putExtra("usr", anfitrion);
                    else if (huesped != null)
                        intent.putExtra("usr", huesped);
                    else if (propietario != null)
                        intent.putExtra("usr", propietario);
                    startActivity(intent);
                    return true;

                case R.id.navigationProfile:
                    return true;
            }
            return false;
        }
    };

}
