package oh.javeriana.co.oh;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity {

    Huesped huesped=null;
    Anfitrion anfitrion=null;

    String rol = "";
    Button button ;
    LinearLayout layout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getSupportActionBar().hide();

        //rol =  getIntent().getStringExtra("rol");
        button = findViewById(R.id.button);

        layout = findViewById(R.id.CommentUser);


        BottomNavigationView guest_navigation = (BottomNavigationView) findViewById(R.id.guest_navigation);
        BottomNavigationView host_navigation = (BottomNavigationView) findViewById(R.id.host_navigation);

        rol = getIntent().getSerializableExtra("usr").getClass().getName();
        Log.i("ROL", rol);
        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") == 0) {
            huesped = (Huesped) getIntent().getSerializableExtra("usr");
        }else{
            anfitrion = (Anfitrion) getIntent().getSerializableExtra("usr");
        }


        if(rol.compareToIgnoreCase("oh.javeriana.co.oh.Huesped") != 0){
            button.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
            guest_navigation.setVisibility(View.GONE);
            host_navigation.getMenu().getItem(0).setCheckable(false);
            host_navigation.setSelectedItemId(R.id.navigationRecord);
            host_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }else{
            host_navigation.setVisibility(View.GONE);
            guest_navigation.getMenu().getItem(1).setCheckable(false);
            guest_navigation.setSelectedItemId(R.id.navigationRecord);
            guest_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onClick(View v){
        Intent intent = new Intent(getApplicationContext(),ReservarActivity.class);
        if(anfitrion != null)
            intent.putExtra("usr", anfitrion);
        else if (huesped != null)
            intent.putExtra("usr", huesped);
        startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigationExplore:
                    intent = new Intent(getApplicationContext(),MapaActivity.class);
                    if(anfitrion != null)
                        intent.putExtra("usr", anfitrion);
                    else if (huesped != null)
                        intent.putExtra("usr", huesped);
                    startActivity(intent);
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
