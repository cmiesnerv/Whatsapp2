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
        int opc = 0;
        mostrarMenuInicio();
        opc = s.nextInt();
        if (opc == 1) {
            CRUD.recogerDatosEInsertar(st, 1);
        } else {
            //Llamada a método iniciar sesión
        }
    }

    public static void mostrarMenuPrincipal() {
        System.out.println("Elija una opción:" +
                "\n 1. Ver contactos. " +
                "\n 2. Ver mensajes pendientes."+
                "\n 3. Añadir contacto." +
                "\n 4. Salir.");
    }

    public static void menuPrincipal(Statement st) {
        Scanner s = new Scanner(System.in);
        int opc = 0;
        mostrarMenuInicio();
        opc = s.nextInt();
        if (opc == 1) {
            //Llamada a método para mostrar contactos
        } else if(opc== 2){
            //Llamada a método mostrar mensajes pendientes
        } else {
            CRUD.recogerDatosEInsertar(st, 2);
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


}
