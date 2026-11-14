/**
 * Equipo o cuarteto (en relevos) que representa a una empresa.
 * Es inmutable: su código lo identifica de forma única.
 */
public class Participante {

    private final String codigo;
    private final String nombre;
    private final String empresa;

    public Participante(String codigo, String nombre, String empresa) {
        this.codigo = Validacion.textoNoVacio(codigo, "El código del participante");
        this.nombre = Validacion.textoNoVacio(nombre, "El nombre del participante");
        this.empresa = Validacion.textoNoVacio(empresa, "La empresa del participante");
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmpresa() {
        return empresa;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (otro == null || getClass() != otro.getClass()) {
            return false;
        }
        return codigo.equalsIgnoreCase(((Participante) otro).codigo);
    }

    @Override
    public int hashCode() {
        return codigo.toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + empresa + ")";
    }
}
