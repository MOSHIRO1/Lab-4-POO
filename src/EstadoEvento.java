/** Estados por los que pasa un evento (encuentro o carrera). */
public enum EstadoEvento {
    SIN_PROGRAMAR("Sin programar"),
    PROGRAMADO("Programado"),
    FINALIZADO("Finalizado");

    private final String texto;

    EstadoEvento(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto;
    }
}
