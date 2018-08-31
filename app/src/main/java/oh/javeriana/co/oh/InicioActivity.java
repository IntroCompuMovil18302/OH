package oh.javeriana.co.oh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class InicioActivity extends AppCompatActivity {

    TextView mensajeRol = null;
    RadioGroup grupoRoles  = null;
    EditText correo  = null;
    EditText contraseña = null;
    Button iniciarSesion = null;
    Button olvidarContraseña = null;
    Button crearCuenta = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        getSupportActionBar().hide();

        mensajeRol = (TextView) findViewById(R.id.textView);
        grupoRoles = (RadioGroup) findViewById(R.id.radioGroup);
        correo = (EditText) findViewById(R.id.correoElectronico);
        contraseña  = (EditText) findViewById(R.id.contraseña);
        iniciarSesion = (Button) findViewById(R.id.iniciarSesion);
        olvidarContraseña = (Button) findViewById(R.id.olvidarContraseña);
        crearCuenta = (Button) findViewById(R.id.crearCuenta);

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correo.getText().toString().equals("huesped@oh.com") && contraseña.getText().toString().equals("1234")){
                    Intent intent = new Intent(getApplicationContext(), ExplorarActivity.class);
                    startActivity(intent);
                }else if(correo.getText().toString().equals("alojamiento@oh.com") && contraseña.getText().toString().equals("1234")) {
                    Intent intent = new Intent(getApplicationContext(), HistorialActivity.class);
                    intent.putExtra("rol", "propietarioAlojamiento");
                    startActivity(intent);
                }else if(correo.getText().toString().equals("negocio@oh.com") && contraseña.getText().toString().equals("1234")){
                    Intent intent = new Intent(getApplicationContext(), HistorialActivity.class);
                    intent.putExtra("rol", "propietarioNegocio");
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Datos invalidos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo.setVisibility(View.GONE);
                contraseña.setVisibility(View.GONE);
                iniciarSesion.setVisibility(View.GONE);
                olvidarContraseña.setVisibility(View.GONE);
                crearCuenta.setVisibility(View.GONE);

                mensajeRol.setVisibility(View.VISIBLE);
                grupoRoles.setVisibility(View.VISIBLE);
            }
        });

        grupoRoles.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
                if(checkedId==R.id.huesped){
                    intent.putExtra("rol","huesped");
                }else if(checkedId==R.id.propietarioAlojamiento){
                    intent.putExtra("rol","propietarioAlojamiento");
                }else{
                    intent.putExtra("rol","propietarioNegocio");
                }
                startActivity(intent);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            correo.setVisibility(View.VISIBLE);
            contraseña.setVisibility(View.VISIBLE);
            iniciarSesion.setVisibility(View.VISIBLE);
            olvidarContraseña.setVisibility(View.VISIBLE);
            crearCuenta.setVisibility(View.VISIBLE);

            mensajeRol.setVisibility(View.GONE);
            grupoRoles.setVisibility(View.GONE);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}