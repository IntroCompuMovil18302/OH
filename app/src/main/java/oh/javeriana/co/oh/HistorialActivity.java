package oh.javeriana.co.oh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HistorialActivity extends Activity {

    int [] images = {R.drawable.alojamiento, R.drawable.alojamiento, R.drawable.alojamiento, R.drawable.alojamiento, R.drawable.alojamiento, R.drawable.alojamiento};
    String[] names = {"Aloja1", "Aloja2", "Aloja3", "Aloja4", "Aloja5", "Aloja6"};
    String[] prop = {"Propietario1", "Propietario2", "Propietario3", "Propietario4", "Propietario5", "Propietario6"};
    String[] descr = {"Breve descripción del ítem", "Breve descripción del ítem", "Breve descripción del ítem", "Breve descripción del ítem", "Breve descripción del ítem", "Breve descripción del ítem"};
    String[] precio = {"$ 300000", "$ 800000", "$ 1500000", "$ 480000", "$ 270000", "$ 1246000"};
    String rol =  "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        rol =  getIntent().getStringExtra("rol");

        ListView listView = findViewById(R.id.listViewHist);
        HistorialActivity.CustomAdapter c = new HistorialActivity.CustomAdapter();
        listView.setAdapter(c);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), ItemActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView guest_navigation = (BottomNavigationView) findViewById(R.id.guest_navigation);
        BottomNavigationView host_navigation = (BottomNavigationView) findViewById(R.id.host_navigation);
        TextView tituloHistorial = findViewById(R.id.tituloHistorial);

        if(!rol.equals("huesped")){
            guest_navigation.setVisibility(View.GONE);
            host_navigation.setSelectedItemId(R.id.navigationRecord);
            host_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            if(rol.equals("propietarioAlojamiento")) {
                tituloHistorial.setText("Historial de alojamientos publicados");
            }else {
                tituloHistorial.setText("Historial de negocios publicados");
            }
        }else{
            host_navigation.setVisibility(View.GONE);
            guest_navigation.setSelectedItemId(R.id.navigationRecord);
            guest_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            tituloHistorial.setText("Historial de reservaciones");
        }


       // Button perfil = findViewById(R.id.perfil);
       // Button explorar = findViewById(R.id.explorar);

/*        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PerfilActivity.class);
                intent.putExtra("rol",rol);
                startActivity(intent);
            }
        });

        explorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ExplorarActivity.class);
                startActivity(intent);
            }
        });*/
    }

    class CustomAdapter extends BaseAdapter {

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

            Button botonCalificar = view.findViewById(R.id.calificar);
            if(!rol.equals("huesped")){
                botonCalificar.setVisibility(View.GONE);
            }

            botonCalificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),CalificarAlojamientoActivity.class);
                    startActivity(intent);
                }
            });

            imageView.setImageResource(images[i]);
            txtVwName.setText(names[i]);
            txtVwProp.setText(prop[i]);
            txtDesc.setText(descr[i]);

            return view;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigationExplore:
                    intent = new Intent(getApplicationContext(),ExplorarActivity.class);
                    intent.putExtra("rol",rol);
                    startActivity(intent);
                    return true;
                case R.id.navigationRecord:
                    return true;
                case R.id.navigationProfile:
                    intent = new Intent(getApplicationContext(),PerfilActivity.class);
                    intent.putExtra("rol",rol);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

}
