/**
 * Voleibol: 4 a 6 participantes, Q450. Ganador con 3 sets, perdedor 0, 1 o 2.
 * 3-0 o 3-1: 3 y 0 puntos; 3-2: 2 y 1. Diferencia = sets a favor - en contra.
 */
public class Voleibol extends DisciplinaDeEncuentros {

    public Voleibol() {
        super("Voleibol", 4, 6, 450.0);
    }

    @Override
    public String getUnidadMarcador() {
        return "sets";
    }

    @Override
    protected void validarReglas(Marcador marcador) {
        int mayor = Math.max(marcador.getValorA(), marcador.getValorB());
        int menor = Math.min(marcador.getValorA(), marcador.getValorB());
        if (mayor != 3 || menor > 2) {
            throw new OperacionInvalidaException(
                    "En voleibol el ganador debe tener exactamente 3 sets y el perdedor 0, 1 o 2.");
        }
    }

    @Override
    protected int[] puntosTabla(Marcador marcador) {
        int setsPerdedor = Math.min(marcador.getValorA(), marcador.getValorB());
        return setsPerdedor == 2 ? repartir(marcador, 2, 1) : repartir(marcador, 3, 0);
    }
}
