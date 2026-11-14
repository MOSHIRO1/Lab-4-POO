import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Vista de eventos programados en orden cronológico. Quien lo recorre no
 * sabe cómo se guardan los eventos ni repite el criterio de orden: este
 * viene de Programable.compareTo.
 */
public class Calendario implements Iterable<Programable> {

    private final List<Programable> eventos = new ArrayList<>();

    public Calendario(Collection<? extends Programable> fuente) {
        for (Programable p : fuente) {
            if (p.estaProgramado()) {
                eventos.add(p);
            }
        }
        Collections.sort(eventos);
    }

    public boolean estaVacio() {
        return eventos.isEmpty();
    }

    public int cantidad() {
        return eventos.size();
    }

    @Override
    public Iterator<Programable> iterator() {
        return Collections.unmodifiableList(eventos).iterator();
    }
}
