package oh.javeriana.co.oh;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;

public class CrearNegocioActivity extends Activity {

    EditText nombreNegocio = null;
    EditText horaApertura = null;
    EditText horaCierre = null;
    EditText telefono = null;
    EditText direccion = null;
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

    Propietario propietario = null;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private DatabaseReference myRef;

    public static final String PATH_NEGOCIOS="negocios/";

    String rol = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_negocio);

        propietario = (Propietario) getIntent().getSerializableExtra("usr");

        database= FirebaseDatabase.getInstance();

        nombreNegocio = (EditText) findViewById(R.id.nombreNegocio);
        horaApertura = (EditText) findViewById(R.id.horaApertura);
        horaCierre = (EditText) findViewById(R.id.horaCierre);
        telefono = (EditText) findViewById(R.id.telefono);
        direccion = (EditText) findViewById(R.id.direccion);
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(0).setCheckable(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        rol = getIntent().getSerializableExtra("usr").getClass().getName();
        Log.i("ROL", rol);
        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Propietario") == 0) {
            propietario = (Propietario) getIntent().getSerializableExtra("usr");
        }

        grupoTipos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                nombreNegocio.setVisibility(View.GONE);
                horaApertura.setVisibility(View.GONE);
                horaCierre.setVisibility(View.GONE);
                telefono.setVisibility(View.GONE);
                direccion.setVisibility(View.GONE);
                tipo.setVisibility(View.GONE);
                grupoTipos.setVisibility(View.GONE);

                if(checkedId== R.id.cafeteria){
                    agregarProductos.setText("Agrega los platos del menú");
                    servicioAdicional.setHint("¿Ofrecen servicio de wifi?");
                    producto.setHint("Plato");
                }else if(checkedId== R.id.drogueria){
                    agregarProductos.setText("Agrega los productos que vendes");
                    servicioAdicional.setHint("¿Ofrecen inyectologia?");
                    producto.setHint("Producto");
                }else if(checkedId== R.id.restaurante){
                    agregarProductos.setText("Agrega los platos del menú");
                    servicioAdicional.setHint("¿Tienen menu infantil?");
                    producto.setHint("Plato");
                }else{
                    agregarProductos.setText("Agrega los productos que vendes");
                    servicioAdicional.setHint("¿Reciben tarjeta de crédito?");
                    producto.setHint("Producto");
                }

                agregarProductos.setVisibility(View.VISIBLE);
                producto.setVisibility(View.VISIBLE);
                agregar.setVisibility(View.VISIBLE);

                servicioAdicional.setVisibility(View.VISIBLE);
                opciones.setVisibility(View.VISIBLE);

                if(checkedId!= R.id.cafeteria){
                    domicilios.setVisibility(View.VISIBLE);
                    opcionesDomicilio.setVisibility(View.VISIBLE);
                }

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

/*
                        if(!nombreET.getText().toString().isEmpty()) {
                            if(!tools.esNumero(String.valueOf(nombreET.getText()))){
                                if (!descripcionET.getText().toString().isEmpty()){
                                    if (!tools.esNumero(String.valueOf(descripcionET.getText()))){
                                        if (!precioET.getText().toString().isEmpty()){
                                            if(tools.esNumero(String.valueOf(precioET.getText()))){
                                                if (!ubicacionET.getText().toString().isEmpty()){
                                                    if (!cantHuespedesET.getText().toString().isEmpty()){
                                                        if (tools.esNumero(String.valueOf(cantHuespedesET.getText()))){
                                                            Geocoder mGeocoder = new Geocoder(getBaseContext());
                                                            String key = myRef.push().getKey();
                                                            myRef = database.getReference(PATH_ALOJAMIENTOS + key);
                                                            LatLng position=null;
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
                                                            }

                                                            int cant = Integer.parseInt(cantHuespedesET.getText().toString());
                                                            double precio = Double.parseDouble( precioET.getText().toString());
                                                            double latitud = position.latitude;
                                                            double longitud = position.longitude;


                                                            Alojamiento alojamiento = new Alojamiento(nombreET.getText().toString(), descripcionET.getText().toString(), ubicacionET.getText().toString(),
                                                                    cant, precio, anfitrion.getId(), tipoAlojamiento, latitud, longitud, fechaInicial.getText().toString(), fechaFinal.getText().toString() );
                                                            myRef.setValue(alojamiento);

                                                            for(int i=0; i<fotos.length; i++){
                                                                if(imageUri[i] != null) {
                                                                    StorageReference imagesProfile = mStorageRef.child(anfitrion.getId()).child( key + "/image" + (i+1));
                                                                    imagesProfile.putFile(imageUri[i]);
                                                                }
                                                            }

                                                            Toast.makeText(AgregarAlojamientoActivity.this, "Alojamiento creado exitosamente", Toast.LENGTH_SHORT).show();
                                                            AgregarAlojamientoActivity.this.finish();
                                                        }else{
                                                            Toast.makeText(AgregarAlojamientoActivity.this, "La cantidad de huéspedes corresponde a un valor numérico", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }else{
                                                        Toast.makeText(AgregarAlojamientoActivity.this, "La cantidad de huéspedes no puede ser vacío", Toast.LENGTH_SHORT).show();
                                                    }
                                                }else{
                                                    Toast.makeText(AgregarAlojamientoActivity.this, "La ubicación del alojamiento no puede ser vacío", Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                Toast.makeText(AgregarAlojamientoActivity.this, "El precio del alojamiento corresponde a un valor numérico", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(AgregarAlojamientoActivity.this, "El precio del alojamiento no puede ser vacío", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(AgregarAlojamientoActivity.this, "La descripción del alojamiento no puede ser un valor numérico", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(AgregarAlojamientoActivity.this, "La descripción del alojamiento no puede ser vacía", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(AgregarAlojamientoActivity.this, "El nombre del alojamiento no puede ser un valor numérico", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(AgregarAlojamientoActivity.this, "El nombre del alojamiento no puede estar vacío", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && nombreNegocio.getVisibility()== View.GONE ) {
            nombreNegocio.setVisibility(View.VISIBLE);
            horaApertura.setVisibility(View.VISIBLE);
            horaCierre.setVisibility(View.VISIBLE);
            telefono.setVisibility(View.VISIBLE);
            direccion.setVisibility(View.VISIBLE);
            tipo.setVisibility(View.VISIBLE);
            grupoTipos.setVisibility(View.VISIBLE);

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
