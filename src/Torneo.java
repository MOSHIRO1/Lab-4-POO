import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Torneo de una sola disciplina. Controla su ciclo de vida (en inscripción,
 * en juego, finalizado), sus inscripciones y sus eventos. Las reglas que
 * dependen de la disciplina se delegan al objeto Disciplina.
 */
public class Torneo {

    private final String codigo;
    private final String nombre;
    private final Disciplina disciplina;
    private EstadoTorneo estado;
    private final List<Inscripcion> inscripciones = new ArrayList<>();
    private final List<Evento> eventos = new ArrayList<>();
    private Inscripcion campeon;
    private double premio;

    public Torneo(String codigo, String nombre, Disciplina disciplina) {
        this.codigo = Validacion.textoNoVacio(codigo, "El código del torneo");
        this.nombre = Validacion.textoNoVacio(nombre, "El nombre del torneo");
        if (disciplina == null) {
            throw new OperacionInvalidaException("La disciplina es obligatoria.");
        }
        this.disciplina = disciplina;
        this.estado = EstadoTorneo.EN_INSCRIPCION;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public EstadoTorneo getEstado() {
        return estado;
    }

    public int getCantidadInscritos() {
        return inscripciones.size();
    }

    public Inscripcion getCampeon() {
        return campeon;
    }

    public double getPremio() {
        return premio;
    }

    /**
     * Inscribe un participante. Valida TODO antes de modificar nada.
     * @param cobrar false solo para los datos iniciales (sin ingresos).
     * @return el monto cobrado.
     */
    public double inscribir(Participante participante, boolean cobrar) {
        if (estado != EstadoTorneo.EN_INSCRIPCION) {
            throw new OperacionInvalidaException("El torneo " + codigo + " no está en inscripción (estado: " + estado + ").");
        }
        disciplina.validarInscripcion(inscripciones.size());
        if (estaInscrito(participante)) {
            throw new OperacionInvalidaException(participante.getNombre() + " ya está inscrito en " + codigo + ".");
        }
        if (contarDeEmpresa(participante.getEmpresa()) >= 2) {
            throw new OperacionInvalidaException("La empresa " + participante.getEmpresa()
                    + " ya tiene dos participantes inscritos en " + codigo + ".");
        }
        double monto = cobrar ? disciplina.getCuotaInscripcion() : 0.0;
        inscripciones.add(new Inscripcion(participante, monto));
        return monto;
    }

    public boolean estaInscrito(Participante participante) {
        for (Inscripcion i : inscripciones) {
            if (i.getParticipante().equals(participante)) {
                return true;
            }
        }
        return false;
    }

    private int contarDeEmpresa(String empresa) {
        int n = 0;
        for (Inscripcion i : inscripciones) {
            if (i.getParticipante().getEmpresa().equalsIgnoreCase(empresa)) {
                n++;
            }
        }
        return n;
    }

    /** Cierra inscripciones y genera los eventos según la disciplina. */
    public void cerrarInscripciones() {
        if (estado != EstadoTorneo.EN_INSCRIPCION) {
            throw new OperacionInvalidaException("El torneo " + codigo + " no está en inscripción.");
        }
        if (!disciplina.alcanzaMinimo(inscripciones.size())) {
            throw new OperacionInvalidaException("No se puede cerrar: " + disciplina.getNombre() + " requiere al menos "
                    + disciplina.getMinParticipantes() + " participantes y hay " + inscripciones.size() + ".");
        }
        List<Participante> participantes = new ArrayList<>();
        for (Inscripcion i : inscripciones) {
            participantes.add(i.getParticipante());
        }
        eventos.addAll(disciplina.crearEventos(this, participantes));
        estado = EstadoTorneo.EN_JUEGO;
    }

    /** Acumula en las inscripciones lo que aportó un evento. */
    void acumularResultados(List<ResultadoIndividual> resultados) {
        for (ResultadoIndividual r : resultados) {
            for (Inscripcion i : inscripciones) {
                if (i.getParticipante().equals(r.getParticipante())) {
                    i.acumular(r);
                }
            }
        }
    }

    /** Si todos los eventos tienen resultado, finaliza y entrega el premio. */
    void verificarFinalizacion() {
        for (Evento e : eventos) {
            if (!e.tieneResultado()) {
                return;
            }
        }
        estado = EstadoTorneo.FINALIZADO;
        campeon = getTablaPosiciones().getPrimerLugar();
        premio = disciplina.calcularPremio(inscripciones.size());
    }

    public Evento buscarEvento(int numero) {
        for (Evento e : eventos) {
            if (e.getNumero() == numero) {
                return e;
            }
        }
        throw new OperacionInvalidaException("El torneo " + codigo + " no tiene el evento número " + numero + ".");
    }

    /** Eventos del torneo (vista de solo lectura). */
    public List<Evento> getEventos() {
        return Collections.unmodifiableList(eventos);
    }

    public double getIngresos() {
        double total = 0;
        for (Inscripcion i : inscripciones) {
            total += i.getMontoPagado();
        }
        return total;
    }

    public TablaPosiciones getTablaPosiciones() {
        return new TablaPosiciones(inscripciones);
    }

    public Calendario getCalendario() {
        return new Calendario(eventos);
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | inscritos: %d", codigo, nombre, disciplina.getNombre(), estado,
                inscripciones.size());
    }
}
