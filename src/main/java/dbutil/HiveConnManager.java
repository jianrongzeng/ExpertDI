package dbutil;

import java.sql.*;

/**
 * @description:
 * @author: Zeng Jianrong
 * @date: 2019/1/3
 */
public class HiveConnManager {
    public static String driverName = "org.apache.hive.jdbc.HiveDriver";
    public static String url = "jdbc:hive2://10.1.48.212:10000/db_hive_zw_test";
    public static String user = "root";
    public static String password = "123456";

    /**
     * 创建Hive连接
     * @return
     * @throws SQLException
     */
    public static Connection createConnection() throws SQLException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = HiveConnManager.createConnection();
        // 2019/1/3。hive中目前有db_hive_zw_test（包含student表）、default（目前无表）、expert（包含t_paper表）三个数据库。
        String sql = "select * from student";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
        resultSet.close();
        connection.close();
    }

}
