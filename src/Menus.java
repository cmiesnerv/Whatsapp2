import java.sql.Statement;
import java.util.Scanner;

public class Menus {

    public static void mostrarMenuInicio() {
        System.out.println("Elija una opción:" +
                "\n 1. Crear un nuevo usuario. " +
                "\n 2. Iniciar sesión con un usuario existente." +
                "\n 3. Salir.");
    }

    public static void menuInicio(Statement st) {
        Scanner s = new Scanner(System.in);
        String idUsuarioIniciado = "";
        int opc;
        mostrarMenuInicio();
        opc = s.nextInt();
        switch (opc){
            case 1 -> CRUD.recogerDatosEInsertar(st, 1, idUsuarioIniciado);
            case 2 -> {
                idUsuarioIniciado = CRUD.iniciarSesion(st);
                menuPrincipal(st, idUsuarioIniciado);
            }
            case 3 -> System.out.println("¡Gracias por utilizar Whatsapp2!\n¡Hasta luego! ;)");
        }
    }

    public static void mostrarMenuPrincipal() {
        System.out.println("Elija una opción:" +
                "\n 1. Ver contactos. " +
                "\n 2. Ver mensajes pendientes."+
                "\n 3. Añadir contacto." +
                "\n 4. Salir.");
    }

    public static void menuPrincipal(Statement st, String idUsuarioIniciado) {
        Scanner s = new Scanner(System.in);
        int opc;
        mostrarMenuInicio();
        opc = s.nextInt();
        switch (opc){
            case 1 -> System.out.println("");//Llamada a método para mostrar contactos
            case 2 -> System.out.println("");//Llamada a método mostrar mensajes pendientes
            case 3 -> CRUD.recogerDatosEInsertar(st, 2, idUsuarioIniciado);
            case 4 -> System.out.println("¡Gracias por utilizar Whatsapp2!\n¡Hasta luego! ;)");
        }
    }



    public static void mostrarMenuContacto() {
        System.out.println("Elija una opción:" +
                "\n 1. Ver/Iniciar conversación. " +
                "\n 2. Bloquear/Desbloquear" +
                "\n 3. Borrar contacto"+
                "\n 4. Volver a menú principal" +
                "\n 5. Salir.");
    }

    public static void menuContacto(Statement st) {
        Scanner s = new Scanner(System.in);
        int opc = 0;
        mostrarMenuInicio();
        opc = s.nextInt();
        switch (opc){
            case 1 -> System.out.println("");//Llamada a método para mostrar contactos
            case 2 -> System.out.println(""); //Llamada a método mostrar mensajes pendientes
            case 3 -> CRUD.recogerDatosEInsertar(st, 2);
            case 4 -> menuPrincipal(st);
            case 5 -> System.out.println("¡Gracias por utilizar Whatsapp2!\n¡Hasta luego! ;)");
        }
    }

}
