import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Tabla de posiciones de un torneo. Se recorre ya ordenada (orden natural
 * de Inscripcion) sin exponer cómo guarda el torneo sus inscripciones.
 */
public class TablaPosiciones implements Iterable<Inscripcion> {

    private final List<Inscripcion> filas;

    public TablaPosiciones(Collection<Inscripcion> inscripciones) {
        this.filas = new ArrayList<>(inscripciones);
        Collections.sort(this.filas);
    }

    public Inscripcion getPrimerLugar() {
        return filas.isEmpty() ? null : filas.get(0);
    }

    public boolean estaVacia() {
        return filas.isEmpty();
    }

    @Override
    public Iterator<Inscripcion> iterator() {
        return Collections.unmodifiableList(filas).iterator();
    }
}
