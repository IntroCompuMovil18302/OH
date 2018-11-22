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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class ItemActivity extends AppCompatActivity {

    Huesped huesped=null;
    Anfitrion anfitrion=null;
    Alojamiento alojamiento=null;
    Propietario propietario=null;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    String rol = "";
    Button button ;
    LinearLayout layout ;
    String pathImg = "";
    ImageView fotos[];
    ImageView foto1;
    ImageView foto2;
    ImageView foto3;
    ImageView foto4;
    TextView descr;
    TextView precio;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getSupportActionBar().hide();

        //rol =  getIntent().getStringExtra("rol");
        button = findViewById(R.id.button);
        fotos = new ImageView[4];
        fotos[0] = findViewById(R.id.imageView1);
        fotos[1] = findViewById(R.id.imageView2);
        fotos[2] = findViewById(R.id.imageView3);
        fotos[3] = findViewById(R.id.imageView4);
        layout = findViewById(R.id.CommentUser);
        descr = findViewById(R.id.txtVwDescription);
        calendarView = findViewById(R.id.calendarView);
        precio = findViewById(R.id.txtVwPrice);

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

        alojamiento = (Alojamiento) getIntent().getExtras().getSerializable("alojamiento");
        String idAloj = (String) getIntent().getExtras().getString("idAloj");
        pathImg = alojamiento.getIdUsuario() + "/" + idAloj + "/";


        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") != 0){
            button.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
            guest_navigation.setVisibility(View.GONE);
            host_navigation.getMenu().getItem(0).setCheckable(false);
            host_navigation.setSelectedItemId(R.id.navigationRecord);
            host_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }else{
            host_navigation.setVisibility(View.GONE);
            guest_navigation.getMenu().getItem(1).setCheckable(false);
            guest_navigation.setSelectedItemId(R.id.navigationRecord);
            guest_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }

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
                        fotos[sub].setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.i("ERROR: ", "Error al cargar la foto");
                    }
                });
        }

        descr.setText(alojamiento.getDescripcion());
        precio.setText("$ (COP) " + String.valueOf(alojamiento.getValorNoche()));

        try {
            Date fi = alojamiento.getFechaInicialDate();
            Date ff = alojamiento.getFechaFinalDate();

            Log.i("Fecha", fi.toString());

            Calendar c1 = Calendar.getInstance();
            c1.setTime(fi);
            //c1.set(fi.g, fi.getMonth()-1, fi.getDay())

            Calendar c2 = Calendar.getInstance();
            c2.setTime(ff);
            //c2.set(ff.getYear(), ff.getMonth()-1, ff.getDay());

            Log.i("Fecha", ""+fi.getYear());

            calendarView.setMinDate(c1.getTimeInMillis());
            calendarView.setMaxDate(c2.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }


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
                    startActivity(intent);
                    return true;
                case R.id.navigationRecord:
                    intent = new Intent(getApplicationContext(),HistorialActivity.class);
                    if(anfitrion != null)
                        intent.putExtra("usr", anfitrion);
                    else if (huesped != null)
                        intent.putExtra("usr", huesped);
                    startActivity(intent);
                    return true;
                case R.id.navigationProfile:
                    intent = new Intent(getApplicationContext(),PerfilActivity.class);
                    if(anfitrion != null)
                        intent.putExtra("usr", anfitrion);
                    else if (huesped != null)
                        intent.putExtra("usr", huesped);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };


}
