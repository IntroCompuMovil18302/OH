package oh.javeriana.co.oh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistorialActivity extends Activity {

    List<String> idAlojamientos = new ArrayList<>();
    List<Alojamiento> alojamientos = new ArrayList<>();

    int [] images = {R.drawable.alojamiento, R.drawable.alojamiento};
    /*String[] names = {"Aloja1", "Aloja2", "Aloja3", "Aloja4", "Aloja5", "Aloja6"};
    String[] prop = {"Propietario1", "Propietario2", "Propietario3", "Propietario4", "Propietario5", "Propietario6"};
    String[] descr = {"Breve descripción del ítem", "Breve descripción del ítem", "Breve descripción del ítem", "Breve descripción del ítem", "Breve descripción del ítem", "Breve descripción del ítem"};
    String[] precio = {"$ 300000", "$ 800000", "$ 1500000", "$ 480000", "$ 270000", "$ 1246000"};*/
    String rol =  "";

    public static final String PATH_RESERVAS = "reservas/";
    public static final String PATH_ALOJAMIENTOS = "alojamientos/";

    Huesped huesped = null;
    Anfitrion anfitrion = null;
    Propietario propietario = null;
    String idUsr = "";
    String nombreAlojamiento;
    String nombreProducto;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference myRefAloj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        database= FirebaseDatabase.getInstance();

        rol = getIntent().getSerializableExtra("usr").getClass().getName();
        idUsr = (String) getIntent().getExtras().getString("idUsr");
        Log.i("IdUsr", idUsr);

        myRef = database.getReference(PATH_RESERVAS + idUsr + "/");
        myRefAloj = database.getReference(PATH_ALOJAMIENTOS );


        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") == 0) {
            huesped = (Huesped) getIntent().getSerializableExtra("usr");
        }
        else if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Anfitrion") == 0) {
            anfitrion = (Anfitrion) getIntent().getSerializableExtra("usr");
        }
        else if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Propietario") == 0) {
            propietario = (Propietario) getIntent().getSerializableExtra("usr");
        }

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    idAlojamientos.add(singleSnapshot.child(singleSnapshot.getKey()).toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ERROR", "error en la consulta", databaseError.toException());
            }
        });

        myRefAloj.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    alojamientos.add(singleSnapshot.getValue(Alojamiento.class));
                }

                ListView listView = findViewById(R.id.listViewHist);
                HistorialActivity.CustomAdapter c = new HistorialActivity.CustomAdapter();
                listView.setAdapter(c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), ItemActivity.class);
               // intent.putExtra("rol",rol);
                if(anfitrion != null)
                    intent.putExtra("usr", anfitrion);
                else if (huesped != null)
                    intent.putExtra("usr", huesped);
                else if (propietario != null)
                    intent.putExtra("usr", propietario);
                startActivity(intent);
            }
        });*/

        BottomNavigationView guest_navigation = (BottomNavigationView) findViewById(R.id.guest_navigation);
        BottomNavigationView host_navigation = (BottomNavigationView) findViewById(R.id.host_navigation);
        TextView tituloHistorial = findViewById(R.id.tituloHistorial);

        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") != 0){
            guest_navigation.setVisibility(View.GONE);
            host_navigation.setSelectedItemId(R.id.navigationRecord);
            host_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Anfitrion") == 0){
                tituloHistorial.setText("Historial de alojamientos publicados");
            }else {
                tituloHistorial.setText("Historial de negocios publicados");
            }
        }else {
            host_navigation.setVisibility(View.GONE);
            guest_navigation.setSelectedItemId(R.id.navigationRecord);
            guest_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            tituloHistorial.setText("Historial de reservaciones");
        }
    }

    class CustomAdapter extends BaseAdapter {

        Button btnRuta;

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.customlayout_hist,null);

            ImageView imageView = view.findViewById(R.id.imageView11);
            TextView txtVwName = view.findViewById(R.id.nomProd);
            TextView txtVwProp = view.findViewById(R.id.propProd);
            TextView txtPrice = view.findViewById(R.id.precioProd);
            TextView txtDesc = view.findViewById(R.id.descProd);

            imageView.setImageResource(images[i]);
            txtVwName.setText(alojamientos.get(i).getNombre());
            txtVwProp.setText(alojamientos.get(i).getFechaFinal());
            if(alojamientos.get(i).getDescripcion().length() > 20)
                txtDesc.setText(alojamientos.get(i).getDescripcion().substring(0, 20) + "...");
            else
                txtDesc.setText(alojamientos.get(i).getDescripcion());

            nombreAlojamiento = txtVwName.getText().toString();
            btnRuta = view.findViewById(R.id.ruta);

            Button botonCalificar = view.findViewById(R.id.calificar);
            if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped")!=0){
                botonCalificar.setVisibility(View.GONE);
            }

            btnRuta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            botonCalificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),CalificarAlojamientoActivity.class);
                    if(anfitrion != null)
                        intent.putExtra("usr", anfitrion);
                    else if (huesped != null)
                        intent.putExtra("usr", huesped);
                        intent.putExtra("nombreAlo", nombreAlojamiento);

                    startActivity(intent);
                }
            });



            return view;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;

            Bundle bundle = new Bundle();
            bundle.putString("idUsr", idUsr);

            if(anfitrion != null)
                bundle.putSerializable("usr", anfitrion);
            else if (huesped != null)
                bundle.putSerializable("usr", huesped);
            else if (propietario != null)
                bundle.putSerializable("usr", propietario);

            switch (item.getItemId()) {
                case R.id.navigationExplore:
                    intent = new Intent(getApplicationContext(),MapaActivity.class);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigationRecord:
                    return true;
                case R.id.navigationProfile:
                    intent = new Intent(getApplicationContext(),PerfilActivity.class);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

}
