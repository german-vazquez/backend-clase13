package presencial;

import java.sql.*;

public class Main {
    private static final String SQL_CREATE_TABLE = "drop table if exists cuentas; create table cuentas(id int primary key, nro_cuenta int not null, nombre varchar(100) not null, saldo numeric(10,2) not null)";
    private static final String SQL_INSERT = "insert into cuentas (id, nro_cuenta, nombre, saldo) values(?,?,?,?)";
    private static final String SQL_UPDATE = "update cuentas set saldo=? where id=?";

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

        } catch (Exception e) {
        }
    }

    public static Connection getConnection() {
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:~/backend-clase13", "sa", "sa");
    }

}

