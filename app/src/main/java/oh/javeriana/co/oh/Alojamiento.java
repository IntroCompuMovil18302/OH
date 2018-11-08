package oh.javeriana.co.oh;

import android.location.Geocoder;
import android.text.Editable;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Alojamiento implements Serializable {
    private String tipo;
    private String nombre;
    private String descripcion;
    private double latitud;
    private double longitud;
    private double valorNoche;
    private int cantHuespedes;
    private String direccion;
    private String foto1;
    private String foto2;
    private String foto3;
    private String foto4;
    private String nombreUsuario;

    public Alojamiento() {
    }


    public Alojamiento( String nombre, String descripcion, String direccion, int cantHuespedes, double valorNoche, String nombreUsuario, String tipoAlojamiento ) {


        //this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantHuespedes = cantHuespedes;
        this.valorNoche = valorNoche;
        this.direccion = direccion;
        this.nombreUsuario = nombreUsuario;
        this.tipo = tipoAlojamiento;

        /*Geocoder mGeocoder = new Geocoder(getBaseContext());
        String addressString = mAddress.getText().toString();
        if (!addressString.isEmpty()) {
            try {
                List<Address> addresses = mGeocoder.getFromLocationName(addressString, 2);
                if (addresses != null && !addresses.isEmpty()) {
                    Address addressResult = addresses.get(0);
                    LatLng position = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        //this.foto = foto;
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

    public String getFoto1() {
        return foto1;
    }

    public String getFoto2() {
        return foto2;
    }

    public String getFoto3() {
        return foto3;
    }

    public String getFoto4() {
        return foto4;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
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

    public void setFoto1(String foto1) {
        this.foto1 = foto1;
    }

    public void setFoto2(String foto2) {
        this.foto2 = foto2;
    }

    public void setFoto3(String foto3) {
        this.foto3 = foto3;
    }

    public void setFoto4(String foto4) {
        this.foto4 = foto4;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
