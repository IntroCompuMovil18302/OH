package oh.javeriana.co.oh;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class BuscarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int [] images = {R.drawable.alojamiento, R.drawable.negocios, R.drawable.alojamiento, R.drawable.negocios, R.drawable.alojamiento, R.drawable.negocios};
    String[] names = {"Aloja1", "Negocio1", "Aloja2", "Negocio2", "Aloja3", "Negocio3"};
    String[] prop = {"Propietario1", "Propietario2", "Propietario3", "Propietario4", "Propietario5", "Propietario6"};
    String[] descr = {"Breve descripción del ítem", "Breve descripción del ítem", "Breve descripción del ítem", "Breve descripción del ítem", "Breve descripción del ítem", "Breve descripción del ítem"};
    String[] precio = {"$ 300000", "$ 800000", "$ 1500000", "$ 480000", "$ 270000", "$ 1246000"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        ListView listView = findViewById(R.id.listVwBusq);
        CustomAdapter c = new CustomAdapter();
        listView.setAdapter(c);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), ItemActivity.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        SeekBar seekBar = findViewById(R.id.seekBar);
        final TextView seekBarValue = findViewById(R.id.txtVwValueSeek);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarValue.setText("$ " + String.valueOf(i*300000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //TODO
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.buscar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.customlayout,null);

            ImageView imageView = view.findViewById(R.id.imageView11);
            TextView txtVwName = view.findViewById(R.id.nomProd);
            TextView txtVwProp = view.findViewById(R.id.propProd);
            TextView txtPrice = view.findViewById(R.id.precioProd);
            TextView txtDesc = view.findViewById(R.id.descProd);

            imageView.setImageResource(images[i]);
            txtVwName.setText(names[i]);
            txtVwProp.setText(prop[i]);
            txtPrice.setText(precio[i]);
            txtDesc.setText(descr[i]);

            return view;
        }
    }

}

