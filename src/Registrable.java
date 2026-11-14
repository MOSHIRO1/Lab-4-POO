/**
 * Contrato de todo lo que admite el registro de un resultado.
 */
public interface Registrable {

    /** Lee, valida y registra el resultado. Si es inválido no cambia nada. */
    void registrarResultado(LectorResultado lector);

    boolean tieneResultado();

    String describirResultado();
}
