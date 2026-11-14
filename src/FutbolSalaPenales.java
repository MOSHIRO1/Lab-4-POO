/**
 * Fútbol sala con penales: ES un fútbol sala (mismo cupo, misma unidad,
 * misma diferencia en tiempo reglamentario) que no acepta empates.
 * Inscripción: Q150 más que el fútbol sala.
 */
public class FutbolSalaPenales extends FutbolSala {

    public static final double RECARGO = 150.0;

    public FutbolSalaPenales() {
        super("Fútbol sala con penales", FutbolSala.CUOTA + RECARGO);
    }

    /** En esta disciplina sí se pueden registrar penales. */
    @Override
    public boolean admitePenales() {
        return true;
    }

    @Override
    protected void validarReglas(Marcador marcador) {
        boolean empateEnGoles = marcador.getValorA() == marcador.getValorB();
        if (!empateEnGoles) {
            if (marcador.tienePenales()) {
                throw new OperacionInvalidaException("Si los goles son distintos no se registran penales.");
            }
            return;
        }
        if (!marcador.tienePenales()) {
            throw new OperacionInvalidaException("Empate en goles: se deben registrar los penales.");
        }
        if (marcador.getPenalesA() < 0 || marcador.getPenalesB() < 0) {
            throw new OperacionInvalidaException("Los penales deben ser enteros no negativos.");
        }
        if (marcador.getPenalesA() == marcador.getPenalesB()) {
            throw new OperacionInvalidaException("Los penales convertidos no pueden ser iguales.");
        }
    }

    @Override
    protected int[] puntosTabla(Marcador marcador) {
        if (!marcador.tienePenales()) {
            return super.puntosTabla(marcador); // ganador en tiempo reglamentario: igual que fútbol sala
        }
        return repartir(marcador, 2, 1);
    }
}
