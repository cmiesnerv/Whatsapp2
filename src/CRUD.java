import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

public class CRUD {

    public static void insertarEnTabla(Statement st, ArrayList<String> campos, ArrayList<String> datos, String tabla) {
        StringBuilder sql = new StringBuilder("INSERT INTO ad2223_cmendoza." + tabla + " (");
        for (int i = 0; i < campos.size() - 1; i++) {
            sql.append(campos.get(i)).append(", ");
        }
        sql.append(campos.get(campos.size() - 1)).append(") VALUES (");
        for (int i = 0; i < datos.size() - 1; i++) {
            sql.append(datos.get(i)).append(", ");
        }
        sql.append(datos.get(datos.size() - 1)).append(");");
        try {
            st.executeUpdate(sql.toString());
        } catch (SQLException e) {
            System.out.println("Hubo un error al enviar los datos a la base de datos");
        }
    }

    public static void borrarEnTabla(Statement st, String valorId, String campoId, String tabla) {
        StringBuilder sql = new StringBuilder("DELETE FROM ad2223_cmendoza." + tabla + " WHERE " + campoId + " LIKE " + valorId);

        try {
            st.executeUpdate(sql.toString());
        } catch (SQLException e) {
            System.out.println("Hubo un error al enviar los datos a la base de datos");
        }
    }

    public static void recogerDatosEInsertar(Statement st, int opc, String idUsuarioIniciado) {
        String tabla = "";
        boolean fallo = false;
        ArrayList<String> campos = new ArrayList<>();
        ArrayList<String> datos = new ArrayList<>();
        switch (opc) {
            case 1 -> {
                campos.add("nombreUsuario");
                campos.add("contrasena");
                pedirDatos(datos, 1);
                tabla = "Usuario";
            }
            case 2 -> {
                campos.add("idUsuario1");
                campos.add("idUsuario2");
                //datos.add("idUsuario1"); BUSCAR LA FORMA DE SABER EL USUARIO QUE ESTA USANDO EL PROGRAMA
                pedirDatos(datos, 2);
                tabla = "Contactos";
            }
            case 3 -> {
                campos.add("idEmisor");
                campos.add("idReceptor");
                campos.add("texto");
                //datos.add("idEmisor"); BUSCAR LA FORMA DE SABER EL USUARIO QUE ESTA UTILIZANDO LA APP
                //datos.add("idReceptor"); BUSCAR LA FORMA DE SABER EL USUARIO QUE RECIBE EL MENSAJE
                pedirDatos(datos, 3);
                tabla = "Mensaje";
            }
            default -> {
                fallo = true;
                System.out.println("se ha producio un error");
            }
        }
        if (!fallo) insertarEnTabla(st, campos, datos, tabla);
    }

    public static ArrayList<String> pedirDatos(ArrayList<String> datos, int opc) {
        Scanner sc = new Scanner(System.in);
        switch (opc) {
            case 1 -> {
                System.out.println("Introduzca un nombre de usuario");
                datos.add("'" + sc.nextLine() + "'");
                System.out.println("Introduzca una contraseña");
                datos.add("'" + sc.nextLine() + "'");
            }
            case 2 -> {
                System.out.println("Introduzca un nombre de usuario");
                datos.add("'" + sc.nextLine() + "'");
            }
            case 3 -> {
                System.out.println("Introduzca su mensaje:");
                datos.add("'" + sc.nextLine() + "'");
            }
        }
        return datos;
    }

    public static String iniciarSesion(Statement st) {
        Scanner s = new Scanner(System.in);
        String usuario, contrasena;
        boolean iniciarSesion = true;
        do {
            System.out.println("Introduce tu usuario");
            usuario = s.nextLine();
            System.out.println("Introduce tu contraseña");
            contrasena = s.nextLine();
            if (!selectTablaUsuario(st, "nombreUsuario", usuario)){
                System.out.println("El usuario introducido no existe, inténtelo de nuevo");
                iniciarSesion = false;
            }else if (!selectTablaUsuario(st, "contrasena", contrasena)){
                System.out.println("La contraseña introducido no es correcta, inténtelo de nuevo");
                iniciarSesion = false;
            }
        } while (!iniciarSesion);
        return usuario;
    }

    /**
     * Método que hace un Select de la taba usuario comprobando que el registro con el dato introducido existe
     * @param campo campo al que corresponde el dato que se introduce
     * @param dato dato para filtrar el select
     * @return un booleano verdadero si se encuentran registros con el dato introducido y falso si no se encuentran registros
     */
    public static boolean selectTablaUsuario(Statement st, String campo, String dato) {
        boolean usuarioEncontrado;
        ResultSet rs;
        try {
            String sql = "SELECT * FROM ad2223_cmendoza.Usuario WHERE " + campo + " LIKE " + dato;
            rs = st.executeQuery(sql);
            usuarioEncontrado = rs.getRow() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usuarioEncontrado;
    }
}
