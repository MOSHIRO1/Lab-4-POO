import java.util.List;

/** Evento entre dos participantes, cuyo resultado es un marcador. */
public class Encuentro extends Evento {

    private final DisciplinaDeEncuentros disciplina;
    private Marcador marcador;

    public Encuentro(Torneo torneo, int numero, Participante a, Participante b, DisciplinaDeEncuentros disciplina) {
        super(torneo, numero, List.of(a, b));
        this.disciplina = disciplina;
    }

    public Participante getParticipanteA() {
        return getParticipantes().get(0);
    }

    public Participante getParticipanteB() {
        return getParticipantes().get(1);
    }

    @Override
    protected List<ResultadoIndividual> calcularResultados(LectorResultado lector) {
        String unidad = disciplina.getUnidadMarcador();
        int a = lector.leerEntero("  " + capitalizar(unidad) + " de " + getParticipanteA().getNombre() + ": ");
        int b = lector.leerEntero("  " + capitalizar(unidad) + " de " + getParticipanteB().getNombre() + ": ");
        Marcador m;
        if (disciplina.admitePenales() && lector.leerSiNo("  ¿Se registran penales? (s/n): ")) {
            int pa = lector.leerEntero("  Penales convertidos por " + getParticipanteA().getNombre() + ": ");
            int pb = lector.leerEntero("  Penales convertidos por " + getParticipanteB().getNombre() + ": ");
            m = new Marcador(a, b, pa, pb);
        } else {
            m = new Marcador(a, b);
        }
        List<ResultadoIndividual> resultados = disciplina.calcularResultados(getParticipanteA(), getParticipanteB(), m);
        this.marcador = m; // solo se guarda si fue válido
        return resultados;
    }

    @Override
    protected String describirParticipantes() {
        return getParticipanteA().getNombre() + " vs " + getParticipanteB().getNombre();
    }

    @Override
    public String describirResultado() {
        return marcador == null ? "pendiente" : marcador.toString();
    }

    private static String capitalizar(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
