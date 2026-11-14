import java.time.LocalDate;

/**
 * Carga los datos con los que inicia el programa, directamente y sin pasar
 * por la inscripción (por lo tanto sin ingresos ni premios registrados).
 *
 * Reglas observables con estos datos:
 *  - T01 Fútbol sala: victorias, empates y desempate por DIFERENCIA
 *    (Pumas, Leones y Tigres empatan con 4 puntos: +3, +2, -2).
 *  - T02 Fútbol sala con penales: encuentros definidos por penales (2 y 1
 *    puntos) y uno ganado en tiempo reglamentario (3 y 0).
 *  - T03 Baloncesto: desempate por VICTORIAS (Quetzales y Tigres: 4 pts,
 *    diferencia +8; Quetzales 2 victorias, Tigres 1).
 *  - T04 Voleibol: resultados 3-0, 3-1 y 3-2.
 *  - T05 Relevos: carrera programada lista para registrar su resultado.
 *  - T06 Fútbol sala en inscripción con 3 participantes (mínimo 4).
 */
public final class DatosIniciales {

    private DatosIniciales() {
    }

    public static void cargar(ArenaGT arena) {
        // Participantes: 12 participantes de 4 empresas
        arena.registrarParticipante("P01", "Tigres", "TecnoSoft");
        arena.registrarParticipante("P02", "Halcones", "TecnoSoft");
        arena.registrarParticipante("P03", "Leones", "Banco Maya");
        arena.registrarParticipante("P04", "Jaguares", "Banco Maya");
        arena.registrarParticipante("P05", "Quetzales", "AgroVerde");
        arena.registrarParticipante("P06", "Volcanes", "AgroVerde");
        arena.registrarParticipante("P07", "Pumas", "LogiExpress");
        arena.registrarParticipante("P08", "Coyotes", "LogiExpress");
        arena.registrarParticipante("R01", "Cuarteto Rayo", "TecnoSoft");
        arena.registrarParticipante("R02", "Cuarteto Trueno", "Banco Maya");
        arena.registrarParticipante("R03", "Cuarteto Viento", "AgroVerde");
        arena.registrarParticipante("R04", "Cuarteto Centella", "LogiExpress");

        // Torneos: al menos uno por disciplina (catálogo en orden: 0..4)
        arena.crearTorneo("T01", "Copa Fútbol Sala 2026", arena.getDisciplinas().get(0));
        arena.crearTorneo("T02", "Copa Penales 2026", arena.getDisciplinas().get(1));
        arena.crearTorneo("T03", "Copa Baloncesto 2026", arena.getDisciplinas().get(2));
        arena.crearTorneo("T04", "Copa Voleibol 2026", arena.getDisciplinas().get(3));
        arena.crearTorneo("T05", "Relevos ArenaGT 2026", arena.getDisciplinas().get(4));
        arena.crearTorneo("T06", "Copa Navidad Fútbol Sala", arena.getDisciplinas().get(0));

        inscribir(arena, "T01", "P01", "P03", "P05", "P07");
        inscribir(arena, "T02", "P02", "P04", "P06", "P08");
        inscribir(arena, "T03", "P01", "P04", "P05", "P08");
        inscribir(arena, "T04", "P02", "P03", "P06", "P07");
        inscribir(arena, "T05", "R01", "R02", "R03", "R04");
        inscribir(arena, "T06", "P02", "P04", "P06");

        for (String t : new String[] {"T01", "T02", "T03", "T04", "T05"}) {
            arena.cerrarInscripciones(t);
        }

        // Programación de TODOS los eventos de los torneos en juego (sin conflictos)
        prog(arena, "T01", 1, "2026-11-09", 9, "Gimnasio Central");
        prog(arena, "T02", 1, "2026-11-09", 9, "Domo Zona 13");
        prog(arena, "T03", 1, "2026-11-09", 11, "Gimnasio Central");
        prog(arena, "T04", 1, "2026-11-09", 11, "Domo Zona 13");
        prog(arena, "T01", 2, "2026-11-09", 15, "Gimnasio Central");
        prog(arena, "T02", 2, "2026-11-09", 15, "Domo Zona 13");
        prog(arena, "T03", 2, "2026-11-09", 17, "Gimnasio Central");
        prog(arena, "T04", 2, "2026-11-09", 17, "Domo Zona 13");
        prog(arena, "T01", 3, "2026-11-10", 9, "Gimnasio Central");
        prog(arena, "T02", 3, "2026-11-10", 9, "Domo Zona 13");
        prog(arena, "T03", 3, "2026-11-10", 11, "Gimnasio Central");
        prog(arena, "T04", 3, "2026-11-10", 11, "Domo Zona 13");
        prog(arena, "T01", 4, "2026-11-10", 15, "Gimnasio Central");
        prog(arena, "T02", 4, "2026-11-10", 15, "Domo Zona 13");
        prog(arena, "T03", 4, "2026-11-10", 17, "Gimnasio Central");
        prog(arena, "T04", 4, "2026-11-10", 17, "Domo Zona 13");
        prog(arena, "T01", 5, "2026-11-11", 9, "Gimnasio Central");
        prog(arena, "T02", 5, "2026-11-11", 9, "Domo Zona 13");
        prog(arena, "T03", 5, "2026-11-11", 11, "Gimnasio Central");
        prog(arena, "T04", 5, "2026-11-11", 11, "Domo Zona 13");
        prog(arena, "T01", 6, "2026-11-11", 15, "Gimnasio Central");
        prog(arena, "T02", 6, "2026-11-11", 15, "Domo Zona 13");
        prog(arena, "T03", 6, "2026-11-11", 17, "Gimnasio Central");
        prog(arena, "T04", 6, "2026-11-11", 17, "Domo Zona 13");
        prog(arena, "T05", 1, "2026-11-11", 17, "Pista Olímpica");

        // T01 Fútbol sala (Tigres, Leones, Quetzales, Pumas)
        res(arena, "T01", 1, "2", "1");      // Tigres 2-1 Leones
        res(arena, "T01", 2, "1", "1");      // Tigres 1-1 Quetzales (empate)
        res(arena, "T01", 3, "0", "3");      // Tigres 0-3 Pumas
        res(arena, "T01", 4, "3", "0");      // Leones 3-0 Quetzales
        res(arena, "T01", 5, "2", "2");      // Leones 2-2 Pumas (empate)

        // T02 Fútbol sala con penales (Halcones, Jaguares, Volcanes, Coyotes)
        res(arena, "T02", 1, "2", "2", "s", "4", "3"); // Halcones gana por penales: 2 y 1
        res(arena, "T02", 2, "3", "1", "n");      // Halcones gana en tiempo regl.: 3 y 0
        res(arena, "T02", 3, "1", "1", "s", "2", "4"); // Coyotes gana por penales
        res(arena, "T02", 4, "0", "2", "n");      // Volcanes gana en tiempo regl.

        // T03 Baloncesto (Tigres, Jaguares, Quetzales, Coyotes)
        res(arena, "T03", 1, "70", "55");    // Tigres gana
        res(arena, "T03", 2, "50", "55");    // Quetzales gana a Tigres
        res(arena, "T03", 3, "48", "50");    // Coyotes gana a Tigres
        res(arena, "T03", 4, "57", "60");    // Quetzales gana a Jaguares

        // T04 Voleibol (Halcones, Leones, Volcanes, Pumas)
        res(arena, "T04", 1, "3", "0");      // 3-0: 3 y 0
        res(arena, "T04", 2, "3", "1");      // 3-1: 3 y 0
        res(arena, "T04", 3, "2", "3");      // 2-3: 1 y 2
        res(arena, "T04", 4, "3", "2");      // 3-2: 2 y 1
    }

    private static void inscribir(ArenaGT arena, String torneo, String... participantes) {
        for (String p : participantes) {
            arena.inscribirSinCobro(torneo, p);
        }
    }

    private static void prog(ArenaGT arena, String torneo, int evento, String fecha, int hora, String sede) {
        arena.programarEvento(torneo, evento, LocalDate.parse(fecha), hora, sede);
    }

    private static void res(ArenaGT arena, String torneo, int evento, String... valores) {
        arena.registrarResultado(torneo, evento, new LectorPredefinido(valores));
    }
}
