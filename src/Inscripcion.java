/**
 * Participación de un participante en UN torneo: lo pagado y lo acumulado
 * en la tabla de ese torneo. Sus objetos se comparan según el criterio de
 * la tabla de posiciones.
 */
public class Inscripcion implements Comparable<Inscripcion> {

    private final Participante participante;
    private final double montoPagado;
    private int puntosTabla;
    private int diferencia;
    private int victorias;
    private int eventosJugados;

    public Inscripcion(Participante participante, double montoPagado) {
        this.participante = participante;
        this.montoPagado = montoPagado;
    }

    public Participante getParticipante() {
        return participante;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public int getPuntosTabla() {
        return puntosTabla;
    }

    public int getDiferencia() {
        return diferencia;
    }

    public int getVictorias() {
        return victorias;
    }

    public int getEventosJugados() {
        return eventosJugados;
    }

    /** Acumula lo que aportó un evento (solo lo invoca el Torneo). */
    void acumular(ResultadoIndividual r) {
        puntosTabla += r.getPuntosTabla();
        diferencia += r.getDiferencia();
        if (r.esVictoria()) {
            victorias++;
        }
        eventosJugados++;
    }

    /** Puntos desc., diferencia desc., victorias desc., nombre asc. */
    @Override
    public int compareTo(Inscripcion otra) {
        int c = Integer.compare(otra.puntosTabla, puntosTabla);
        if (c != 0) {
            return c;
        }
        c = Integer.compare(otra.diferencia, diferencia);
        if (c != 0) {
            return c;
        }
        c = Integer.compare(otra.victorias, victorias);
        if (c != 0) {
            return c;
        }
        return participante.getNombre().compareToIgnoreCase(otra.participante.getNombre());
    }
}
