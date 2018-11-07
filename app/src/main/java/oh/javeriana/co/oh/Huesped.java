package oh.javeriana.co.oh;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Huesped  implements Serializable {
    private String rol;
    private String email;
    private String nombre;
    private String fechaNac;
    private String foto;
    private String genero;
    private String nacionalidad;


    public Huesped() {

    }

    public Huesped(String nombre, String email, String fechaNac, String foto, String genero, String nacionalidad) throws ParseException {
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");

        this.rol = "huesped";
        this.email = email;
        this.nombre = nombre;
        this.fechaNac = fechaNac;
        this.foto = foto;
        this.genero = genero;
        this.nacionalidad = nacionalidad;
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
}
