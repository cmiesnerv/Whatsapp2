import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
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
            System.out.println("El registro ya existe");
        }
    }

    public static void bloquearDesbloquearContactos(Statement st, String usuarioConectado) {
        Scanner s = new Scanner(System.in);
        StringBuilder sql = new StringBuilder("UPDATE ad2223_cmendoza.Contactos SET bloqueado=");
        String idUsuario2;
        int bloqueado = 2;

        System.out.println("Introduce el usuario a bloquear/desbloquear");
        idUsuario2 = s.nextLine();

        try {
            ResultSet rs = st.executeQuery("SELECT * FROM ad2223_cmendoza.Contactos WHERE idUsuario1 LIKE '" + usuarioConectado + "' AND idUsuario2 LIKE '" + idUsuario2 +"'");

            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                bloqueado = rs.getInt(md.getColumnLabel(3));
            }
            if (bloqueado==0) {
                bloqueado = 1;
            }else if(bloqueado==1){
                bloqueado = 0;
            }

            if (bloqueado!=2){
            sql.append(bloqueado);

            sql.append(" WHERE idUsuario1 LIKE '").append(usuarioConectado).append("' AND idUsuario2 LIKE '").append(idUsuario2).append("'");

            st.executeUpdate(sql.toString());

            System.out.println("Se ha bloqueado al usuario " + idUsuario2 + " en tus contactos");
            }else System.out.println("El usuario" + idUsuario2 + "no existe");
        } catch (SQLException e) {
            System.out.println("Hubo un error al borrar los datos de la base de datos");
        }
    }

    public static void borrarEnTablaContactos(Statement st, String usuarioConectado) {
        Scanner s = new Scanner(System.in);
        String idUsuario2;

        System.out.println("Introduce el usuario a borrar");
        idUsuario2 = s.nextLine();

        String sql = "DELETE FROM ad2223_cmendoza.Contactos WHERE idUsuario1 LIKE '" + usuarioConectado + "' AND idUsuario2 LIKE '" + idUsuario2 +"'";

        try {
            st.executeUpdate(sql);
            System.out.println("Se ha borrado el usuario " + idUsuario2 + " de tu lista de contactos");
        } catch (SQLException e) {
            System.out.println("Hubo un error al borrar los datos de la base de datos");
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
                datos.add("'"+idUsuarioIniciado+"'");
                pedirDatos(datos, 2);
                tabla = "Contactos";
                if (Objects.equals(datos.get(0), datos.get(1))){
                    System.out.println("No puedes añadirte a ti mismo como contacto ;b");
                    fallo = true;
                }
            }

            default -> {
                fallo = true;
                System.out.println("se ha producio un error");
            }
        }
        if (!fallo) insertarEnTabla(st, campos, datos, tabla);
    }

    public static void enviarMensaje(Statement st, int opc, String idUsuarioIniciado, String idReceptor) {
        Scanner sc = new Scanner(System.in);
        String tabla = "";
        ArrayList<String> campos = new ArrayList<>();
        ArrayList<String> datos = new ArrayList<>();
        campos.add("idEmisor");
        campos.add("idReceptor");
        campos.add("texto");
        datos.add(idUsuarioIniciado);
        datos.add(idReceptor);
        System.out.println("Introduzca su mensaje:");
        datos.add("'" + sc.nextLine() + "'");
        tabla = "Mensaje";
        insertarEnTabla(st, campos, datos, tabla);
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
        boolean salir = false;
        System.out.println("Introduce tu usuario");
        usuario = s.nextLine();
        if (!selectTablaUsuario(st, "nombreUsuario", usuario)) {
            usuario = "";
            System.out.println("El usuario introducido no existe");
            Menus.menuInicio(st);
            salir = true;
        }
        while (!salir) {
            System.out.println("Introduce tu contraseña");
            contrasena = s.nextLine();
            if (!selectTablaUsuario(st, "contrasena", contrasena)) {
                System.out.println("La contraseña introducido no es correcta, inténtelo de nuevo");
            }else salir = true;
        }
        return usuario;
    }

    /**
     * Método que hace un Select de la taba usuario comprobando que el registro con el dato introducido existe
     *
     * @param campo campo al que corresponde el dato que se introduce
     * @param dato  dato para filtrar el select
     * @return un booleano verdadero si se encuentran registros con el dato introducido y falso si no se encuentran registros
     */
    public static boolean selectTablaUsuario(Statement st, String campo, String dato) {
        boolean usuarioEncontrado;
        ResultSet rs;
        try {
            String sql = "SELECT * FROM ad2223_cmendoza.Usuario WHERE " + campo + " LIKE '" + dato + "'";
            rs = st.executeQuery(sql);
            usuarioEncontrado = true;
        } catch (SQLException e) {
            usuarioEncontrado = false;
        }
        return usuarioEncontrado;
    }

    public static void selectMostrarTablaContactos(Statement st, String usuarioConectado) {
        int bloqueado;
        String cadena = "";
        ResultSet rs;
        System.out.println("----------LISTA DE CONTACTOS----------");
        try {
            String sql = "SELECT * FROM ad2223_cmendoza.Contactos WHERE idUsuario1 LIKE '" + usuarioConectado + "'";
            rs = st.executeQuery(sql);

            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                cadena = rs.getString(md.getColumnLabel(2));
                bloqueado = rs.getInt(md.getColumnLabel(3));
                if (bloqueado == 1) cadena += " -> BLOQUEADO";
                System.out.println(cadena);
            }
            if (Objects.equals(cadena, "")) System.out.println("No hay contactos");
            System.out.println("--------------------------------------");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void mostrarDatos(ResultSet rs) {
        try {
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                StringBuilder cadena = new StringBuilder();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    cadena.append(md.getColumnLabel(i).toUpperCase()).append(": ").append(rs.getString(md.getColumnLabel(i))).append("    ");
                }
                System.out.println(cadena);
            }
        } catch (SQLException e) {
            System.out.println("Error: Clase CentroSalud, método mostrarDatos");
            e.printStackTrace();
        }
    }
}
