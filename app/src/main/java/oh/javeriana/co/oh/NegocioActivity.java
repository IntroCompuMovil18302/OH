package oh.javeriana.co.oh;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;

public class NegocioActivity extends AppCompatActivity {

    Huesped huesped=null;
    Anfitrion anfitrion=null;
    Alojamiento alojamiento=null;
    Propietario propietario=null;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    String rol = "";
    TextView horaApertura ;
    TextView horaCierre ;
    TextView telefono;
    TextView serviciosAdicionales;
    TextView producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocio);

        horaApertura = findViewById(R.id.horaApertura);
        horaCierre = findViewById(R.id.horaCierre);
        telefono = findViewById(R.id.telefono);
        serviciosAdicionales = findViewById(R.id.serviciosAdicionales);
        producto = findViewById(R.id.producto);

        BottomNavigationView guest_navigation = (BottomNavigationView) findViewById(R.id.guest_navigation);
        BottomNavigationView host_navigation = (BottomNavigationView) findViewById(R.id.host_navigation);
    }
}
