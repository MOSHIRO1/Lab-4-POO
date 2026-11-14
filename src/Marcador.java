/**
 * Marcador de un encuentro: valores del participante A y del B
 * (goles, puntos o sets) y, opcionalmente, penales convertidos.
 */
public class Marcador {

    private final int valorA;
    private final int valorB;
    private final boolean conPenales;
    private final int penalesA;
    private final int penalesB;

    /** Marcador sin penales. */
    public Marcador(int valorA, int valorB) {
        this(valorA, valorB, false, 0, 0);
    }

    /** Marcador con penales. */
    public Marcador(int valorA, int valorB, int penalesA, int penalesB) {
        this(valorA, valorB, true, penalesA, penalesB);
    }

    private Marcador(int valorA, int valorB, boolean conPenales, int penalesA, int penalesB) {
        this.valorA = valorA;
        this.valorB = valorB;
        this.conPenales = conPenales;
        this.penalesA = penalesA;
        this.penalesB = penalesB;
    }

    public int getValorA() {
        return valorA;
    }

    public int getValorB() {
        return valorB;
    }

    public boolean tienePenales() {
        return conPenales;
    }

    public int getPenalesA() {
        return penalesA;
    }

    public int getPenalesB() {
        return penalesB;
    }

    /** @return 1 si gana A, 2 si gana B, 0 si es empate. */
    public int getGanador() {
        if (valorA != valorB) {
            return valorA > valorB ? 1 : 2;
        }
        if (conPenales && penalesA != penalesB) {
            return penalesA > penalesB ? 1 : 2;
        }
        return 0;
    }

    @Override
    public String toString() {
        String texto = valorA + " - " + valorB;
        if (conPenales) {
            texto += " (penales " + penalesA + " - " + penalesB + ")";
        }
        return texto;
    }
}
