import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Lector que entrega valores fijos, en orden. Se usa para cargar los
 * resultados de los datos iniciales sin pasar por la consola.
 */
public class LectorPredefinido implements LectorResultado {

    private final Deque<String> valores;

    public LectorPredefinido(String... valores) {
        this.valores = new ArrayDeque<>(Arrays.asList(valores));
    }

    private String siguiente() {
        if (valores.isEmpty()) {
            throw new OperacionInvalidaException("Faltan datos para el resultado.");
        }
        return valores.poll();
    }

    @Override
    public int leerEntero(String mensaje) {
        return Integer.parseInt(siguiente());
    }

    @Override
    public double leerDecimal(String mensaje) {
        return Double.parseDouble(siguiente());
    }

    @Override
    public boolean leerSiNo(String mensaje) {
        return siguiente().equalsIgnoreCase("s");
    }
}
