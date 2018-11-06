package oh.javeriana.co.oh;

import java.text.ParseException;

public class Anfitrion extends Usuario {

    public Anfitrion(String rol, String email, String nombre, String fechaNac, String foto) throws ParseException {
        super(rol, email, nombre, fechaNac, foto);
    }
}
