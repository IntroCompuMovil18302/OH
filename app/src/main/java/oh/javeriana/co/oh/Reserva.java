package oh.javeriana.co.oh;

import java.io.Serializable;

public class Reserva implements Serializable {
    String idHuesped;
    String idAlojamiento;
    String fechaReserva;
    String fechaInicial;
    String fechaFinal;
    boolean calificado;

    public Reserva(String idHuesped, String idAlojamiento, String fechaReserva, String fechaInicial, String fechaFinal, boolean calificado) {
        this.idHuesped = idHuesped;
        this.idAlojamiento = idAlojamiento;
        this.fechaReserva = fechaReserva;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
        this.calificado = calificado;
    }

    public String getIdHuesped() {
        return idHuesped;
    }

    public void setIdHuesped(String idHuesped) {
        this.idHuesped = idHuesped;
    }

    public String getIdAlojamiento() {
        return idAlojamiento;
    }

    public void setIdAlojamiento(String idAlojamiento) {
        this.idAlojamiento = idAlojamiento;
    }

    public String getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getFechaInicial() {
        return fechaInicial;
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

    public boolean isCalificado() {
        return calificado;
    }

    public void setCalificado(boolean calificado) {
        this.calificado = calificado;
    }
}
