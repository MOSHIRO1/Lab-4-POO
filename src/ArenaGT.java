import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Sistema de ArenaGT: guarda participantes, torneos y el catálogo de
 * disciplinas; coordina las operaciones que involucran varios torneos
 * (conflictos de programación, calendarios generales y reportes).
 */
public class ArenaGT {

    private final List<Disciplina> disciplinas = new ArrayList<>();
    private final Map<String, Participante> participantes = new LinkedHashMap<>();
    private final Map<String, Torneo> torneos = new LinkedHashMap<>();

    public ArenaGT() {
        disciplinas.add(new FutbolSala());
        disciplinas.add(new FutbolSalaPenales());
        disciplinas.add(new Baloncesto());
        disciplinas.add(new Voleibol());
        disciplinas.add(new Relevos());
    }

    // ---------------------------------------------------------------- registro

    public Participante registrarParticipante(String codigo, String nombre, String empresa) {
        Participante p = new Participante(codigo, nombre, empresa);
        String clave = p.getCodigo().toUpperCase();
        if (participantes.containsKey(clave)) {
            throw new OperacionInvalidaException("Ya existe un participante con el código " + p.getCodigo() + ".");
        }
        participantes.put(clave, p);
        return p;
    }

    public Torneo crearTorneo(String codigo, String nombre, Disciplina disciplina) {
        Torneo t = new Torneo(codigo, nombre, disciplina);
        String clave = t.getCodigo().toUpperCase();
        if (torneos.containsKey(clave)) {
            throw new OperacionInvalidaException("Ya existe un torneo con el código " + t.getCodigo() + ".");
        }
        torneos.put(clave, t);
        return t;
    }

    // ---------------------------------------------------------- inscripciones

    /** Cotiza sin inscribir ni cobrar. */
    public double cotizar(Disciplina disciplina, int cantidad) {
        return disciplina.cotizar(cantidad);
    }

    public double inscribir(String codigoTorneo, String codigoParticipante) {
        return buscarTorneo(codigoTorneo).inscribir(buscarParticipante(codigoParticipante), true);
    }

    /** Solo para los datos iniciales: inscribe sin registrar ingresos. */
    void inscribirSinCobro(String codigoTorneo, String codigoParticipante) {
        buscarTorneo(codigoTorneo).inscribir(buscarParticipante(codigoParticipante), false);
    }

    public void cerrarInscripciones(String codigoTorneo) {
        buscarTorneo(codigoTorneo).cerrarInscripciones();
    }

    // ------------------------------------------------------------ calendario

    /** Programa o reprograma un evento verificando conflictos en TODOS los torneos. */
    public void programarEvento(String codigoTorneo, int numeroEvento, LocalDate fecha, int hora, String sede) {
        Torneo torneo = buscarTorneo(codigoTorneo);
        Evento evento = torneo.buscarEvento(numeroEvento);
        evento.validarProgramacion(fecha, hora, sede);
        String sedeValida = sede.trim();
        for (Programable otro : todosLosEventos()) {
            if (otro == evento || !otro.ocupa(fecha, hora)) {
                continue;
            }
            if (otro.getSede().equalsIgnoreCase(sedeValida)) {
                throw new OperacionInvalidaException("Conflicto de sede: " + sedeValida + " ya tiene un evento el "
                        + fecha + " a las " + String.format("%02d:00", hora) + ".");
            }
            for (Participante p : evento.getParticipantes()) {
                if (otro.involucra(p)) {
                    throw new OperacionInvalidaException("Conflicto de participante: " + p.getNombre()
                            + " ya tiene otro evento el " + fecha + " a las " + String.format("%02d:00", hora) + ".");
                }
            }
        }
        evento.programar(fecha, hora, sedeValida);
    }

    public void registrarResultado(String codigoTorneo, int numeroEvento, LectorResultado lector) {
        buscarTorneo(codigoTorneo).buscarEvento(numeroEvento).registrarResultado(lector);
    }

