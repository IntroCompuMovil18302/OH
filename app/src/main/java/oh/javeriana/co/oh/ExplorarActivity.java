package oh.javeriana.co.oh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ExplorarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorar);

        EditText search = findViewById(R.id.txtInBuscar);
        Button botonExplorar = findViewById(R.id.explorar);
        Button botonHistorial = findViewById(R.id.historial);
        Button botonPerfil = findViewById(R.id.perfil);

        getSupportActionBar().hide();

        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    onClickSearch(view);
                    return true;
                }
                return false;
            }
        });

        botonHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HistorialActivity.class);
                intent.putExtra("rol","huesped");
                startActivity(intent);
            }
        });

        botonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PerfilActivity.class);
                intent.putExtra("rol","huesped");
                startActivity(intent);
            }
        });
    }

    protected void onClickItem(View v) {
        Intent intent = new Intent(v.getContext(), ItemActivity.class);
        startActivity(intent);
    }

    protected void onClickSearch(View v) {
        Intent intent = new Intent(v.getContext(), BuscarActivity.class);
        startActivity(intent);
    }
}
