package fuse;

import dbutil.MysqlDBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static fuse.ResultSetGetter.getSamePersonPaperSet;

/**
 * @Description:
 * @Author: Zeng Jianrong
 * @Date: 2019/3/4
 **/
public class UpdateExpertAward {

    public static void updatePatentId(String expert_id, String name, String unit, Connection conn) throws SQLException {
        String patentId = getPatentId(name, unit, conn);
        String sql = "update t_expert_award set patent_id = \"" + patentId + "\" where expert_id = \"" + expert_id +
                "\"";
        MysqlDBHelper.ExecuteUpdate(conn, sql);
    }

    public static String getPatentId(String name, String unit, Connection conn) throws SQLException {
        ResultSet patentSet = getPatentSetByName(name, unit, conn);
        String patentId = "";
        while (patentSet.next()) {
            String applicant = patentSet.getString("applicant");
            if (applicant.contains(unit)) {
                patentId += patentSet.getString("patent_id") + "####";
            }
        }
        return patentId;
    }

    private static ResultSet getPatentSetByName(String name, String unit, Connection conn) {
        String sql = "select * from t_patent where inventor like \"%" + name + "%\"";
        return MysqlDBHelper.ExecuteQuery(conn, sql);
    }

    public static void updatePaperId(String expert_id, String name, String unit, Connection conn) throws SQLException {
        String paperId = getPaperId(name, unit, conn);
        String sql = "update t_expert_award set patent_id = \"" + paperId + "\" where expert_id = \"" + expert_id +
                "\"";
        MysqlDBHelper.ExecuteUpdate(conn, sql);
    }

    private static String getPaperId(String name, String expertUnit, Connection conn) throws SQLException {
        String paperId = "";
        ResultSet paperSet = getSamePersonPaperSet(name, conn);
        while (paperSet.next()) {
            String paperUnit = paperSet.getString("author_unit_ch");
            // 判断是否为同一专家实体
            if (paperUnit.contains(expertUnit) || ExpertFuser.getLevenshtein(expertUnit, paperUnit) >= 0.6) {
                paperId += paperSet.getString("paper_id") + "####";
            }
        }
        return paperId;
    }

    public static void main(String[] args) {
        // UpdateExpertAward.updatePaperId("1", "张三", "1");
    }
}