    public Calendario getCalendarioGeneral() {
        return new Calendario(todosLosEventos());
    }

    public Calendario getCalendarioDeTorneo(String codigoTorneo) {
        return buscarTorneo(codigoTorneo).getCalendario();
    }

    public Calendario getCalendarioDeParticipante(String codigoParticipante) {
        Participante p = buscarParticipante(codigoParticipante);
        List<Programable> suyos = new ArrayList<>();
        for (Programable e : todosLosEventos()) {
            if (e.involucra(p)) {
                suyos.add(e);
            }
        }
        return new Calendario(suyos);
    }

    private List<Programable> todosLosEventos() {
        List<Programable> todos = new ArrayList<>();
        for (Torneo t : torneos.values()) {
            todos.addAll(t.getEventos());
        }
        return todos;
    }

    // --------------------------------------------------------------- reportes

    public double getIngresosTotales() {
        double total = 0;
        for (Torneo t : torneos.values()) {
            total += t.getIngresos();
        }
        return total;
    }

    /** Ingresos por inscripciones de cada disciplina del catálogo. */
    public Map<String, Double> getIngresosPorDisciplina() {
        Map<String, Double> resultado = new LinkedHashMap<>();
        for (Disciplina d : disciplinas) {
            resultado.put(d.getNombre(), 0.0);
        }
        for (Torneo t : torneos.values()) {
            resultado.merge(t.getDisciplina().getNombre(), t.getIngresos(), Double::sum);
        }
        return Collections.unmodifiableMap(resultado);
    }

    public double getPremiosEntregados() {
        double total = 0;
        for (Torneo t : torneos.values()) {
            total += t.getPremio();
        }
        return total;
    }

    /** Suma de puntos de tabla de todos los participantes de cada empresa, en todos los torneos. */
    public List<PuntajeEmpresa> getRankingEmpresas() {
        Map<String, PuntajeEmpresa> porEmpresa = new LinkedHashMap<>();
        for (Participante p : participantes.values()) {
            porEmpresa.computeIfAbsent(p.getEmpresa().toUpperCase(), k -> new PuntajeEmpresa(p.getEmpresa()));
        }
        for (Torneo t : torneos.values()) {
            for (Inscripcion i : t.getTablaPosiciones()) {
                porEmpresa.get(i.getParticipante().getEmpresa().toUpperCase()).sumar(i.getPuntosTabla());
            }
        }
        List<PuntajeEmpresa> ranking = new ArrayList<>(porEmpresa.values());
        Collections.sort(ranking);
        return Collections.unmodifiableList(ranking);
    }

    // ---------------------------------------------------------------- consultas

    public Torneo buscarTorneo(String codigo) {
        Torneo t = codigo == null ? null : torneos.get(codigo.trim().toUpperCase());
        if (t == null) {
            throw new OperacionInvalidaException("No existe un torneo con el código '" + codigo + "'.");
        }
        return t;
    }

    public Participante buscarParticipante(String codigo) {
        Participante p = codigo == null ? null : participantes.get(codigo.trim().toUpperCase());
        if (p == null) {
            throw new OperacionInvalidaException("No existe un participante con el código '" + codigo + "'.");
        }
        return p;
    }

    public Collection<Torneo> getTorneos() {
        return Collections.unmodifiableCollection(torneos.values());
    }

    public Collection<Participante> getParticipantes() {
        return Collections.unmodifiableCollection(participantes.values());
    }

    public List<Disciplina> getDisciplinas() {
        return Collections.unmodifiableList(disciplinas);
    }

    /** Torneos en los que está inscrito un participante. */
    public List<Torneo> getTorneosDe(Participante p) {
        List<Torneo> suyos = new ArrayList<>();
        for (Torneo t : torneos.values()) {
            if (t.estaInscrito(p)) {
                suyos.add(t);
            }
        }
        return suyos;
    }
}
