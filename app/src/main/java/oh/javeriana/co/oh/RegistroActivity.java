package oh.javeriana.co.oh;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.regex.Pattern;

public class RegistroActivity extends Activity {

    private ImageButton next;
    private ImageButton prev;
    private Button botonRegistrarse;
    private Button camara;
    private Button galeria;
    private EditText correo;
    private EditText contrasena;
    private EditText contrasena2;
    private EditText nombre;
    private EditText fechaNacimiento;
    private TextView datosAcceso;
    private TextView datosPersonales;
    private ImageView foto;
    private String rol ="";
    private Spinner genero;
    private EditText nacionalidad;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private Uri imageUri;
    private StorageReference mStorageRef;
    private ProgressDialog mProgress;
    private final int IMAGE_PICKER_REQUEST = 1;
    private final int REQUEST_IMAGE_CAPTURE = 2;
    private final int REQUEST_EXTERNAL_STORAGE = 3;
    public static final String PATH_USERS="usuarios/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        String explanation = "Es necesario usar la cámara para tomar la foto";
        tools.requestPermission(RegistroActivity.this, Manifest.permission.CAMERA, explanation, REQUEST_IMAGE_CAPTURE);
        tools.requestPermission(RegistroActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, explanation, REQUEST_EXTERNAL_STORAGE);


        database= FirebaseDatabase.getInstance();

        rol = getIntent().getStringExtra("rol");

        botonRegistrarse = (Button)findViewById(R.id.registrarme);
        galeria = (Button) findViewById(R.id.galeria);
        camara = (Button) findViewById(R.id.camara);

        next = (ImageButton)findViewById(R.id.next);
        prev = (ImageButton) findViewById(R.id.prev);
        foto = (ImageView) findViewById(R.id.foto);

        correo = (EditText)findViewById(R.id.correoElectronico);
        contrasena = (EditText) findViewById(R.id.contraseña);
        contrasena2 = (EditText) findViewById(R.id.confirmacionContraseña);

        nombre = (EditText) findViewById(R.id.nombre);
        fechaNacimiento = (EditText) findViewById(R.id.fechaNacimiento);

        genero = (Spinner) findViewById(R.id.genero);
        nacionalidad = (EditText) findViewById(R.id.nacionalidad);


        datosAcceso = (TextView) findViewById(R.id.datosAcceso);
        datosPersonales = (TextView) findViewById(R.id.datosPersonales);

        botonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mStorageRef = FirebaseStorage.getInstance().getReference();

                try {
                    myRef = database.getReference(PATH_USERS);
                    String key = myRef.push().getKey();
                    myRef = database.getReference(PATH_USERS + key);
                    String formato = "\\d{1,2}/\\d{1,2}/\\d{4}";

                    if(rol.compareTo("huesped") == 0) {
                        if(!nombre.getText().toString().isEmpty()){
                            if(!nacionalidad.getText().toString().isEmpty()){
                                if(Pattern.matches(formato,fechaNacimiento.getText())){
                                    Huesped huesped = new Huesped(key, nombre.getText().toString(), correo.getText().toString(), fechaNacimiento.getText().toString(), "", (String) genero.getSelectedItem(), nacionalidad.getText().toString());
                                    myRef.setValue(huesped);

                                    mAuth.createUserWithEmailAndPassword(correo.getText().toString(), contrasena.getText().toString());
                                    Intent intent = new Intent(getApplicationContext(),ExplorarActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(RegistroActivity.this, "La fecha debe estar en formato dd/mm/AAAA", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(RegistroActivity.this, "Su nacionalidad no puede faltar", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(RegistroActivity.this, "Su nombre no puede faltar", Toast.LENGTH_SHORT).show();
                        }



                    }
                    else if(rol.compareTo("propietarioAlojamiento") == 0) {
                        if(!nombre.getText().toString().isEmpty()){
                            if(Pattern.matches(formato,fechaNacimiento.getText())){
                                Anfitrion propAloj = new Anfitrion(key, "propietarioAlojamiento", correo.getText().toString(), nombre.getText().toString(), fechaNacimiento.getText().toString(), "");
                                myRef.setValue(propAloj);

                                mAuth.createUserWithEmailAndPassword(correo.getText().toString(), contrasena.getText().toString());
                                Intent intent = new Intent(getApplicationContext(), HistorialActivity.class);
                                intent.putExtra("usr", propAloj);
                                startActivity(intent);
                            }else {
                                Toast.makeText(RegistroActivity.this, "La fecha debe estar en formato dd/mm/AAAA", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(RegistroActivity.this, "Su nombre no puede faltar", Toast.LENGTH_SHORT).show();
                        }



                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Función no implementada", Toast.LENGTH_SHORT).show();
                    }

                    if(imageUri != null) {
                        StorageReference imagesProfile = mStorageRef.child(key).child("imageProfile");
                        imagesProfile.putFile(imageUri);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Intent intent = new Intent(getApplicationContext(),ExplorarActivity.class);
                //startActivity(intent);
            }
        });


        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String explanation = "Es necesario usar la cámara para tomar la foto";
              tools.requestPermission(RegistroActivity.this, Manifest.permission.CAMERA, explanation, REQUEST_IMAGE_CAPTURE);
                tools.requestPermission(RegistroActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, explanation, REQUEST_EXTERNAL_STORAGE);
              takePicture();
            }

            private void takePicture() {
              Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
              if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                  startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
              }
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
                    contrasena.setVisibility(View.GONE);
                    contrasena2.setVisibility(View.GONE);
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
                contrasena.setVisibility(View.VISIBLE);
                contrasena2.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);

                datosPersonales.setVisibility(View.GONE);
                nombre.setVisibility(View.GONE);
                fechaNacimiento.setVisibility(View.GONE);
                botonRegistrarse.setVisibility(View.GONE);
                camara.setVisibility(View.GONE);
                galeria.setVisibility(View.GONE);
                foto.setVisibility(View.GONE);
                prev.setVisibility(View.GONE);

                if(rol.equals("huesped")) {
                    genero.setVisibility(View.GONE);
                    nacionalidad.setVisibility(View.GONE);
                }

            }
        });
    }

    private boolean esNumero(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
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
                        imageUri = data.getData();
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
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    Log.i("URI", imageUri.toString());
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    foto.setImageBitmap(imageBitmap);
                    foto.setMaxHeight(106);
                    foto.setMaxWidth(106);
                }
                break;

            }
    }


}
