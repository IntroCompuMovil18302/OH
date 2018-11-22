package oh.javeriana.co.oh;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Propietario  implements Serializable {

    //private String id;
    private String rol;
    private String email;
    private String nombre;
    private String fechaNac;
    private String foto;

    public Propietario() {
    }


    //public Propietario(String id, String rol, String email, String nombre, String fechaNac, String foto) {
    public Propietario(String rol, String email, String nombre, String fechaNac, String foto) {
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");

        //this.id = id;
        this.rol = rol;
        this.email = email;
        this.nombre = nombre;
        this.fechaNac = fechaNac;
        this.foto = foto;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    @Exclude
    public long getEdad() throws ParseException {
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");

        Calendar start = Calendar.getInstance();
        start.setTime(format.parse(fechaNac));
        long end = Calendar.getInstance().getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start.getTimeInMillis()))/365;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
