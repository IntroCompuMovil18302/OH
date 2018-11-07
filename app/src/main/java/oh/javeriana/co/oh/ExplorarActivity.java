package oh.javeriana.co.oh;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ExplorarActivity extends AppCompatActivity {

    Huesped huesped=null;
    Anfitrion anfitrion=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        EditText search = findViewById(R.id.txtInBuscar);

        getSupportActionBar().hide();

        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    onClickSearch(view);
                    return true;
                }
                return false;
            }
        });
    }

    protected void onClickItem(View v) {
        Intent intent = new Intent(v.getContext(), ItemActivity.class);
        intent.putExtra("rol","huesped");
        startActivity(intent);
    }

    protected void onClickSearch(View v) {
        Intent intent = new Intent(v.getContext(), BuscarActivity.class);
        startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;

            String rol = getIntent().getSerializableExtra("usr").getClass().getName();
            if(rol.compareToIgnoreCase("huesped") == 0) {
                huesped = (Huesped) getIntent().getSerializableExtra("usr");
            }
            else if(rol.compareToIgnoreCase("propietarioAlojamiento") == 0) {
                anfitrion = (Anfitrion) getIntent().getSerializableExtra("usr");
            }

            switch (item.getItemId()) {
                case R.id.navigationExplore:
                    return true;
                case R.id.navigationRecord:
                    intent = new Intent(getApplicationContext(),HistorialActivity.class);

                    if(anfitrion != null)
                        intent.putExtra("usr", anfitrion);
                    else if (huesped != null)
                        intent.putExtra("usr", huesped);
                    startActivity(intent);

                    return true;
                case R.id.navigationProfile:
                    intent = new Intent(getApplicationContext(),PerfilActivity.class);

                    if(anfitrion != null)
                        intent.putExtra("usr", anfitrion);
                    else if (huesped != null)
                        intent.putExtra("usr", huesped);

                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
}
