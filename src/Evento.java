import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Estado y comportamiento comunes de los eventos de un torneo: identificación,
 * participantes, programación y ciclo de vida. El registro del resultado es
 * un método plantilla: el flujo y las verificaciones de estado son comunes y
 * cada subclase solo obtiene sus datos y los delega a su disciplina.
 */
public abstract class Evento implements Programable, Registrable {

    private final Torneo torneo;
    private final int numero;
    private final List<Participante> participantes;
    private EstadoEvento estado;
    private LocalDate fecha;
    private int hora;
    private String sede;

    protected Evento(Torneo torneo, int numero, List<Participante> participantes) {
        this.torneo = torneo;
        this.numero = numero;
        this.participantes = List.copyOf(participantes);
        this.estado = EstadoEvento.SIN_PROGRAMAR;
    }

    public String getId() {
        return torneo.getCodigo() + "-E" + numero;
    }

    public int getNumero() {
        return numero;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    @Override
    public List<Participante> getParticipantes() {
        return Collections.unmodifiableList(participantes);
    }

    @Override
    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public int getHora() {
        return hora;
    }

    @Override
    public String getSede() {
        return sede;
    }

    @Override
    public boolean estaProgramado() {
        return estado != EstadoEvento.SIN_PROGRAMAR;
    }

    @Override
    public boolean tieneResultado() {
        return estado == EstadoEvento.FINALIZADO;
    }

    /**
     * Reglas propias de la programación (estado, fecha, hora 0-23, sede).
     * Los conflictos con otros eventos los verifica ArenaGT.
     */
    public void validarProgramacion(LocalDate fecha, int hora, String sede) {
        if (estado == EstadoEvento.FINALIZADO) {
            throw new OperacionInvalidaException("El evento " + getId() + " ya tiene resultado; no se puede reprogramar.");
        }
        if (fecha == null) {
            throw new OperacionInvalidaException("La fecha es obligatoria.");
        }
        if (hora < 0 || hora > 23) {
            throw new OperacionInvalidaException("La hora debe ser una hora completa entre 0 y 23.");
        }
        Validacion.textoNoVacio(sede, "La sede");
    }

    /** Programa o reprograma el evento. */
    @Override
    public void programar(LocalDate fecha, int hora, String sede) {
        validarProgramacion(fecha, hora, sede);
        String sedeValida = sede.trim();
        this.fecha = fecha;
        this.hora = hora;
        this.sede = sedeValida;
        this.estado = EstadoEvento.PROGRAMADO;
    }

    /** Método plantilla para registrar el resultado. */
    @Override
    public final void registrarResultado(LectorResultado lector) {
        if (estado == EstadoEvento.SIN_PROGRAMAR) {
            throw new OperacionInvalidaException("El evento " + getId() + " no está programado.");
        }
        if (estado == EstadoEvento.FINALIZADO) {
            throw new OperacionInvalidaException("El evento " + getId() + " ya está finalizado.");
        }
        List<ResultadoIndividual> resultados = calcularResultados(lector); // puede lanzar excepción: nada cambia
        torneo.acumularResultados(resultados);
        estado = EstadoEvento.FINALIZADO;
        torneo.verificarFinalizacion();
    }

    /**
     * Lee los datos con el lector, los valida mediante la disciplina y
     * devuelve lo que aporta a cada participante. Solo guarda el detalle
     * del resultado si es válido.
     */
    protected abstract List<ResultadoIndividual> calcularResultados(LectorResultado lector);

    /** Texto con los participantes (p. ej. "A vs B"). */
    protected abstract String describirParticipantes();

    @Override
    public String describir() {
        String cuando = estaProgramado()
                ? String.format("%s %02d:00 | %s", fecha, hora, sede)
                : "sin programar";
        String texto = String.format("%-7s | %-24s | %-23s | %s | %s", getId(), torneo.getNombre(),
                torneo.getDisciplina().getNombre(), cuando, describirParticipantes());
        if (tieneResultado()) {
            texto += " | Resultado: " + describirResultado();
        }
        return texto;
    }

    @Override
    public String toString() {
        return describir();
    }
}
