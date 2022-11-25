import java.sql.*;

public class MainChat {
    private static final String servidor = "jdbc:mysql://dns11036.phdns11.es";
    private static Connection con;
    private static Statement st = null;

    public static void conectar() {
        try {
            con = null;
            String password = "papitaFr1ta987";
            //String password = "1234";
            String usuario = "ad2223_cmendoza";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(servidor, usuario, password);
            if (con != null) {
                st = con.createStatement();

            } else {
                System.out.println("Conexión fallida");
            }

        } catch (ClassNotFoundException | SQLException e1) {
            e1.printStackTrace();
        }

    }

    //Crea una tabla. Antes de crear la tabla, comprueba que esta no exista mediante un select; si existe no ejecuta create table
    public static void crearTabla(String[] info) {
        boolean tablaExiste = true;
        //Intenta hacer un SELECT en la tabla a crear, de forma que si esta no existe lanzará una excepción
        try {
            st.executeQuery("SELECT * FROM ad2223_cmendoza." + info[0]);
            //Si la tabla no existe, la excepción lanzada es capturada por el catch, que pone el booleano a false
        } catch (SQLException e) {
            tablaExiste = false;
        }
        //Si no se ha podido realizar la consulta anterior, entra en el if
        if (!tablaExiste){
            StringBuilder sql = new StringBuilder("CREATE TABLE ad2223_cmendoza." + info[0] + "(");
            //El bucle va leyendo los campos y añadiéndolos al StringBuilder
            for (int i = 1; i < info.length; i++) {
                sql.append(info[i]);
                //Comprueba que el campo añadido no sea el último y, si no lo es, añade coma detrás de este
                if (i < info.length - 1) {
                    sql.append(", ");
                }
            }
            //Al acabar de rellenar los campos cierra la consulta con ");"
            sql.append(");");
            //Crea la tabla
            try {
                st.executeUpdate(sql.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) {
        String[] infoUsuario = {"Usuario", "nombreUsuario varchar(45) PRIMARY KEY", " contrasena varchar(20)"},
                 infoContactos = {"Contactos", "idUsuario1 varchar(45) ", "idUsuario2 varchar(45)", "bloqueado tinyint default 0",
                         "FOREIGN KEY (idUsuario1) REFERENCES Usuario(nombreUsuario) on delete cascade on update cascade",
                         "FOREIGN KEY (idUsuario2) REFERENCES Usuario(nombreUsuario) on delete cascade on update cascade",
                         "CONSTRAINT PK_Contactos PRIMARY KEY (idUsuario1, idUsuario2)"},
                infoMensaje = {"Mensaje", "idMensaje int AUTO_INCREMENT PRIMARY KEY", "idEmisor varchar(45)", "idReceptor varchar(45)",
                        "leido tinyint default 0", "texto varchar(500)", "fechaHora timestamp DEFAULT CURRENT_TIMESTAMP",
                        "FOREIGN KEY (idEmisor) REFERENCES Usuario(nombreUsuario) on delete cascade on update cascade",
                        "FOREIGN KEY (idReceptor) REFERENCES Usuario(nombreUsuario) on delete cascade on update cascade"};
        //Conecta con la base de datos
        conectar();
        //Crea las tablas si no existen
        crearTabla(infoUsuario);
        crearTabla(infoContactos);
        crearTabla(infoMensaje);
        //Muestra primer menu
        Menus.menuInicio(st);


    }
}