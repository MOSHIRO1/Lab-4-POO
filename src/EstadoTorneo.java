/** Estados por los que pasa un torneo. */
public enum EstadoTorneo {
    EN_INSCRIPCION("En inscripción"),
    EN_JUEGO("En juego"),
    FINALIZADO("Finalizado");

    private final String texto;

    EstadoTorneo(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto;
    }
}
