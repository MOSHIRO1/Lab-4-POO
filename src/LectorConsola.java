import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Lectura de datos desde la consola. Si el formato es incorrecto muestra un
 * mensaje y vuelve a pedir el dato, de modo que el programa nunca termina
 * por una entrada mal escrita.
 */
public class LectorConsola implements LectorResultado {

    private final Scanner scanner;

    public LectorConsola(Scanner scanner) {
        this.scanner = scanner;
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    @Override
    public int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("  Formato inválido: escriba un número entero.");
            }
        }
    }

    @Override
    public double leerDecimal(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje).replace(',', '.');
            try {
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                System.out.println("  Formato inválido: escriba un número (ej. 50.1).");
            }
        }
    }

    @Override
    public boolean leerSiNo(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje).toLowerCase();
            if (texto.equals("s") || texto.equals("si") || texto.equals("sí")) {
                return true;
            }
            if (texto.equals("n") || texto.equals("no")) {
                return false;
            }
            System.out.println("  Responda 's' o 'n'.");
        }
    }

    public LocalDate leerFecha(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                System.out.println("  Formato inválido: use AAAA-MM-DD (ej. 2026-11-20).");
            }
        }
    }
}
