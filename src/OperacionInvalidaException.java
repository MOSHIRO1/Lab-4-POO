/**
 * Excepción que se lanza cuando una operación viola una regla del negocio.
 * Siempre se lanza ANTES de modificar el estado, por lo que una operación
 * rechazada nunca altera tablas, ingresos ni premios.
 */
public class OperacionInvalidaException extends RuntimeException {

    public OperacionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
