import java.time.LocalDate;
import java.util.List;

/**
 * Contrato de todo lo que puede programarse y mostrarse en un calendario
 * (hoy: encuentros y carreras; mañana: exhibiciones u otros). También fija,
 * en un solo lugar, el ORDEN CRONOLÓGICO del calendario: fecha, hora y sede.
 */
public interface Programable extends Comparable<Programable> {

    void programar(LocalDate fecha, int hora, String sede);

    boolean estaProgramado();

    LocalDate getFecha();

    int getHora();

    String getSede();

    List<Participante> getParticipantes();

    String describir();

    /** ¿Participa 'p' en este evento? */
    default boolean involucra(Participante p) {
        return getParticipantes().contains(p);
    }

    /** ¿Está programado exactamente en esa fecha y hora? */
    default boolean ocupa(LocalDate fecha, int hora) {
        return estaProgramado() && getFecha().equals(fecha) && getHora() == hora;
    }

    /** Orden del calendario: por fecha, luego por hora y luego por nombre de sede. */
    @Override
    default int compareTo(Programable otro) {
        int c = getFecha().compareTo(otro.getFecha());
        if (c != 0) {
            return c;
        }
        c = Integer.compare(getHora(), otro.getHora());
        if (c != 0) {
            return c;
        }
        return getSede().compareToIgnoreCase(otro.getSede());
    }
}
