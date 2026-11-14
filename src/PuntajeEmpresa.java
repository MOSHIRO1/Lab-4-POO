/**
 * Fila del ranking de empresas. Se compara por puntos (desc.) y luego por
 * nombre de la empresa (asc.).
 */
public class PuntajeEmpresa implements Comparable<PuntajeEmpresa> {

    private final String empresa;
    private int puntos;

    public PuntajeEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getEmpresa() {
        return empresa;
    }

    public int getPuntos() {
        return puntos;
    }

    void sumar(int puntosTabla) {
        puntos += puntosTabla;
    }

    @Override
    public int compareTo(PuntajeEmpresa otra) {
        int c = Integer.compare(otra.puntos, puntos);
        return c != 0 ? c : empresa.compareToIgnoreCase(otra.empresa);
    }
}
