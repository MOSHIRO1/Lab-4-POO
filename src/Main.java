import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Aplicación de consola de ArenaGT. Solo interactúa con el usuario:
 * todas las reglas están en el modelo (ArenaGT, Torneo, Disciplina, Evento).
 */
public class Main {

    private final ArenaGT arena;
    private final LectorConsola lector;

    public Main(ArenaGT arena, LectorConsola lector) {
        this.arena = arena;
        this.lector = lector;
    }

    public static void main(String[] args) {
        ArenaGT arena = new ArenaGT();
        DatosIniciales.cargar(arena);
        new Main(arena, new LectorConsola(new Scanner(System.in))).ejecutar();
    }

    public void ejecutar() {
        System.out.println("=== ArenaGT - Copas deportivas entre empresas ===");
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            try {
                int opcion = lector.leerEntero("Opción: ");
                salir = atender(opcion);
            } catch (OperacionInvalidaException e) {
                System.out.println("  ERROR: " + e.getMessage() + " (no se realizó ningún cambio)");
            } catch (NoSuchElementException e) {
                salir = true; // fin de la entrada estándar
            }
        }
        System.out.println("Hasta luego.");
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("------------------- MENU -------------------");
        System.out.println(" 1. Registrar participante");
        System.out.println(" 2. Crear torneo");
        System.out.println(" 3. Cotizar inscripción");
        System.out.println(" 4. Inscribir participante en torneo");
        System.out.println(" 5. Cerrar inscripciones de un torneo");
        System.out.println(" 6. Programar / reprogramar evento");
        System.out.println(" 7. Registrar resultado");
        System.out.println(" 8. Consultar torneos");
        System.out.println(" 9. Consultar participantes");
        System.out.println("10. Calendario general");
        System.out.println("11. Calendario de un torneo");
        System.out.println("12. Calendario de un participante");
        System.out.println("13. Reporte: tabla de posiciones y campeón");
        System.out.println("14. Reporte: ingresos y premios");
        System.out.println("15. Reporte: ranking de empresas");
        System.out.println(" 0. Salir");
    }

    private boolean atender(int opcion) {
        switch (opcion) {
            case 1 -> registrarParticipante();
            case 2 -> crearTorneo();
            case 3 -> cotizar();
            case 4 -> inscribir();
            case 5 -> cerrarInscripciones();
            case 6 -> programarEvento();
            case 7 -> registrarResultado();
            case 8 -> consultarTorneos();
            case 9 -> consultarParticipantes();
            case 10 -> mostrarCalendario("Calendario general", arena.getCalendarioGeneral());
            case 11 -> {
                String t = lector.leerTexto("Código del torneo: ");
                mostrarCalendario("Calendario de " + arena.buscarTorneo(t).getNombre(), arena.getCalendarioDeTorneo(t));
            }
            case 12 -> {
                String p = lector.leerTexto("Código del participante: ");
                mostrarCalendario("Calendario de " + arena.buscarParticipante(p).getNombre(),
                        arena.getCalendarioDeParticipante(p));
            }
            case 13 -> reporteTabla();
            case 14 -> reporteIngresos();
            case 15 -> reporteRanking();
            case 0 -> {
                return true;
            }
            default -> System.out.println("  Opción no válida.");
        }
        return false;
    }

    // ------------------------------------------------------------ operaciones

    private void registrarParticipante() {
        String codigo = lector.leerTexto("Código: ");
        String nombre = lector.leerTexto("Nombre: ");
        String empresa = lector.leerTexto("Empresa: ");
        Participante p = arena.registrarParticipante(codigo, nombre, empresa);
        System.out.println("  Participante registrado: " + p);
    }

    private Disciplina elegirDisciplina() {
        List<Disciplina> lista = arena.getDisciplinas();
        for (int i = 0; i < lista.size(); i++) {
            Disciplina d = lista.get(i);
            System.out.printf("  %d. %s (%d a %d participantes, %s por participante)%n", i + 1, d.getNombre(),
                    d.getMinParticipantes(), d.getMaxParticipantes(), Validacion.monto(d.getCuotaInscripcion()));
        }
        int n = lector.leerEntero("Disciplina: ");
        if (n < 1 || n > lista.size()) {
            throw new OperacionInvalidaException("La disciplina debe ser un número entre 1 y " + lista.size() + ".");
        }
        return lista.get(n - 1);
    }

    private void crearTorneo() {
        String codigo = lector.leerTexto("Código del torneo: ");
        String nombre = lector.leerTexto("Nombre: ");
        Disciplina d = elegirDisciplina();
        Torneo t = arena.crearTorneo(codigo, nombre, d);
        System.out.println("  Torneo creado: " + t);
    }

    private void cotizar() {
        Disciplina d = elegirDisciplina();
        int cantidad = lector.leerEntero("Cantidad de participantes: ");
        double total = arena.cotizar(d, cantidad);
        System.out.printf("  Cotización %s: %d x %s = %s (no se inscribió ni cobró nada)%n", d.getNombre(), cantidad,
                Validacion.monto(d.getCuotaInscripcion()), Validacion.monto(total));
    }

    private void inscribir() {
        String t = lector.leerTexto("Código del torneo: ");
        String p = lector.leerTexto("Código del participante: ");
        double cobro = arena.inscribir(t, p);
        System.out.println("  Inscripción realizada. Cobro: " + Validacion.monto(cobro));
    }

    private void cerrarInscripciones() {
        String t = lector.leerTexto("Código del torneo: ");
        arena.cerrarInscripciones(t);
        Torneo torneo = arena.buscarTorneo(t);
        System.out.println("  Inscripciones cerradas. Estado: " + torneo.getEstado() + ". Eventos generados:");
        for (Evento e : torneo.getEventos()) {
            System.out.println("   " + e.describir());
        }
    }

    private void programarEvento() {
        Torneo torneo = arena.buscarTorneo(lector.leerTexto("Código del torneo: "));
        listarEventos(torneo);
        int numero = lector.leerEntero("Número de evento (ej. 3 para " + torneo.getCodigo() + "-E3): ");
        LocalDate fecha = lector.leerFecha("Fecha (AAAA-MM-DD): ");
        int hora = lector.leerEntero("Hora de inicio (0-23): ");
        String sede = lector.leerTexto("Sede: ");
        arena.programarEvento(torneo.getCodigo(), numero, fecha, hora, sede);
        System.out.println("  Programado: " + torneo.buscarEvento(numero).describir());
    }

    private void registrarResultado() {
        Torneo torneo = arena.buscarTorneo(lector.leerTexto("Código del torneo: "));
        listarEventos(torneo);
        int numero = lector.leerEntero("Número de evento: ");
        arena.registrarResultado(torneo.getCodigo(), numero, lector);
        System.out.println("  Resultado registrado: " + torneo.buscarEvento(numero).describir());
        if (torneo.getEstado() == EstadoTorneo.FINALIZADO) {
            System.out.println("  ¡El torneo " + torneo.getCodigo() + " finalizó! Campeón: "
                    + torneo.getCampeon().getParticipante().getNombre() + " - Premio: "
                    + Validacion.monto(torneo.getPremio()));
        }
    }

    // --------------------------------------------------------------- consultas

    private void listarEventos(Torneo torneo) {
        if (torneo.getEventos().isEmpty()) {
            System.out.println("  El torneo no tiene eventos (estado: " + torneo.getEstado() + ").");
            return;
        }
        Calendario calendario = torneo.getCalendario();
        if (!calendario.estaVacio()) {
            System.out.println("  Eventos programados (orden cronológico):");
            for (Programable e : calendario) {
                System.out.println("   " + e.describir());
            }
        }
        if (calendario.cantidad() < torneo.getEventos().size()) {
            System.out.println("  Eventos sin programar:");
            for (Evento e : torneo.getEventos()) {
                if (!e.estaProgramado()) {
                    System.out.println("   " + e.describir());
                }
            }
        }
    }

    private void consultarTorneos() {
        System.out.println("  Torneos:");
        for (Torneo t : arena.getTorneos()) {
            System.out.println("   " + t);
        }
        String codigo = lector.leerTexto("Código para ver detalle (Enter para volver): ");
        if (codigo.isEmpty()) {
            return;
        }
        Torneo t = arena.buscarTorneo(codigo);
        System.out.println("  " + t);
        System.out.println("  Inscritos:");
        for (Inscripcion i : t.getTablaPosiciones()) {
            System.out.println("   " + i.getParticipante());
        }
        listarEventos(t);
    }

    private void consultarParticipantes() {
        System.out.println("  Participantes:");
        for (Participante p : arena.getParticipantes()) {
            StringBuilder torneos = new StringBuilder();
            for (Torneo t : arena.getTorneosDe(p)) {
                torneos.append(t.getCodigo()).append(' ');
            }
            System.out.println("   " + p + " | torneos: " + (torneos.length() == 0 ? "-" : torneos.toString().trim()));
        }
    }

    private void mostrarCalendario(String titulo, Calendario calendario) {
        System.out.println("  " + titulo + " (" + calendario.cantidad() + " eventos programados):");
        if (calendario.estaVacio()) {
            System.out.println("   (sin eventos programados)");
        }
        for (Programable e : calendario) {
            System.out.println("   " + e.describir());
        }
    }

    // ---------------------------------------------------------------- reportes

    private void reporteTabla() {
        Torneo t = arena.buscarTorneo(lector.leerTexto("Código del torneo: "));
        System.out.println("  Tabla de posiciones - " + t.getNombre() + " (" + t.getDisciplina().getNombre() + ", "
                + t.getEstado() + ")");
        System.out.printf("   %-3s %-20s %-12s %4s %4s %4s %4s%n", "#", "Participante", "Empresa", "EJ", "PTS", "DIF", "VIC");
        int pos = 1;
        for (Inscripcion i : t.getTablaPosiciones()) {
            System.out.printf("   %-3d %-20s %-12s %4d %4d %+4d %4d%n", pos++, i.getParticipante().getNombre(),
                    i.getParticipante().getEmpresa(), i.getEventosJugados(), i.getPuntosTabla(), i.getDiferencia(),
                    i.getVictorias());
        }
        if (t.getEstado() == EstadoTorneo.FINALIZADO) {
            System.out.println("  Campeón: " + t.getCampeon().getParticipante().getNombre() + " - Premio entregado: "
                    + Validacion.monto(t.getPremio()));
        } else {
            System.out.println("  El torneo aún no ha finalizado: no hay campeón.");
        }
    }

    private void reporteIngresos() {
        System.out.println("  Ingresos por inscripciones por disciplina:");
        for (Map.Entry<String, Double> e : arena.getIngresosPorDisciplina().entrySet()) {
            System.out.printf("   %-25s %12s%n", e.getKey(), Validacion.monto(e.getValue()));
        }
        System.out.printf("   %-25s %12s%n", "TOTAL", Validacion.monto(arena.getIngresosTotales()));
        System.out.println("  Premios entregados:");
        boolean alguno = false;
        for (Torneo t : arena.getTorneos()) {
            if (t.getEstado() == EstadoTorneo.FINALIZADO) {
                alguno = true;
                System.out.printf("   %s %-25s campeón: %-18s %12s%n", t.getCodigo(), t.getNombre(),
                        t.getCampeon().getParticipante().getNombre(), Validacion.monto(t.getPremio()));
            }
        }
        if (!alguno) {
            System.out.println("   (ningún torneo ha finalizado)");
        }
        System.out.printf("   %-25s %12s%n", "TOTAL PREMIOS", Validacion.monto(arena.getPremiosEntregados()));
    }

    private void reporteRanking() {
        System.out.println("  Ranking de empresas (puntos de tabla en todos los torneos):");
        int pos = 1;
        for (PuntajeEmpresa p : arena.getRankingEmpresas()) {
            System.out.printf("   %d. %-15s %4d pts%n", pos++, p.getEmpresa(), p.getPuntos());
        }
    }
}
