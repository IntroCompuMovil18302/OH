package oh.javeriana.co.oh;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegistroActivity extends Activity {

    private ImageButton next;
    private ImageButton prev;
    private Button botonRegistrarse;
    private Button camara;
    private Button galeria;
    private EditText correo;
    private EditText contraseña;
    private EditText contraseña2;
    private EditText nombre;
    private EditText fechaNacimiento;
    private TextView datosAcceso;
    private TextView datosPersonales;
    private ImageView foto;
    private String rol ="";
    private final int IMAGE_PICKER_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        rol = getIntent().getStringExtra("rol");

        botonRegistrarse = (Button)findViewById(R.id.registrarme);
        galeria = (Button) findViewById(R.id.galeria);
        camara = (Button) findViewById(R.id.camara);

        next = (ImageButton)findViewById(R.id.next);
        prev = (ImageButton) findViewById(R.id.prev);
        foto = (ImageView) findViewById(R.id.foto);

        correo = (EditText)findViewById(R.id.correoElectronico);
        contraseña = (EditText) findViewById(R.id.contraseña);
        contraseña2 = (EditText) findViewById(R.id.confirmacionContraseña);

        nombre = (EditText) findViewById(R.id.nombre);
        fechaNacimiento = (EditText) findViewById(R.id.fechaNacimiento);

        datosAcceso = (TextView) findViewById(R.id.datosAcceso);
        datosPersonales = (TextView) findViewById(R.id.datosPersonales);

        botonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ExplorarActivity.class);
                startActivity(intent);
            }
        });

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImage = new Intent(Intent.ACTION_PICK);
                pickImage.setType("image/*");
                startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){

                    datosAcceso.setVisibility(View.GONE);
                    correo.setVisibility(View.GONE);
                    contraseña.setVisibility(View.GONE);
                    contraseña2.setVisibility(View.GONE);
                    next.setVisibility(View.GONE);

                    datosPersonales.setVisibility(View.VISIBLE);
                    nombre.setVisibility(View.VISIBLE);
                    fechaNacimiento.setVisibility(View.VISIBLE);
                    botonRegistrarse.setVisibility(View.VISIBLE);
                    camara.setVisibility(View.VISIBLE);
                    galeria.setVisibility(View.VISIBLE);
                    foto.setVisibility(View.VISIBLE);
                    prev.setVisibility(View.VISIBLE);

                    if(rol.equals("huesped")){
                        Spinner genero = (Spinner) findViewById(R.id.genero);
                        EditText nacionalidad = (EditText) findViewById(R.id.nacionalidad);
                        genero.setVisibility(View.VISIBLE);
                        nacionalidad.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datosAcceso.setVisibility(View.VISIBLE);
                correo.setVisibility(View.VISIBLE);
                contraseña.setVisibility(View.VISIBLE);
                contraseña2.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);

                datosPersonales.setVisibility(View.GONE);
                nombre.setVisibility(View.GONE);
                fechaNacimiento.setVisibility(View.GONE);
                botonRegistrarse.setVisibility(View.GONE);
                camara.setVisibility(View.GONE);
                galeria.setVisibility(View.GONE);
                foto.setVisibility(View.GONE);
                prev.setVisibility(View.GONE);

                if(rol.equals("huesped")){
                    Spinner genero = (Spinner) findViewById(R.id.genero);
                    EditText nacionalidad = (EditText) findViewById(R.id.nacionalidad);
                    genero.setVisibility(View.GONE);
                    nacionalidad.setVisibility(View.GONE);
                }

            }
        });
    }

    public boolean validarCampos(){
        return true;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case IMAGE_PICKER_REQUEST:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        foto.setImageBitmap(selectedImage);
                        //  LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(167, 185);
                        //image.setLayoutParams(params);
                        foto.setMaxHeight(106);
                        foto.setMaxWidth(106);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }


}
