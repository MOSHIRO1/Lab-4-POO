/**
 * Fútbol sala: 4 a 8 participantes, Q600. Victoria 3, empate 1, derrota 0.
 * Diferencia = goles a favor - goles en contra.
 */
public class FutbolSala extends DisciplinaDeEncuentros {

    public static final double CUOTA = 600.0;

    public FutbolSala() {
        this("Fútbol sala", CUOTA);
    }

    /** Constructor para subclases que son variantes del fútbol sala. */
    protected FutbolSala(String nombre, double cuota) {
        super(nombre, 4, 8, cuota);
    }

    @Override
    public String getUnidadMarcador() {
        return "goles";
    }

    @Override
    protected void validarReglas(Marcador marcador) {
        if (marcador.tienePenales()) {
            throw new OperacionInvalidaException("En fútbol sala no se registran penales.");
        }
    }

    @Override
    protected int[] puntosTabla(Marcador marcador) {
        if (marcador.getGanador() == 0) {
            return new int[] {1, 1};
        }
        return repartir(marcador, 3, 0);
    }
}
