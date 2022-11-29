import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class CRUD {

    //Metodo para insertar datos en una tabla que elijamos
    //Recibe por parametros los campos a modificar, los datos a añadir y la tabla donde serán añadidos
    public static void insertarEnTabla(ArrayList<String> campos, ArrayList<String> datos, String tabla) {
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
            MainChat.st.executeUpdate(sql.toString());
        } catch (SQLException e) {
            System.out.println("El registro ya existe");
        }
    }

    //Método para cambiar el estado de bloqueo de un contacto
    //Recibe el nombre del usuario cuyo estado vamos a cambiar
    public static void bloquearDesbloquearContactos(String usuarioConectado) {
        Scanner s = new Scanner(System.in);
        StringBuilder sql = new StringBuilder("UPDATE ad2223_cmendoza.Contactos SET bloqueado=");
        String idUsuario2;
        int bloqueado = 2;

        System.out.println("Introduce el usuario a bloquear/desbloquear");
        idUsuario2 = s.nextLine();

        try {
            //Buscamos el contacto
            ResultSet rs = MainChat.st.executeQuery("SELECT * FROM ad2223_cmendoza.Contactos WHERE idUsuario1 LIKE '" + usuarioConectado + "' AND idUsuario2 LIKE '" + idUsuario2 + "'");

            ResultSetMetaData md = rs.getMetaData();
            //Comprobamos el estado de bloqueo y lo cambiamos
            while (rs.next()) {
                bloqueado = rs.getInt(md.getColumnLabel(3));
            }
            if (bloqueado==0) {
                bloqueado = 1;
            }else if(bloqueado==1){
                bloqueado = 0;
            }

            if (bloqueado != 2) {
                sql.append(bloqueado);

                sql.append(" WHERE idUsuario1 LIKE '").append(usuarioConectado).append("' AND idUsuario2 LIKE '").append(idUsuario2).append("'");

                MainChat.st.executeUpdate(sql.toString());

                System.out.println("Se ha bloqueado al usuario " + idUsuario2 + " en tus contactos");
            } else System.out.println("El usuario" + idUsuario2 + "no existe");
        } catch (SQLException e) {
            System.out.println("Hubo un error al borrar los datos de la base de datos");
        }
    }

    //Metodo para borrar un contacto de nuestra lista de contactos
    //Recibe por parametros el usuario que se ha conectado y le pide el nombre del contacto a borrar
    public static void borrarEnTablaContactos(String usuarioConectado) {
        Scanner s = new Scanner(System.in);
        String idUsuario2;

        System.out.println("Introduce el usuario a borrar");
        idUsuario2 = s.nextLine();

        String sql = "DELETE FROM ad2223_cmendoza.Contactos WHERE idUsuario1 LIKE '" + usuarioConectado + "' AND idUsuario2 LIKE '" + idUsuario2 + "'";

        try {
            MainChat.st.executeUpdate(sql);
            System.out.println("Se ha borrado el usuario " + idUsuario2 + " de tu lista de contactos");
        } catch (SQLException e) {
            System.out.println("Hubo un error al borrar los datos de la base de datos");
        }
    }

    //Metodo para recoger datos e insertarlos
    //Recibe la opcion a elegir y el usuario que ha iniciado sesion
    public static void recogerDatosEInsertar(int opc, String idUsuarioIniciado) {
        String tabla = "";
        boolean fallo = false;
        ArrayList<String> campos = new ArrayList<>();
        ArrayList<String> datos = new ArrayList<>();
        switch (opc) {
            //En la primera opcion creamos un nuevo usuario
            case 1 -> {
                campos.add("nombreUsuario");
                campos.add("contrasena");
                pedirDatos(datos, 1);
                tabla = "Usuario";
            }
            //En la segunda opcion añadimos un usuario a nuestra lista de contactos
            case 2 -> {
                campos.add("idUsuario1");
                campos.add("idUsuario2");
                datos.add("'" + idUsuarioIniciado + "'");
                pedirDatos(datos, 2);
                tabla = "Contactos";
                if (Objects.equals(datos.get(0), datos.get(1))) {
                    System.out.println("No puedes añadirte a ti mismo como contacto ;b");
                    fallo = true;
                }
            }

            default -> {
                fallo = true;
                System.out.println("se ha producio un error");
            }
        }
        if (!fallo) insertarEnTabla(campos, datos, tabla);
    }

    //Metodo para enviar un mensaje a un usuario
    //Recibe el usuario que inicia la sesion
    //Le pasamos los datos necesarios para la sentencia sql, le pedimos el mensaje al usuario y llamamos al metodo insertarEnTabla
    public static void enviarMensaje(String idUsuarioIniciado) {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> campos = new ArrayList<>();
        ArrayList<String> datos = new ArrayList<>();
        campos.add("idEmisor");
        campos.add("idReceptor");
        campos.add("texto");
        datos.add(idUsuarioIniciado);
        datos.add(pedirUsuarioReceptor(idUsuarioIniciado));
        System.out.println("Introduzca su mensaje:");
        datos.add("'" + sc.nextLine() + "'");
        insertarEnTabla(campos, datos, "Mensaje");
    }

    //Metodo para elegir a que usuario mandarle un mensaje
    //Comprueba si está o estás bloqueado, además de si existen los usuarios
    //En caso de estar bloqueado alguno de los usarios o no existir, no manda el mensaje
    public static String pedirUsuarioReceptor(String idUsuarioIniciado) {
        Scanner sc = new Scanner(System.in);
        String idReceptor;
        boolean contactoExiste = false;
        ResultSet rs, rs2;
        do {
            System.out.println("Introduzca el usuario al que desea enviar un mensaje");
            idReceptor = sc.nextLine();
            try {
                String sql = "SELECT * FROM ad2223_cmendoza.Contactos WHERE idUsuario2 LIKE '" + idReceptor + "'AND idUsuario1 LIKE '" + idUsuarioIniciado + "' AND bloqueado=0";
                rs = MainChat.st.executeQuery(sql);
                String sql2 = "SELECT * FROM ad2223_cmendoza.Contactos WHERE idUsuario2 LIKE '" + idUsuarioIniciado + "'AND idUsuario1 LIKE '" + idReceptor + "' AND bloqueado=0";
                rs2 = MainChat.st.executeQuery(sql2);

                if (!rs.next() || !rs2.next()) {
                    contactoExiste = true;
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (!contactoExiste) System.out.println("El contacto no existe o está bloqueado");
        } while (!contactoExiste);
        return idReceptor;
    }

    //Metodo para pedirle datos a un usario mediante scanner
    //Recibe un array de datos y la opcion a elegir
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

    //Metodo para inicar sesion
    //Pide un usuario y una contraseña
    //Comprueba si ya existe en la base de datos
    //Devuelve el nombre de usuario si este existe
    public static String iniciarSesion() {
        Scanner s = new Scanner(System.in);
        String usuario, contrasena;
        ResultSet rs;
        boolean salir = false, contrasenabien;
        System.out.println("Introduce tu usuario");
        usuario = s.nextLine();
        if (!selectTablaUsuario("nombreUsuario", usuario)) {
            usuario = "";
            System.out.println("El usuario introducido no existe");
            Menus.menuInicio();
            salir = true;
        }
        while (!salir) {
            System.out.println("Introduce tu contraseña");
            contrasena = s.nextLine();
            try {
                rs = MainChat.st.executeQuery("SELECT * FROM ad2223_cmendoza.Usuario WHERE nombreUsuario LIKE '"+usuario+"' AND contrasena LIKE '" + contrasena + "'");
                contrasenabien = rs.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (!contrasenabien) {
                System.out.println("La contraseña introducida no es correcta, inténtelo de nuevo");
            } else salir = true;
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
    public static boolean selectTablaUsuario(String campo, String dato) {
        boolean usuarioEncontrado;
        ResultSet rs;
        try {
            String sql = "SELECT * FROM ad2223_cmendoza.Usuario WHERE " + campo + " LIKE '" + dato + "'";
            rs = MainChat.st.executeQuery(sql);
            usuarioEncontrado = rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usuarioEncontrado;
    }

    //Metodo para mostrar la lista de contactos del usuario que entra por parametro
    public static void selectMostrarTablaContactos(String usuarioConectado) {
        int bloqueado;
        String cadena = "";
        ResultSet rs;
        System.out.println("----------LISTA DE CONTACTOS----------");
        try {
            String sql = "SELECT * FROM ad2223_cmendoza.Contactos WHERE idUsuario1 LIKE '" + usuarioConectado + "'";
            rs = MainChat.st.executeQuery(sql);

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
}
