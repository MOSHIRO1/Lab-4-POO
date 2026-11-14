/**
 * Fuente de los datos de un resultado. Permite que los eventos pidan sus
 * datos sin depender de la consola (la consola y los datos iniciales la
 * implementan de forma distinta).
 */
public interface LectorResultado {

    int leerEntero(String mensaje);

    double leerDecimal(String mensaje);

    boolean leerSiNo(String mensaje);
}
