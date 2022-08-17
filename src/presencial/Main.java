package presencial;

import java.sql.*;

public class Main {
    private static final String SQL_CREATE_TABLE = "drop table if exists cuentas; create table cuentas(id int primary key, nro_cuenta int not null, nombre varchar(100) not null, saldo numeric(10,2) not null)";
    private static final String SQL_INSERT = "insert into cuentas (id, nro_cuenta, nombre, saldo) values(?,?,?,?)";
    private static final String SQL_UPDATE = "update cuentas set saldo=? where id=?";
    private static final String SQL_SELECT = "select * from cuentas";

    public static void main(String[] args) {
        Cuenta cuenta = new Cuenta(1, 800, "CajaAhorro", 10d);
        Connection connection = null;
        try {
            connection = getConnection();
            //crear la tabla
            Statement statement = connection.createStatement();
            statement.execute(SQL_CREATE_TABLE);

            //insertar la cuenta en la linea 20
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT);
            ps.setInt(1, cuenta.getId());
            ps.setInt(2, cuenta.getNroCuenta());
            ps.setString(3, cuenta.getNombre());
            ps.setDouble(4, cuenta.getSaldo());
            ps.executeUpdate();

            //primera actualización
            PreparedStatement psUpdate= connection.prepareStatement(SQL_UPDATE);
            psUpdate.setDouble(1, cuenta.getSaldo()+10);
            psUpdate.setInt(2, cuenta.getId());
            psUpdate.executeUpdate();

            // commit ejemplo con una seguna actualización
            connection.setAutoCommit(false);
            PreparedStatement psUpdate2= connection.prepareStatement(SQL_UPDATE);
            psUpdate2.setDouble(1, cuenta.getSaldo()+25);
            psUpdate2.setInt(2, cuenta.getId());
            psUpdate2.executeUpdate();
            connection.commit();

            connection.setAutoCommit(true);

            ResultSet rs=statement.executeQuery(SQL_SELECT);
            while (rs.next()){
                System.out.println("id: " + rs.getInt(1)+ "- Saldo: " + rs.getDouble(4));
            }



        } catch (Exception e) {
            if(connection!=null){
                try{
                    connection.rollback();
                }
                catch (SQLException exception){
                    exception.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:~/backend-clase13", "sa", "sa");
    }

}

