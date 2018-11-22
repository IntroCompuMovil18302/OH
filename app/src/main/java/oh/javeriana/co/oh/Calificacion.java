package oh.javeriana.co.oh;

public class Calificacion {
    String nombreHuesped;
    String nombreAlojamiento;
    float numeroEstrellas;
    String comentario;

    public Calificacion() {

    }

    public Calificacion(String nombreHuesped, String nombreAlojamiento, float numeroEstrellas, String comentario) {
        this.nombreHuesped = nombreHuesped;
        this.nombreAlojamiento = nombreAlojamiento;
        this.numeroEstrellas = numeroEstrellas;
        this.comentario = comentario;
    }

    public String getNombreHuesped() {
        return nombreHuesped;
    }

    public String getNombreAlojamiento() {
        return nombreAlojamiento;
    }

    public float getNumeroEstrellas() {
        return numeroEstrellas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setNombreHuesped(String nombreHuesped) {
        this.nombreHuesped = nombreHuesped;
    }

    public void setNombreAlojamiento(String nombreAlojamiento) {
        this.nombreAlojamiento = nombreAlojamiento;
    }

    public void setNumeroEstrellas(float numeroEstrellas) {
        this.numeroEstrellas = numeroEstrellas;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
