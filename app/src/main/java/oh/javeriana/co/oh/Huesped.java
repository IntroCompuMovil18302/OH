package oh.javeriana.co.oh;

import java.text.ParseException;

public class Huesped extends Usuario {
    private String genero;
    private String nacionalidad;

    public Huesped(String nombre, String email, String fechaNac, String foto, String genero, String nacionalidad) throws ParseException {
        super("huesped", email, nombre, fechaNac, foto);
        this.genero = genero;
        this.nacionalidad = nacionalidad;
    }

    public String getGenero() {
        return genero;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }
}
