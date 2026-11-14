import java.util.Locale;

/** Utilidades de validación y formato compartidas (sin estado). */
public final class Validacion {

    private Validacion() {
    }

    /** Devuelve el texto sin espacios extremos o lanza excepción si está vacío. */
    public static String textoNoVacio(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new OperacionInvalidaException(campo + " no puede estar en blanco.");
        }
        return texto.trim();
    }

    /** Formatea un monto en quetzales con dos decimales. */
    public static String monto(double valor) {
        return String.format(Locale.US, "Q%,.2f", valor);
    }
}
