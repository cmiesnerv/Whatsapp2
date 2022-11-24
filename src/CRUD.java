import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

public class CRUD {

    public static void insertarEnTabla(Statement st, ArrayList<String> campos, ArrayList<String> datos, String tabla) {
        StringBuilder sql = new StringBuilder("INSERT INTO ad2223_cmendoza." + tabla + " (");
        for (int i = 0; i< campos.size()-1; i++){
            sql.append(campos.get(i)).append(", ");
        }
        sql.append(campos.get(campos.size()-1)).append(") VALUES (");
        for (int i = 0; i< datos.size()-1; i++){
            sql.append(datos.get(i)).append(", ");
        }
        sql.append(datos.get(datos.size()-1)).append(");");
        try {
            st.executeUpdate(sql.toString());
        } catch (SQLException e) {
            System.out.println("Hubo un error al enviar los datos a la base de datos");
        }
    }

    public static void recogerDatosEInsertar(Statement st, int opc) {
        Scanner sc = new Scanner(System.in);
        String tabla = "";
        boolean fallo = false;
        ArrayList<String> campos = new ArrayList<>();
        ArrayList<String> datos = new ArrayList<>();
        switch (opc) {
            case 1 -> {
                campos.add("nombreUsuario");
                campos.add("contraseña");
                System.out.println("Introduzca un nombre de usuario");
                datos.add("'" + sc.nextLine() + "'");
                System.out.println("Introduzca una contraseña");
                datos.add("'" + sc.nextLine() + "'");
                tabla = "Usuario";
            }
            case 2 -> {
                campos.add("idUsuario1");
                campos.add("idUsuario2");
                //datos.add("idUsuario1"); BUSCAR LA FORMA DE SABER EL USUARIO QUE ESTA USANDO EL PROGRAMA
                System.out.println("Introduzca un nombre de usuario");
                datos.add("'" + sc.nextLine() + "'");
                tabla = "Contactos";
            }
            case 3 -> {
                campos.add("idEmisor");
                campos.add("idReceptor");
                campos.add("texto");
                //datos.add("idEmisor"); BUSCAR LA FORMA DE SABER EL USUARIO QUE ESTA UTILIZANDO LA APP
                //datos.add("idReceptor"); BUSCAR LA FORMA DE SABER EL USUARIO QUE RECIBE EL MENSAJE
                System.out.println("Introduzca su mensaje:");
                datos.add("'" + sc.nextLine() + "'");
                tabla = "Mensaje";
            }
            default -> {
                fallo = true;
                System.out.println("se ha producio un error");
            }
        }
        if (!fallo) insertarEnTabla(st, campos, datos, tabla);
    }

    public static ArrayList<String> pedirDatos(Statement st, int opc){
        Scanner sc = new Scanner(System.in);
        ArrayList<String> datos = new ArrayList<>();
        String dato ="";
        switch (opc){
            case 1->{
                do{

                }while (comprobarDatosIntroducidos(st, 1, "'nombreUsuario'", dato));
                System.out.println("Introduzca un nombre de usuario");
                datos.add("'" + sc.nextLine() + "'");
                System.out.println("Introduzca una contraseña");
                dato = "'" + sc.nextLine() + "'";
                datos.add("'" + sc.nextLine() + "'");
            }
            case 2->{
                System.out.println("Introduzca un nombre de usuario");
                datos.add("'" + sc.nextLine() + "'");
            }
            case 3->{
                System.out.println("Introduzca su mensaje:");
                datos.add("'" + sc.nextLine() + "'");
            }
        }
        return datos;
    }

    public static boolean comprobarDatosIntroducidos(Statement st, int opc, String campo, String dato){
        boolean datosCorrectos = false;

        switch (opc){
            case 1->{
                datosCorrectos = !selectTablaUsuario(st, campo, dato);
            }
            case 2->{
                datosCorrectos = selectTablaUsuario(st, campo, dato);
            }
        }
        return datosCorrectos;
    }

    public static boolean selectTablaUsuario(Statement st, String campo, String dato){
        boolean usuarioEncontrado;
        try {
            String sql = "SELECT * FROM ad2223_cmendoza.Usuario WHERE " + campo +" LIKE "+ dato;
            st.executeQuery(sql);
            usuarioEncontrado = true;
        } catch (SQLException e) {
            usuarioEncontrado = false;
        }
        return usuarioEncontrado;
    }
}
