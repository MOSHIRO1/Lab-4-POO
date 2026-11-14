/**
 * Baloncesto: 4 a 6 participantes, Q500. No hay empates.
 * Victoria 2, derrota 1. Diferencia = puntos anotados - recibidos.
 */
public class Baloncesto extends DisciplinaDeEncuentros {

    public Baloncesto() {
        super("Baloncesto", 4, 6, 500.0);
    }

    @Override
    public String getUnidadMarcador() {
        return "puntos";
    }

    @Override
    protected void validarReglas(Marcador marcador) {
        if (marcador.getValorA() == marcador.getValorB()) {
            throw new OperacionInvalidaException("En baloncesto no se aceptan marcadores empatados.");
        }
    }

    @Override
    protected int[] puntosTabla(Marcador marcador) {
        return repartir(marcador, 2, 1);
    }
}
