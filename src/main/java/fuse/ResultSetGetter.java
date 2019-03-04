package fuse;

import dbutil.MysqlDBHelper;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * @Description:
 * @Author: Zeng Jianrong
 * @Date: 2019/3/4
 **/
public class ResultSetGetter {
    public static ResultSet getSamePersonPaperSet(String name, Connection conn) {
        String sqlForamt = "select * from papers1 where author_ch like \"%s %%\" or author_ch like \"%% %s %%\" or " +
                "author_ch like \"%% %s\"";
        String sql = String.format(sqlForamt, name, name, name);
        ResultSet paperSet = MysqlDBHelper.ExecuteQuery(conn, sql);
        return paperSet;
    }
}
