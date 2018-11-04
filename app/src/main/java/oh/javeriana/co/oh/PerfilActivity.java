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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        botonGestionar=findViewById(R.id.gestionar);
        botonAgregar=findViewById(R.id.agregar);
       // explorar = findViewById(R.id.explorar);
       // historial = findViewById(R.id.historial);
       // perfil = findViewById(R.id.perfil);

        final String rol =  getIntent().getStringExtra("rol");

        if(rol.equals("propietarioAlojamiento")){
            botonAgregar.setText("Agregar alojamiento");
            botonGestionar.setText("Gestionar alojamientos");
            explorar.setVisibility(View.GONE);
        }else if(rol.equals("propietarioNegocio")){
            botonAgregar.setText("Agregar Negocio");
            botonGestionar.setText("Gestionar Negocio");
            explorar.setVisibility(View.GONE);
        }else{
            botonAgregar.setVisibility(View.GONE);
            botonGestionar.setVisibility(View.GONE);
        }

       /* explorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getApplicationContext(),ExplorarActivity.class );
                startActivity(intent);
            }
        });

        historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getApplicationContext(),HistorialActivity.class );
                intent.putExtra("rol",rol);
                startActivity(intent);
            }
        });

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
        });*/
        botonGestionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(view.getContext(),EditarAlojamientoActivity.class);
                startActivity(intent2);
            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigationProfile);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigationExplore:
                    intent = new Intent(getApplicationContext(),ExplorarActivity.class);
                    intent.putExtra("rol","huesped");
                    startActivity(intent);
                    return true;

                case R.id.navigationRecord:
                    intent = new Intent(getApplicationContext(),HistorialActivity.class);
                    intent.putExtra("rol","huesped");
                    startActivity(intent);
                    return true;
                //mTextMessage.setText(R.string.title_dashboard);
                //return true;
                case R.id.navigationProfile:
                    return true;
                //break;
            }
            //return false;
            return false;
        }
    };

}
