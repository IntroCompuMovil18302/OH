package oh.javeriana.co.oh;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class InicioActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;


    TextView mensajeRol = null;
    RadioGroup grupoRoles  = null;
    EditText correo  = null;
    EditText contraseña = null;
    Button iniciarSesion = null;
    Button olvidarContraseña = null;
    Button crearCuenta = null;

    /*@Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }*/

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void sessionUser() {
        myRef = database.getReference("usuarios");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    //usr = singleSnapshot.getValue(Huesped.class);
                    Log.i("sngle", singleSnapshot.getValue().toString());
                    String mail = singleSnapshot.child("email").getValue().toString();
                    if(mail.compareToIgnoreCase(user.getEmail()) == 0) {
                        String rol = singleSnapshot.child("rol").getValue().toString();

                        Bundle bundle = new Bundle();
                        bundle.putString("idUsr", singleSnapshot.getKey());

                        if (rol.compareTo("huesped") == 0) {
                            Huesped usr  = singleSnapshot.getValue(Huesped.class);
                            bundle.putSerializable("usr", usr);

                            Intent intent = new Intent(getApplicationContext(), MapaActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        } else if (rol.compareTo("propietarioAlojamiento") == 0) {
                            Anfitrion usr  = singleSnapshot.getValue(Anfitrion.class);
                            bundle.putSerializable("usr", usr);

                            Intent intent = new Intent(getApplicationContext(), HistorialActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                        else if (rol.compareTo("propietarioNegocio") == 0) {
                            Propietario usr  = singleSnapshot.getValue(Propietario.class);
                            bundle.putSerializable("usr", usr);

                            Intent intent = new Intent(getApplicationContext(), HistorialActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Función no implementada", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ERROR", "error en la consulta", databaseError.toException());
            }
        });
    }

    public void loadUsers() {
        myRef = database.getReference("usuarios");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    //usr = singleSnapshot.getValue(Huesped.class);
                    Log.i("sngle", singleSnapshot.getValue().toString());
                    String mail = singleSnapshot.child("email").getValue().toString();
                    if(mail.compareToIgnoreCase(user.getEmail()) == 0) {
                        String rol = singleSnapshot.child("rol").getValue().toString();

                        Bundle bundle = new Bundle();
                        bundle.putString("idUsr", singleSnapshot.getKey());

                        if (rol.compareTo("huesped") == 0) {
                            Huesped usr  = singleSnapshot.getValue(Huesped.class);
                            bundle.putSerializable("usr", usr);

                            Intent intent = new Intent(getApplicationContext(), MapaActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        } else if (rol.compareTo("propietarioAlojamiento") == 0) {
                            Anfitrion usr  = singleSnapshot.getValue(Anfitrion.class);
                            bundle.putSerializable("usr", usr);

                            Intent intent = new Intent(getApplicationContext(), HistorialActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        } else if (rol.compareTo("propietarioNegocio") == 0) {
                            Propietario usr  = singleSnapshot.getValue(Propietario.class);
                            bundle.putSerializable("usr", usr);

                            Intent intent = new Intent(getApplicationContext(), HistorialActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        } else
                            Toast.makeText(getApplicationContext(), "Función no implementada", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ERROR", "error en la consulta", databaseError.toException());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        database= FirebaseDatabase.getInstance();

        getSupportActionBar().hide();

        mensajeRol = (TextView) findViewById(R.id.textView);
        grupoRoles = (RadioGroup) findViewById(R.id.radioGroup);
        correo = (EditText) findViewById(R.id.correoElectronico);
        contraseña  = (EditText) findViewById(R.id.contraseña);
        iniciarSesion = (Button) findViewById(R.id.iniciarSesion);
        olvidarContraseña = (Button) findViewById(R.id.olvidarContraseña);
        crearCuenta = (Button) findViewById(R.id.crearCuenta);


        mAuth = FirebaseAuth.getInstance();

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()) {
                    String email = correo.getText().toString();
                    String password = contraseña.getText().toString();

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(InicioActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user = mAuth.getCurrentUser();
                                        loadUsers();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Datos invalidos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
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
                if(checkedId == R.id.huesped){
                    intent.putExtra("rol","huesped");
                }else if(checkedId == R.id.propietarioAlojamiento){
                    intent.putExtra("rol","propietarioAlojamiento");
                }else{
                    intent.putExtra("rol","propietarioNegocio");
                }
                startActivity(intent);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i("State", "Ciclo");
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    loadUsers();
                } else {
                    // User is signed out
                    //startActivity(new Intent(getApplicationContext(), InicioActivity.class));
                }
            }
        };

    }

    private boolean validateForm() {
        boolean valid = true;
        String email = correo.getText().toString();
        if (TextUtils.isEmpty(email)) {
            correo.setError("Campo obligatorio");
            valid = false;
        } else {
            correo.setError(null);
        }
        String password = contraseña.getText().toString();
        if (TextUtils.isEmpty(password)) {
            contraseña.setError("Campo obligatorio");
            valid = false;
        } else {
            contraseña.setError(null);
        }
        return valid;
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