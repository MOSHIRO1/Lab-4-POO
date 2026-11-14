import java.util.ArrayList;
import java.util.List;

/**
 * Disciplinas que se juegan en encuentros entre dos participantes (todos
 * contra todos). Define el algoritmo común para calcular un resultado
 * (método plantilla) y deja a las subclases las reglas de validación y de
 * puntos de tabla.
 */
public abstract class DisciplinaDeEncuentros extends Disciplina {

    protected DisciplinaDeEncuentros(String nombre, int min, int max, double cuota) {
        super(nombre, min, max, cuota);
    }

    /** Un encuentro por cada pareja de participantes, una sola vez. */
    @Override
    public List<Evento> crearEventos(Torneo torneo, List<Participante> participantes) {
        List<Evento> eventos = new ArrayList<>();
        int numero = 1;
        for (int i = 0; i < participantes.size(); i++) {
            for (int j = i + 1; j < participantes.size(); j++) {
                eventos.add(new Encuentro(torneo, numero++, participantes.get(i), participantes.get(j), this));
            }
        }
        return eventos;
    }

    /** Nombre de lo que se anota (goles, puntos, sets). */
    public abstract String getUnidadMarcador();

    /** Indica si la disciplina admite registrar penales. Por defecto no. */
    public boolean admitePenales() {
        return false;
    }

    /** Validación común + reglas propias. Lanza excepción si es inválido. */
    public final void validar(Marcador marcador) {
        if (marcador.getValorA() < 0 || marcador.getValorB() < 0) {
            throw new OperacionInvalidaException("Los " + getUnidadMarcador()
                    + " deben ser enteros no negativos.");
        }
        validarReglas(marcador);
    }

    /** Reglas de validez propias de la disciplina. */
    protected abstract void validarReglas(Marcador marcador);

    /** Puntos de tabla {A, B} que otorga el marcador. */
    protected abstract int[] puntosTabla(Marcador marcador);

    /** Diferencia del encuentro para A (B recibe el valor negativo). */
    protected int diferencia(Marcador marcador) {
        return marcador.getValorA() - marcador.getValorB();
    }

    /**
     * Método plantilla: valida y calcula lo que el encuentro aporta a cada
     * participante. No modifica ningún estado.
     */
    public final List<ResultadoIndividual> calcularResultados(Participante a, Participante b, Marcador marcador) {
        validar(marcador);
        int[] puntos = puntosTabla(marcador);
        int dif = diferencia(marcador);
        int ganador = marcador.getGanador();
        List<ResultadoIndividual> resultados = new ArrayList<>();
        resultados.add(new ResultadoIndividual(a, puntos[0], dif, ganador == 1));
        resultados.add(new ResultadoIndividual(b, puntos[1], -dif, ganador == 2));
        return resultados;
    }

    /** Ayuda: asigna puntos al ganador y al perdedor según quién ganó. */
    protected static int[] repartir(Marcador marcador, int puntosGanador, int puntosPerdedor) {
        return marcador.getGanador() == 1
                ? new int[] {puntosGanador, puntosPerdedor}
                : new int[] {puntosPerdedor, puntosGanador};
    }
}
