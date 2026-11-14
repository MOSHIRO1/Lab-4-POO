import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Relevos: 3 a 8 participantes, Q300. Una sola carrera con todos los
 * inscritos. Puntos por lugar 10-8-6-5-4-3-2-1; descalificado 0.
 * La diferencia siempre vale 0.
 */
public class Relevos extends Disciplina {

    private static final int[] PUNTOS_POR_LUGAR = {10, 8, 6, 5, 4, 3, 2, 1};

    public Relevos() {
        super("Relevos", 3, 8, 300.0);
    }

    /** Una única carrera con todos los inscritos. */
    @Override
    public List<Evento> crearEventos(Torneo torneo, List<Participante> participantes) {
        List<Evento> eventos = new ArrayList<>();
        eventos.add(new Carrera(torneo, 1, participantes, this));
        return eventos;
    }

    /** Puntos de tabla para el lugar indicado (1 = primero). */
    public int puntosPorLugar(int lugar) {
        return lugar >= 1 && lugar <= PUNTOS_POR_LUGAR.length ? PUNTOS_POR_LUGAR[lugar - 1] : 0;
    }

    /** En relevos la diferencia siempre es 0. */
    public int diferencia() {
        return 0;
    }

    /** Valida que los tiempos sean mayores que cero y distintos entre sí. */
    public void validar(Map<Participante, Double> tiempos) {
        Set<Double> vistos = new HashSet<>();
        for (Map.Entry<Participante, Double> e : tiempos.entrySet()) {
            Double tiempo = e.getValue();
            if (tiempo == null) {
                continue; // descalificado
            }
            if (tiempo <= 0) {
                throw new OperacionInvalidaException("El tiempo de " + e.getKey().getNombre()
                        + " debe ser mayor que cero.");
            }
            if (!vistos.add(tiempo)) {
                throw new OperacionInvalidaException("Tiempo repetido (" + tiempo
                        + " s): los tiempos deben ser distintos.");
            }
        }
    }

    /**
     * Valida y calcula lo que la carrera aporta a cada participante.
     * Un valor null en el mapa significa descalificación. No modifica estado.
     */
    public List<ResultadoIndividual> calcularResultados(Map<Participante, Double> tiempos) {
        validar(tiempos);
        List<Participante> conTiempo = new ArrayList<>();
        for (Map.Entry<Participante, Double> e : tiempos.entrySet()) {
            if (e.getValue() != null) {
                conTiempo.add(e.getKey());
            }
        }
        conTiempo.sort((p1, p2) -> Double.compare(tiempos.get(p1), tiempos.get(p2)));
        List<ResultadoIndividual> resultados = new ArrayList<>();
        for (int i = 0; i < conTiempo.size(); i++) {
            int lugar = i + 1;
            resultados.add(new ResultadoIndividual(conTiempo.get(i), puntosPorLugar(lugar), diferencia(), lugar == 1));
        }
        for (Map.Entry<Participante, Double> e : tiempos.entrySet()) {
            if (e.getValue() == null) {
                resultados.add(new ResultadoIndividual(e.getKey(), 0, diferencia(), false));
            }
        }
        return resultados;
    }
}
