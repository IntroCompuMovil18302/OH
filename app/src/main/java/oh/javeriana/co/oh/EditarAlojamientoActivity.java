package oh.javeriana.co.oh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

public class EditarAlojamientoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_alojamiento);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(0).setCheckable(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigationRecord:
                    intent = new Intent(getApplicationContext(),HistorialActivity.class);
                    intent.putExtra("rol","propietarioAlojamiento");
                    startActivity(intent);
                    return true;
                case R.id.navigationProfile:
                    intent = new Intent(getApplicationContext(),PerfilActivity.class);
                    intent.putExtra("rol","propietarioAlojamiento");
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

}
