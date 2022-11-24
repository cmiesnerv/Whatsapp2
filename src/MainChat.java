import com.mysql.cj.log.Log;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class MainChat {
    private static String servidor = "jdbc:mysql://dns11036.phdns11.es";
    private static Connection conexion;
    private static Statement statement = null;

    public static void conectar() {
        try {
            conexion = null;
            String password = "papitaFr1ta987";
            //String password = "1234";
            String usuario = "ad2223_cmendoza";
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(servidor, usuario, password);
            if (conexion != null) {
                statement = conexion.createStatement();
                System.out.println("Conexion a base de datos chat. ");
                System.out.println(statement.toString());

            } else {
                System.out.println("Conexión fallida");
            }

        } catch (ClassNotFoundException | SQLException e1) {
            e1.printStackTrace();
        }

    }

    //Crea una tabla. Si la tabla existe, la borra antes de intentar volver a crearla
    public static void crearTabla(String tabla, String[] campos) {
        // Creamos la tabla normal
        String drop = "DROP TABLE IF EXISTS ad2223_cmendoza." + tabla;
        String sql = "CREATE TABLE ad2223_cmendoza." + tabla + "(";

        // bucle para ir rellenando los campos
        for (int i = 0; i < campos.length; i++) {
            sql += campos[i];
            if (i < campos.length - 1) {
                // se van separando los campos por comas
                sql += ", ";
            }
        }
        // final de sql
        sql += ");";

        try {
            statement.executeUpdate(drop);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    public static void main(String[] args) {

        String camposUsuario[] = {"nombreUsuario varchar(45) PRIMARY KEY", " contraseña varchar(20)"};
        String camposContactos[] = {"idUsuario1 varchar(45) ", "idUsuario2 varchar(45)", "bloqueado tinyint default 0",
                "FOREIGN KEY (idUsuario1) REFERENCES Usuario(nombreUsuario) on delete cascade on update cascade",
                "FOREIGN KEY (idUsuario2) REFERENCES Usuario(nombreUsuario) on delete cascade on update cascade",
                "CONSTRAINT PK_Contactos PRIMARY KEY (idUsuario1, idUsuario2)"};
        String camposMensaje[] = {"idMensaje int AUTO_INCREMENT PRIMARY KEY", "idEmisor varchar(45)", "idReceptor varchar(45)",
                "leido tinyint default 0", "texto varchar(500)", "fechaHora timestamp DEFAULT CURRENT_TIMESTAMP",
                "FOREIGN KEY (idEmisor) REFERENCES Usuario(nombreUsuario) on delete cascade on update cascade",
                "FOREIGN KEY (idReceptor) REFERENCES Usuario(nombreUsuario) on delete cascade on update cascade"};

        conectar();

        //crearTabla("Mensaje", camposMensaje);


    }
}