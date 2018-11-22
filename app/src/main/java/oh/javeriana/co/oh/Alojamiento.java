package oh.javeriana.co.oh;

import android.location.Geocoder;
import android.text.Editable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Alojamiento implements Serializable {
    private String tipo;
    private String nombre;
    private String descripcion;
    private double latitud;
    private double longitud;
    private double valorNoche;
    private int cantHuespedes;
    private String direccion;
    private String idUsuario;
    private String fechaInicial;
    private String fechaFinal;

    public Alojamiento() {
    }

    public Alojamiento(String nombre, String descripcion, String direccion, int cantHuespedes, double valorNoche, String idUsuario,
                String tipoAlojamiento, double latitud, double longitud, String fechaInicial, String fechaFinal) {
        //this.idAloj = idAloj;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantHuespedes = cantHuespedes;
        this.valorNoche = valorNoche;
        this.direccion = direccion;
        this.idUsuario = idUsuario;
        this.tipo = tipoAlojamiento;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaInicial=fechaInicial;
        this.fechaFinal=fechaFinal;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getLongitud() {
        return longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getValorNoche() {
        return valorNoche;
    }

    public int getCantHuespedes() {
        return cantHuespedes;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setValorNoche(double valorNoche) {
        this.valorNoche = valorNoche;
    }

    public void setCantHuespedes(int cantHuespedes) {
        this.cantHuespedes = cantHuespedes;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setIdUsuario(String nombreUsuario) {
        this.idUsuario = nombreUsuario;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    @Exclude
    public Date getFechaInicialDate() throws ParseException {
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        Date date =format.parse(fechaInicial);
        return date;
    }

    @Exclude
    public Date getFechaFinalDate() throws ParseException {
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        Date date =format.parse(fechaFinal);
        return date;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

}