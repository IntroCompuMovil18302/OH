package oh.javeriana.co.oh;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class CrearNegocioActivity extends Activity {

    EditText nombreNegocio = null;
    TextView horaApertura = null;
    TextView horaAperturaLabel = null;
    TextView horaCierre = null;
    TextView horaCierreLabel = null;
    EditText telefono = null;
    //EditText direccion = null;
    TextView tipo = null;
    RadioGroup grupoTipos = null;

    TextView servicioAdicional = null;
    RadioGroup opciones = null;

    TextView domicilios = null;
    RadioGroup opcionesDomicilio = null;

    TextView agregarProductos = null;
    EditText producto = null;
    Button agregar = null;
    Button publicar = null;

    ImageButton botonHoraApertura = null;
    ImageButton botonHoraCierre = null;

    Propietario propietario = null;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private DatabaseReference myRef;

    private int tipoNegocio;
    private boolean servicioAdicionalNegocio;
    private boolean domiciliosNegocio;
    private String catalogoNegocio ="";

    public static final String PATH_NEGOCIOS="negocios/";

    String rol = "";
    public final Calendar c = Calendar.getInstance();
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_negocio);

        propietario = (Propietario) getIntent().getSerializableExtra("usr");

        database= FirebaseDatabase.getInstance();

        nombreNegocio = (EditText) findViewById(R.id.nombreNegocio);
        horaAperturaLabel = (TextView) findViewById(R.id.textView15);
        horaApertura = (TextView) findViewById(R.id.horaApertura);
        horaCierreLabel = (TextView) findViewById(R.id.textView18);
        horaCierre = (TextView) findViewById(R.id.horaCierre);
        telefono = (EditText) findViewById(R.id.telefono);
        //direccion = (EditText) findViewById(R.id.direccion);
        tipo = (TextView) findViewById(R.id.tipo);
        grupoTipos = (RadioGroup) findViewById(R.id.tiposNegocio);

        servicioAdicional = (TextView) findViewById(R.id.servicioAdicional);
        opciones = (RadioGroup) findViewById(R.id.opciones);

        domicilios = (TextView) findViewById(R.id.domicilios);
        opcionesDomicilio = (RadioGroup) findViewById(R.id.opcionesDomicilio);

        agregarProductos = (TextView) findViewById(R.id.agregarProductos);
        producto = (EditText) findViewById(R.id.producto);
        agregar =(Button) findViewById(R.id.agregar);
        publicar = (Button) findViewById(R.id.publicar);

        botonHoraApertura = findViewById(R.id.botonHoraApertura);
        botonHoraCierre = findViewById(R.id.botonHoraCierre);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(0).setCheckable(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        rol = getIntent().getSerializableExtra("usr").getClass().getName();
        Log.i("ROL", rol);
        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Propietario") == 0) {
            propietario = (Propietario) getIntent().getSerializableExtra("usr");
        }

        botonHoraApertura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerHora(1);
            }
        });

        botonHoraCierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerHora(2);
            }
        });

        opciones.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.si){
                    servicioAdicionalNegocio = true;
                }else{
                    servicioAdicionalNegocio = false;
                }
            }
        });

        opcionesDomicilio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.siDomicilio){
                    domiciliosNegocio = true;
                }else{
                    domiciliosNegocio = false;
                }
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catalogoNegocio = catalogoNegocio.concat(producto.getText().toString());
                catalogoNegocio = catalogoNegocio.concat(",");
                //Toast.makeText(getApplicationContext(),"Cadena va en: "+catalogoNegocio,Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),producto.getText().toString()+ " agregado",Toast.LENGTH_LONG).show();
                producto.setText("");
            }
        });

        grupoTipos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                nombreNegocio.setVisibility(View.GONE);
                horaAperturaLabel.setVisibility(View.GONE);
                horaApertura.setVisibility(View.GONE);
                horaCierreLabel.setVisibility(View.GONE);
                horaCierre.setVisibility(View.GONE);
                telefono.setVisibility(View.GONE);
