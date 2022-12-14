import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Menus {

    //Aquí estan los diferentes menus que encontrara el usuario a lo largo de la app
    public static void mostrarMenuInicio() {
        System.out.println("Elija una opción:" +
                "\n 1. Crear un nuevo usuario. " +
                "\n 2. Iniciar sesión con un usuario existente." +
                "\n 3. Salir.");
    }

    //Metodo que realiza la accion elegida por el usuario en el menu incio
    public static void menuInicio() {
        Scanner s = new Scanner(System.in);
        String idUsuarioIniciado = "";
        int opc;
        mostrarMenuInicio();
        opc = s.nextInt();
        switch (opc) {
            case 1 -> {
                CRUD.recogerDatosEInsertar(1, idUsuarioIniciado);
                menuInicio();
            }
            case 2 -> {
                idUsuarioIniciado = CRUD.iniciarSesion();
                if (!Objects.equals(idUsuarioIniciado, "")) {

                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override public void run() {
                            System.out.println("¡Ejecutando la primera vez en " +   "1 segundo y las demás cada 5 segundos!"); }
                    }, 1000, 5000);


                    menuPrincipal(idUsuarioIniciado);
                }
            }
            case 3 -> System.out.println("¡Gracias por utilizar Whatsapp2!\n¡Hasta luego! ;)");
        }
    }

    public static void mostrarMenuPrincipal() {
        System.out.println("Elija una opción:" +
                "\n 1. Ver menu contactos. " +
                "\n 2. Ver mensajes pendientes." +
                "\n 3. Añadir contacto." +
                "\n 4. Salir.");
    }

    //Metodo que realiza la accion elegida por el usuario en el menu principal tras iniciar sesion
    public static void menuPrincipal(String idUsuarioIniciado) {
        Scanner s = new Scanner(System.in);
        int opc;
        mostrarMenuPrincipal();
        opc = s.nextInt();
        switch (opc) {
            case 1 -> menuContacto(idUsuarioIniciado);
            case 2 -> System.out.println("");//Llamada a método mostrar mensajes pendientes
            case 3 -> {
                CRUD.recogerDatosEInsertar(2, idUsuarioIniciado);
                menuPrincipal(idUsuarioIniciado);
            }
            case 4 -> System.out.println("¡Gracias por utilizar Whatsapp2!\n¡Hasta luego! ;)");
        }
    }


    public static void mostrarMenuContacto() {
        System.out.println("Elija una opción:" +
                "\n 1. Ver lista contactos" +
                "\n 2. Escribir mensaje " +
                "\n 3. Bloquear/Desbloquear" +
                "\n 4. Borrar contacto" +
                "\n 5. Ver conversacion" +
                "\n 6. Volver a menú principal" +
                "\n 7. Salir.");
    }

    //Metodo que realiza la accion elegida por el usuario en el menu contacto
    public static void menuContacto(String idUsuarioIniciado) {
        Scanner s = new Scanner(System.in);
        int opc, seguir = 0;
        mostrarMenuContacto();
        opc = s.nextInt();
        switch (opc) {
            case 1 -> CRUD.selectMostrarTablaContactos(idUsuarioIniciado);
            case 2 -> CRUD.enviarMensaje(idUsuarioIniciado);
            case 3 -> CRUD.bloquearDesbloquearContactos(idUsuarioIniciado);
            case 4 -> CRUD.borrarEnTablaContactos(idUsuarioIniciado);
            case 5 -> System.out.println("");//Llamada metodo ver conver
            case 6 -> {
                menuPrincipal(idUsuarioIniciado);
                seguir = 1;
            }
            case 7 -> {
                System.out.println("¡Gracias por utilizar Whatsapp2!\n¡Hasta luego! ;)");
                seguir = 1;
            }
        }
        if (seguir == 0) menuContacto(idUsuarioIniciado);
    }

}
