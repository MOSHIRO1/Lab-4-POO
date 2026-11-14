import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Evento único de relevos en el que compiten todos los inscritos. */
public class Carrera extends Evento {

    private final Relevos disciplina;
    private Map<Participante, Double> tiempos; // null = descalificado

    public Carrera(Torneo torneo, int numero, List<Participante> participantes, Relevos disciplina) {
        super(torneo, numero, participantes);
        this.disciplina = disciplina;
    }

    @Override
    protected List<ResultadoIndividual> calcularResultados(LectorResultado lector) {
        Map<Participante, Double> leidos = new LinkedHashMap<>();
        for (Participante p : getParticipantes()) {
            boolean descalificado = lector.leerSiNo("  ¿" + p.getNombre() + " fue descalificado? (s/n): ");
            leidos.put(p, descalificado ? null : lector.leerDecimal("  Tiempo en segundos de " + p.getNombre() + ": "));
        }
        List<ResultadoIndividual> resultados = disciplina.calcularResultados(leidos);
        this.tiempos = leidos; // solo se guarda si fue válido
        return resultados;
    }

    @Override
    protected String describirParticipantes() {
        List<String> nombres = new ArrayList<>();
        for (Participante p : getParticipantes()) {
            nombres.add(p.getNombre());
        }
        return "Carrera: " + String.join(", ", nombres);
    }

    @Override
    public String describirResultado() {
        if (tiempos == null) {
            return "pendiente";
        }
        List<Map.Entry<Participante, Double>> conTiempo = new ArrayList<>();
        List<String> descalificados = new ArrayList<>();
        for (Map.Entry<Participante, Double> e : tiempos.entrySet()) {
            if (e.getValue() == null) {
                descalificados.add(e.getKey().getNombre());
            } else {
                conTiempo.add(e);
            }
        }
        conTiempo.sort(Map.Entry.comparingByValue());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < conTiempo.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(i + 1).append("° ").append(conTiempo.get(i).getKey().getNombre())
              .append(String.format(Locale.US, " %.2f s", conTiempo.get(i).getValue()));
        }
        if (!descalificados.isEmpty()) {
            sb.append(" | Descalificados: ").append(String.join(", ", descalificados));
        }
        return sb.toString();
    }
}