//                direccion.setVisibility(View.GONE);
                tipo.setVisibility(View.GONE);
                grupoTipos.setVisibility(View.GONE);
                botonHoraCierre.setVisibility(View.GONE);
                botonHoraApertura.setVisibility(View.GONE);

                if(checkedId== R.id.cafeteria){
                    tipoNegocio = 1;
                    //Toast.makeText()
                    //Toast.makeText(getApplicationContext(),"Tipo Negocio "+tipoNegocio,Toast.LENGTH_LONG).show();
                    agregarProductos.setText("Agrega los platos del menú");
                    servicioAdicional.setHint("¿Ofrecen servicio de wifi?");
                    producto.setHint("Plato");
                }else if(checkedId== R.id.drogueria){
                    tipoNegocio =2;
                    agregarProductos.setText("Agrega los productos que vendes");
                    servicioAdicional.setHint("¿Ofrecen inyectologia?");
                    producto.setHint("Producto");
                }else if(checkedId== R.id.restaurante){
                    tipoNegocio = 3;
                    agregarProductos.setText("Agrega los platos del menú");
                    servicioAdicional.setHint("¿Tienen menu infantil?");
                    producto.setHint("Plato");
                }else{
                    tipoNegocio =4;
                    agregarProductos.setText("Agrega los productos que vendes");
                    servicioAdicional.setHint("¿Reciben tarjeta de crédito?");
                    producto.setHint("Producto");
                }

                agregarProductos.setVisibility(View.VISIBLE);
                producto.setVisibility(View.VISIBLE);
                agregar.setVisibility(View.VISIBLE);

                servicioAdicional.setVisibility(View.VISIBLE);
                opciones.setVisibility(View.VISIBLE);
                domicilios.setVisibility(View.VISIBLE);
                opcionesDomicilio.setVisibility(View.VISIBLE);



                publicar.setVisibility(View.VISIBLE);

                BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                navigation.getMenu().getItem(0).setCheckable(false);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                publicar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAuth = FirebaseAuth.getInstance();
                        mStorageRef = FirebaseStorage.getInstance().getReference();
                        myRef = database.getReference(PATH_NEGOCIOS);
                        /*Geocoder mGeocoder = new Geocoder(getBaseContext());*/
                        String key = myRef.push().getKey();
                        myRef = database.getReference(PATH_NEGOCIOS + key);
                                                           /* LatLng position=null;
                                                            try {
                                                                List<Address> addresses = mGeocoder.getFromLocationName(ubicacionET.getText().toString(), 2, ABAJOIZQLAT, ABAJOIZQLONG, ARRIBADERLAT, ARRIBADERLONG);
                                                                if (addresses != null && !addresses.isEmpty()) {

                                                                    Address addressResult = addresses.get(0);
                                                                    position = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                                                                    Log.i("LATITUD", String.valueOf(position.latitude));
                                                                    Log.i("LONGITUD", String.valueOf(position.longitude));
                                                                }

                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }*/

                         Negocio negocio = new Negocio(nombreNegocio.getText().toString(),horaApertura.getText().toString(),horaCierre.getText().toString(),telefono.getText().toString(),tipoNegocio,"carrera 7 # 40",1.2,2.3,propietario.getId(),catalogoNegocio,servicioAdicionalNegocio,domiciliosNegocio);
                         myRef.setValue(negocio);

                                                           /* for(int i=0; i<fotos.length; i++){
                                                                if(imageUri[i] != null) {
                                                                    StorageReference imagesProfile = mStorageRef.child(anfitrion.getId()).child( key + "/image" + (i+1));
                                                                    imagesProfile.putFile(imageUri[i]);
                                                                }
                                                            }*/

                         Toast.makeText(CrearNegocioActivity.this, "Negocio creado exitosamente", Toast.LENGTH_SHORT).show();
                         CrearNegocioActivity.this.finish();
                    }
                });
            }
        });
    }

    private void obtenerHora(final int codigo){
        Log.i("HORA: ","AQUI");
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormateada =  (hourOfDay < 10)? String.valueOf("0" + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 10)? String.valueOf("0" + minute):String.valueOf(minute);
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                if(codigo==1){
                    Log.i("HORA: ","AQUI");
                    horaApertura.setText(horaFormateada + ":" + minutoFormateado + " " + AM_PM);
                }

                else{
                    horaCierre.setText(horaFormateada + ":" + minutoFormateado + " " + AM_PM);
                }


            }
        }, hora, minuto, false);

        Log.i("HOLA","HOLA");

        recogerHora.show();
        Log.i("HOLA","HOLA");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && nombreNegocio.getVisibility()== View.GONE ) {
            nombreNegocio.setVisibility(View.VISIBLE);
            horaAperturaLabel.setVisibility(View.VISIBLE);
            horaApertura.setVisibility(View.VISIBLE);
            horaCierreLabel.setVisibility(View.VISIBLE);
            horaCierre.setVisibility(View.VISIBLE);
            telefono.setVisibility(View.VISIBLE);
            //direccion.setVisibility(View.VISIBLE);
            tipo.setVisibility(View.VISIBLE);
            grupoTipos.setVisibility(View.VISIBLE);
            botonHoraCierre.setVisibility(View.VISIBLE);
            botonHoraApertura.setVisibility(View.VISIBLE);

            agregarProductos.setVisibility(View.GONE);
            producto.setVisibility(View.GONE);
            agregar.setVisibility(View.GONE);

            servicioAdicional.setVisibility(View.GONE);
            opciones.setVisibility(View.GONE);

            domicilios.setVisibility(View.GONE);
            opcionesDomicilio.setVisibility(View.GONE);
            publicar.setVisibility(View.GONE);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigationRecord:
                    intent = new Intent(getApplicationContext(),HistorialActivity.class);
                    //intent.putExtra("rol","propietarioAlojamiento");
                    if (propietario != null)
                        intent.putExtra("usr", propietario);
                    startActivity(intent);
                    return true;
                case R.id.navigationProfile:
                    intent = new Intent(getApplicationContext(),PerfilActivity.class);
                    // intent.putExtra("rol","propietarioAlojamiento");
                    if (propietario != null)
                        intent.putExtra("usr", propietario);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
}
