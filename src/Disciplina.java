import java.util.List;

/**
 * Reglas de una disciplina. Cada subclase concreta fija, mediante la cadena
 * de constructores, su nombre, mínimo, máximo y cuota; y resuelve las
 * decisiones propias (tipo de eventos, validación de resultados, puntos de
 * tabla y diferencia). Las instancias no tienen estado mutable, por lo que
 * un mismo objeto puede compartirse entre varios torneos.
 */
public abstract class Disciplina {

    private final String nombre;
    private final int minParticipantes;
    private final int maxParticipantes;
    private final double cuotaInscripcion;

    protected Disciplina(String nombre, int minParticipantes, int maxParticipantes, double cuotaInscripcion) {
        this.nombre = nombre;
        this.minParticipantes = minParticipantes;
        this.maxParticipantes = maxParticipantes;
        this.cuotaInscripcion = cuotaInscripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getMinParticipantes() {
        return minParticipantes;
    }

    public int getMaxParticipantes() {
        return maxParticipantes;
    }

    public double getCuotaInscripcion() {
        return cuotaInscripcion;
    }

    /** Monto total de inscribir 'cantidad' participantes. No modifica nada. */
    public double cotizar(int cantidad) {
        if (cantidad <= 0 || cantidad > maxParticipantes) {
            throw new OperacionInvalidaException("La cantidad debe estar entre 1 y " + maxParticipantes
                    + " para " + nombre + ".");
        }
        return cuotaInscripcion * cantidad;
    }

    /** Premio para el campeón: 50 % de la cuota por la cantidad de inscritos. */
    public double calcularPremio(int inscritos) {
        return 0.5 * cuotaInscripcion * inscritos;
    }

    /**
     * Regla de inscripción propia de la disciplina (cupo máximo). Una nueva
     * disciplina puede sobrescribirla para agregar condiciones.
     */
    public void validarInscripcion(int inscritosActuales) {
        if (inscritosActuales >= maxParticipantes) {
            throw new OperacionInvalidaException("El torneo ya alcanzó el máximo de " + maxParticipantes
                    + " participantes de " + nombre + ".");
        }
    }

    /** Indica si la cantidad de inscritos permite cerrar inscripciones. */
    public boolean alcanzaMinimo(int inscritos) {
        return inscritos >= minParticipantes;
    }

    /** Genera los eventos del torneo al cerrar inscripciones. */
    public abstract List<Evento> crearEventos(Torneo torneo, List<Participante> participantes);

    @Override
    public String toString() {
        return nombre;
    }
}
