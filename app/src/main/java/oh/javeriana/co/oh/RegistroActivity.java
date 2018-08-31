package oh.javeriana.co.oh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegistroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        String rol = getIntent().getStringExtra("rol");
        if(rol.equals("huesped")){
            Spinner genero = (Spinner) findViewById(R.id.genero);
            EditText nacionalidad = (EditText) findViewById(R.id.nacionalidad);
            genero.setVisibility(View.VISIBLE);
            nacionalidad.setVisibility(View.VISIBLE);

        }

        Button botonRegistrarse = findViewById(R.id.registrarme);
        botonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ExplorarActivity.class);
                startActivity(intent);
            }
        });
    }

}
