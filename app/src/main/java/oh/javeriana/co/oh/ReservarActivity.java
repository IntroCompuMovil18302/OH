package oh.javeriana.co.oh;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
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
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;


public class ReservarActivity extends AppCompatActivity {

    Huesped huesped = null;
    Alojamiento alojamiento=null;

    CalendarView calendarView;
    TextView fechaInicial;
    ImageButton botonFechaInicial;
    TextView fechaFinal;
    ImageButton botonFechaFinal;
    Button botonReservar;
    String rol = "";
    String pathImg = "";
    String idUsr;

    public final Calendar c = Calendar.getInstance();
    private final int mes = c.get(Calendar.MONTH);
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int anio = c.get(Calendar.YEAR);


    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private DatabaseReference myRef;

    public static final String PATH_RESERVAS="reservas/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        fechaInicial = (TextView) findViewById(R.id.fechaInicial);
        fechaFinal = (TextView) findViewById(R.id.fechaFinal);
        botonFechaInicial= (ImageButton) findViewById(R.id.botonFechaInicial);
        botonFechaFinal= (ImageButton) findViewById(R.id.botonFechaFinal);
        botonReservar = (Button) findViewById(R.id.botonReservar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(0).setCheckable(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        rol = getIntent().getExtras().getSerializable("usr").getClass().getName();
        idUsr = (String) getIntent().getExtras().getString("idUsr");

        Log.i("ROL", rol);
        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") == 0) {
            huesped = (Huesped) getIntent().getExtras().getSerializable("usr");
        }

        alojamiento = (Alojamiento) getIntent().getExtras().getSerializable("alojamiento");
        final String idAloj = (String) getIntent().getExtras().getString("idAloj");
        pathImg = alojamiento.getIdUsuario() + "/" + idAloj + "/";

       botonFechaInicial.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               obtenerFecha(1);
           }
       });

        botonFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha(2);
            }
        });

        botonReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mStorageRef = FirebaseStorage.getInstance().getReference();
                myRef = database.getReference(PATH_RESERVAS);
                String key = myRef.push().getKey();
                myRef = database.getReference(PATH_RESERVAS + key);

                Reserva reserva = new Reserva(idAloj, Calendar.getInstance().getTime().toString(),fechaInicial.getText().toString(),fechaFinal.getText().toString(),false);
                myRef.setValue(reserva);

                ReservarActivity.this.finish();
            }
        });

    }


    public void obtenerFecha(final int codigo) {

        DatePickerDialog.OnDateSetListener dateList = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);
                if (codigo == 1)
                    fechaInicial.setText(diaFormateado + "/" + mesFormateado + "/" + year);
                else
                    fechaFinal.setText(diaFormateado + "/" + mesFormateado + "/" + year);
            }
        };

        DatePickerDialog recogerFecha = new DatePickerDialog(this, dateList, anio, mes, dia);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recogerFecha.setOnDateSetListener(dateList);
        }

        recogerFecha.show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;

            String rol = getIntent().getSerializableExtra("usr").getClass().getName();
            Log.i("ROL", rol);
            if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") == 0) {
                huesped = (Huesped) getIntent().getSerializableExtra("usr");
            }


            switch (item.getItemId()) {
                case R.id.navigationExplore:
                    intent = new Intent(getApplicationContext(),MapaActivity.class);
                    if (huesped != null)
                        intent.putExtra("usr", huesped);
                   // intent.putExtra("rol","huesped");
                    startActivity(intent);
                    return true;
                case R.id.navigationRecord:
                    intent = new Intent(getApplicationContext(),HistorialActivity.class);
                    //intent.putExtra("rol","huesped");
                    if (huesped != null)
                        intent.putExtra("usr", huesped);
                    startActivity(intent);
                    return true;
                case R.id.navigationProfile:
                    intent = new Intent(getApplicationContext(),PerfilActivity.class);
                    //intent.putExtra("rol","huesped");
                    if (huesped != null)
                        intent.putExtra("usr", huesped);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

}
