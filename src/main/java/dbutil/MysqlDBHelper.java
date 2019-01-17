package dbutil;

import java.sql.*;

public class MysqlDBHelper {
    /**
     * 执行不带参数的数据库查询操作
     *
     * @param conn Connection对象
     * @param sql  查询的sql语句
     * @return 查询结果ResulSet
     */
    public static ResultSet ExecuteQuery(Connection conn, String sql) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("数据库查询出错！" + e.toString());
        }
        return rs;
    }

    /**
     * 执行带参数的数据库查询操作
     *
     * @param conn   Connection对象
     * @param sql    查询的sql语句
     * @param params 参数数组
     * @return 查询结果ResulSet
     */
    public static ResultSet ExecuteQuery(Connection conn, String sql, Object[] params) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("数据库查询出错！" + e.toString());
        }
        return rs;
    }

    /**
     * 执行不带参数的数据库更新操作
     *
     * @param conn Connection对象
     * @param sql  更新的sql语句
     * @return 更新操作影响的行数
     */
    public static int ExecuteUpdate(Connection conn, String sql) {
        Statement stmt = null;
        int count = 0;
        try {
            stmt = conn.createStatement();
            count = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("数据库更新出错！" + e.toString());
        }
        return count;
    }

    /**
     * 执行带参数的数据库更新操作
     *
     * @param conn   Connection对象
     * @param sql    更新的sql语句
     * @param params 参数数组
     * @return 更新操作影响的行数
     */
    public static int ExecuteUpdate(Connection conn, String sql, Object[] params) {
        PreparedStatement pstmt = null;
        int count = 0;
        try {
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            count = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("数据库更新出错！" + e.toString());
        }
        return count;
    }
}
