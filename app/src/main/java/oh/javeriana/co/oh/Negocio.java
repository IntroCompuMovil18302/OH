package oh.javeriana.co.oh;

import android.util.Log;

import java.io.Serializable;
import java.sql.Time;

public class Negocio implements Serializable {
    private String nombre;
    private String horaApertura;
    private String horaCierre;
    private String telefono;
    private int tipo;
    private String direccion;
    private double latitud;
    private double longitud;
    private String idPropietario;
    private String catalogo;
    private boolean servicioAdicional;
    private boolean domicilios;
    private String foto;


    public Negocio(){

    }

    public Negocio(String nombre, String horaApertura, String horaCierre, String telefono, int tipo, String direccion, double latitud, double longitud, String idPropietario, String catalogo, boolean servicioAdicional, boolean domicilios,String foto) {
        this.nombre = nombre;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
        this.telefono = telefono;
        this.tipo = tipo;
        this.direccion = direccion;
        this.longitud = longitud;
        this.latitud = latitud;
        this.idPropietario = idPropietario;
        this.catalogo = catalogo;
        this.servicioAdicional = servicioAdicional;
        this.domicilios = domicilios;
        this.foto = foto;

        Log.i("PRODUCTO", "AQUI");
    }

    public String getNombre() {
        return nombre;
    }

    public String getHoraApertura() {
        return horaApertura;
    }

    public String getHoraCierre() {
        return horaCierre;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getTipo() {
        return tipo;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getIdPropietario() {
        return idPropietario;
    }

    public String getCatalogo() {
        return catalogo;
    }

    public boolean isServicioAdicional() {
        return servicioAdicional;
    }

    public boolean isDomicilios() {
        return domicilios;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setHoraApertura(String horaApertura) {
        this.horaApertura = horaApertura;
    }

    public void setHoraCierre(String horaCierre) {
        this.horaCierre = horaCierre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setIdPropietario(String idPropietario) {
        this.idPropietario = idPropietario;
    }

    public void setCatalogo(String catalogo) {
        this.catalogo = catalogo;
    }

    public void setServicioAdicional(boolean servicioAdicional) {
        this.servicioAdicional = servicioAdicional;
    }

    public void setDomicilios(boolean domicilios) {
        this.domicilios = domicilios;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
