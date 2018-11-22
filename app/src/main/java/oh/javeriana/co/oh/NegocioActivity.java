package oh.javeriana.co.oh;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NegocioActivity extends AppCompatActivity {

    Huesped huesped=null;
    Anfitrion anfitrion=null;
    Negocio negocio=null;
    Propietario propietario=null;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    ImageView foto;
    String rol = "";
    TextView nombre;
    TextView horaApertura ;
    TextView horaCierre ;
    TextView telefono;
    TextView serviciosAdicionales;
    TextView producto;

    String pathImg = "";
    String idUsr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocio);

        nombre = findViewById(R.id.nombre);
        horaApertura = findViewById(R.id.horaApertura);
        horaCierre = findViewById(R.id.horaCierre);
        telefono = findViewById(R.id.telefono);
        serviciosAdicionales = findViewById(R.id.serviciosAdicionales);
        producto = findViewById(R.id.producto);

        BottomNavigationView guest_navigation = (BottomNavigationView) findViewById(R.id.guest_navigation);
        BottomNavigationView host_navigation = (BottomNavigationView) findViewById(R.id.host_navigation);

        rol = getIntent().getExtras().getSerializable("usr").getClass().getName();
        Log.i("ROL", rol);
        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") == 0) {
            huesped = (Huesped) getIntent().getExtras().getSerializable("usr");
        }else if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Anfitrion") == 0){
            anfitrion = (Anfitrion) getIntent().getExtras().getSerializable("usr");
        }else{
            propietario = (Propietario) getIntent().getExtras().getSerializable("usr");
        }

        negocio = (Negocio) getIntent().getExtras().getSerializable("negocio");
        String idNegocio = (String) getIntent().getExtras().getString("idNeg");
        idUsr =  getIntent().getExtras().getString("idUsr");
        pathImg = idUsr + "/" + idNegocio + "/";


        StorageReference storageRef = storage.getReference();

        for(int i=0; i<4; i++) {
            Log.i("RUTA: ", pathImg + "image" + (i+1));
            final int sub = i;
            StorageReference imagesRef = storageRef.child(pathImg + "image" + (i+1));
            final long ONE_MEGABYTE = 1024 * 1024;
            imagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    foto.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("ERROR: ", "Error al cargar la foto");
                }
            });
        }

        nombre.setText(negocio.getNombre());
        horaApertura.setText(negocio.getHoraApertura());
        horaCierre.setText(negocio.getHoraCierre());
        telefono.setText(negocio.getTelefono());
        if (negocio.isServicioAdicional()){
            switch (negocio.getTipo()){
                case 1:
                    serviciosAdicionales.setText("Wifi");
                    break;
                case 2:
                    serviciosAdicionales.setText("Inyectologia");
                    break;
                case 3:
                    serviciosAdicionales.setText("Menu infantil");
                    break;
                case 4:
                    serviciosAdicionales.setText("Pago con tarjeta de credito");
                    break;
            }
        }if(negocio.isDomicilios()){
            serviciosAdicionales.setText(serviciosAdicionales.getText()+" , domicilios");
        }
        producto.setText(negocio.getCatalogo());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onClick(View v){
        Intent intent = new Intent(getApplicationContext(),ReservarActivity.class);
        if(anfitrion != null)
            intent.putExtra("usr", anfitrion);
        else if (huesped != null)
            intent.putExtra("usr", huesped);
        else if (propietario != null)
            intent.putExtra("usr", propietario);
        startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigationExplore:
                    intent = new Intent(getApplicationContext(),MapaActivity.class);
                    if(anfitrion != null)
                        intent.putExtra("usr", anfitrion);
                    else if (huesped != null)
                        intent.putExtra("usr", huesped);
                    else
                        intent.putExtra("usr",propietario);
                    startActivity(intent);
                    return true;
                case R.id.navigationRecord:
                    intent = new Intent(getApplicationContext(),HistorialActivity.class);
                    if(anfitrion != null)
                        intent.putExtra("usr", anfitrion);
                    else if (huesped != null)
                        intent.putExtra("usr", huesped);
                    else
                        intent.putExtra("usr",propietario);
                    startActivity(intent);
                    return true;
                case R.id.navigationProfile:
                    intent = new Intent(getApplicationContext(),PerfilActivity.class);
                    if(anfitrion != null)
                        intent.putExtra("usr", anfitrion);
                    else if (huesped != null)
                        intent.putExtra("usr", huesped);
                    else
                        intent.putExtra("usr",propietario);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
}
