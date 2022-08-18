package playground;

import java.sql.*;

public class Test {
    private static final String SQL_TABLE_CREATE= "DROP TABLE IF EXISTS USUARIO; CREATE TABLE USUARIO " +
            "(" +
            "ID INT PRIMARY KEY, " +
            "NOMBRE VARCHAR(100) NOT NULL, " +
            "EMAIL VARCHAR(100) NOT NULL, " +
            "SUELDO NUMERIC(15,2) NOT NULL" +
            ")";

    private static final String SQL_INSERT = "INSERT INTO USUARIO(ID,NOMBRE, EMAIL, SUELDO) VALUES(?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE USUARIO SET SUELDO=? WHERE EMAIL=?";

    public static void main(String[] args) throws Exception{

        // crea el usuario -> establece la conexion -> se crea la sentencia -> se interactua con la BD

        //usuario
        Usuario usuario = new Usuario("cosme", "fulanito@mail.com", 2000d);

        //conexion
        Connection connection = null;

        try {
            connection = getConnection();

            //sentencia crear tabla
            Statement statement=connection.createStatement();
            statement.execute(SQL_TABLE_CREATE);

            //inicio sentencia PreparedStatement, seteo los datos y finalmente la ejecuto.
            PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT);

            psInsert.setInt(1,1);
            psInsert.setString(2, usuario.getNombre());
            psInsert.setString(3, usuario.getEmail());
            psInsert.setDouble(4,usuario.getSueldo());

            psInsert.execute();

            //actualización de sueldo -> al ser una transacción debe interrumpir el autocommit
            //se repite el proceso de inicio sentencia, seteo los datos y ejecuto
            connection.setAutoCommit(false);

            PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE);
            psUpdate.setDouble(1, usuario.subirSueldo(10d));
            psUpdate.setString(2, usuario.getEmail());
            psUpdate.execute();
            connection.commit(); //hacemos el commit manualmente

            connection.setAutoCommit(true); // volvemos a activar el autocommit



            String sql = "SELECT * FROM USUARIO";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                System.out.println(rs.getInt(1)+rs.getString(2)+rs.getString(3)+rs.getDouble(4));
            }





        }catch (Exception e){
            e.printStackTrace();
            connection.rollback();

        }finally {
            connection.close();

        }
    }

    private static Connection getConnection() throws Exception{
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:~/backend-clase13pg", "sa", "sa");
    }
}
