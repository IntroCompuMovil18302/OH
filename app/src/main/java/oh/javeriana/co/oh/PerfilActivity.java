package oh.javeriana.co.oh;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PerfilActivity extends Activity {

    Button botonGestionar;
    Button botonAgregar;
    Button explorar;
    Button historial;
    Button perfil;
    String rol="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        botonGestionar=findViewById(R.id.gestionar);
        botonAgregar=findViewById(R.id.agregar);

        rol =  getIntent().getStringExtra("rol");

        BottomNavigationView guest_navigation = (BottomNavigationView) findViewById(R.id.guest_navigation);
        BottomNavigationView host_navigation = (BottomNavigationView) findViewById(R.id.host_navigation);;

        if(!rol.equals("huesped")){
            guest_navigation.setVisibility(View.GONE);
            host_navigation.setSelectedItemId(R.id.navigationProfile);
            host_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            if(rol.equals("propietarioAlojamiento")){
                botonAgregar.setText("Agregar alojamiento");
                botonGestionar.setText("Gestionar alojamientos");
            }else{
                botonAgregar.setText("Agregar Negocio");
                botonGestionar.setText("Gestionar Negocio");
            }
        }else{
            host_navigation.setVisibility(View.GONE);
            guest_navigation.setSelectedItemId(R.id.navigationProfile);
            guest_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(rol.equals("propietarioAlojamiento")){
                    intent =new Intent(getApplicationContext(),AgregarAlojamientoActivity.class );
                }else {
                    intent = new Intent(getApplicationContext(),CrearNegocioActivity.class);
                }
                startActivity(intent);
            }
        });

        botonGestionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(view.getContext(),EditarAlojamientoActivity.class);
                startActivity(intent2);
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
                    intent = new Intent(getApplicationContext(),ExplorarActivity.class);
                    intent.putExtra("rol",rol);
                    startActivity(intent);
                    return true;

                case R.id.navigationRecord:
                    intent = new Intent(getApplicationContext(),HistorialActivity.class);
                    intent.putExtra("rol",rol);
                    startActivity(intent);
                    return true;

                case R.id.navigationProfile:
                    return true;
            }
            return false;
        }
    };

}
