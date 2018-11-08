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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;

public class ItemActivity extends AppCompatActivity {

    Huesped huesped=null;
    Anfitrion anfitrion=null;
    Alojamiento alojamiento=null;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    String rol = "";
    Button button ;
    LinearLayout layout ;
    String pathImg = "";
    ImageView foto1;
    ImageView foto2;
    ImageView foto3;
    ImageView foto4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getSupportActionBar().hide();

        //rol =  getIntent().getStringExtra("rol");
        button = findViewById(R.id.button);
        foto1 = findViewById(R.id.imageView1);
        foto2 = findViewById(R.id.imageView2);
        foto3 = findViewById(R.id.imageView3);
        foto4 = findViewById(R.id.imageView4);
        layout = findViewById(R.id.CommentUser);

        BottomNavigationView guest_navigation = (BottomNavigationView) findViewById(R.id.guest_navigation);
        BottomNavigationView host_navigation = (BottomNavigationView) findViewById(R.id.host_navigation);

        rol = getIntent().getExtras().getSerializable("usr").getClass().getName();
        Log.i("ROL", rol);
        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") == 0) {
            huesped = (Huesped) getIntent().getExtras().getSerializable("usr");
        }else{
            anfitrion = (Anfitrion) getIntent().getExtras().getSerializable("usr");
        }

        alojamiento = (Alojamiento) getIntent().getExtras().getSerializable("alojamiento");
        pathImg = alojamiento.getIdUsuario() + "/" + alojamiento.getIdAloj() + "/";


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
            StorageReference imagesRef = storageRef.child(pathImg + "image" + (i+1));
            final long ONE_MEGABYTE = 1024 * 1024;
                imagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "images/island.jpg" is returns, use this as needed
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        foto1.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.i("ERROR: ", "Error al cargar la foto");
                    }
                });
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
