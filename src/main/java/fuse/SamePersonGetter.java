package fuse;

import dbutil.ElasticsearchClientManager;
import dbutil.MySQLConnManager;
import dbutil.MysqlDBHelper;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Zeng Jianrong
 * @Date: 2019/1/14
 **/
public class SamePersonGetter {

    // 形如"t_xxx_xxx"的表所用的数据库连接
    public static Connection mysqlConnection1 = MySQLConnManager.creatConnectionByIPandDB("192.168.37.2", "expert");
    // 论文表papers
    public static Connection mysqlConnection2 = MySQLConnManager.creatConnectionByIPandDB("10.1.48.212", "data");
    // 所有专家名单
    public static ArrayList<Map<String, Object>> nameList = new ArrayList<>();
    // es连接客户端
    private static TransportClient transportClient;

    static {
        try {
            transportClient = ElasticsearchClientManager.createClient();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有专家名单
     *
     * @return
     * @throws InterruptedException
     */
    private static ArrayList<Map<String, Object>> getExpertList() throws InterruptedException {
        String name;
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchAllQuery());
        SearchResponse searchResponse = transportClient.prepareSearch("expert")
                .setTypes("t_basic_expert_information").setSize(100).setQuery(queryBuilder)
                .setScroll(new TimeValue(10000))
                .execute().actionGet();
        int page = (int) (searchResponse.getHits().getTotalHits() / 10);
        ArrayList<Map<String, Object>> list = new ArrayList();
        for (int i = 0; i < page; i++) {
            searchResponse = transportClient.prepareSearchScroll(searchResponse.getScrollId())
                    .setScroll(new TimeValue(10000)).execute()
                    .actionGet();
            // scrollOutput(searchResponse);
            for (SearchHit searchHit : searchResponse.getHits()) {
                Map<String, Object> map = searchHit.getSource();
                list.add(map);
            }

        }
        //关闭es连接客户端
        transportClient.close();
        return list;
    }

    /**
     * 查询作者名包含name的论文记录
     *
     * @param connection mysql数据库链接
     * @param name       专家姓名
     * @return ResultSet类的专家论文结果集
     */
    public static ResultSet getPaperSet(Connection connection, String name) {
        // 在String类的format函数中，“%”才是转义符，并且在sql语句中，参数部分需带上英文引号""。
        String sqlFormat = "select * from data.papers1 where author_ch like \"%s %%\" or author_ch like \"%% %s %%\" " +
                "or author_ch like \"%% %s\"";
        String sql = String.format(sqlFormat, name, name, name);
        ResultSet resultSet = MysqlDBHelper.ExecuteQuery(connection, sql);
        return resultSet;
    }

    /**
     * @param connection mysql数据库链接
     * @param name       专家姓名
     * @return ResultSet类的专家基金结果集
     */
    public static ResultSet getFoundation(Connection connection, String name) {
        String sql = "select * from expert.t_fund where principal = \"" + name + "\"";
        ResultSet resultSet = MysqlDBHelper.ExecuteQuery(connection, sql);
        return resultSet;

    }

    /**
     * 查询指定期刊的影响因子
     *
     * @param journalName 期刊名
     * @return 该期刊的影响因子
     * @throws SQLException
     */
    private static float getInfluenceFactorByJournalName(String journalName) throws SQLException {
        float influenceFactor = 0;
        String sql = "select * from t_influence_factor where journalname = \"" + journalName + "\"";
        ResultSet infFacResultSet = MysqlDBHelper.ExecuteQuery(mysqlConnection1, sql);
        while (infFacResultSet.next()) {
            influenceFactor = infFacResultSet.getFloat("indexnumber");
            break;
        }
        return influenceFactor;
    }

    public static void main(String[] args) throws InterruptedException, SQLException, IOException, BadHanyuPinyinOutputFormatCombination {
        nameList = SamePersonGetter.getExpertList();
        for (Map<String, Object> map : nameList) {

            // 融合专家信息
            map = getSameExpertInfo(map);
            // 融合后的专家信息更新到mysql数据库中
            updateExpertInfo(map);

        }

    }

    /**
     * 将融合后的专家信息更新到mysql数据库中
     *
     * @param map
     */
    public static void updateExpertInfo(Map<String, Object> map) {
        int expert_id = updateFinalExpInfo(map);
        if (map.get("paper_id") != null) {
            updateAward(map, expert_id);
        }
    }

    /**
     * 更新表t_final_expert_information
     *
     * @param map
     * @return 专家id
     */
    public static int updateFinalExpInfo(Map<String, Object> map) {
        // 构造mysql插入语句
        String sqlFormat = "insert into t_final_expert_information(expert_id, name_ch, name_en, mail, title, " +
                "degree, mobile_phone, office_phone, profession, research, label, ccf_a_num, ccf_b_num, " +
                "ccf_c_num, patent_num, copyright_num, national_project_num, social_service, cite_sum, " +
                "doctor_num, master_num, graduated, sex, birth_date, nationality, carrer, people, birth_place, " +
                "achievement, origan, add_date, province_project_num, paper_level_1_num, paper_level_2_num, " +
                "paper_level_3_num) VALUES " +
                "(%d, \"%s\", \"%s\", \"%s\", \"%s\", " +
                "\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", %d, %d, " +
                "%d, %d, %d, %d, \"%s\", %d, " +
                "%d, %d, \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", " +
                "\"%s\", \"%s\", \"%s\", %d, %d, %d, " +
                "%d)";
        String id = (String) map.get("expert_id");
        int expert_id = Integer.parseInt(id);
        String name_ch = (String) map.get("name");
        name_ch = name_ch != null ? name_ch.replace("\"", "") : name_ch;

        String name_en = (String) map.get("english_name");
        name_en = name_en != null ? name_en.replace("\"", "") : name_en;

        String mail = (String) map.get("mail");
        mail = mail != null ? mail.replace("\"", "") : mail;

        String title = (String) map.get("title");
        title = title != null ? title.replace("\"", "") : title;

        String degree = (String) map.get("degree");
        degree = degree != null ? degree.replace("\"", "") : degree;

        String mobile_phone = (String) map.get("mobile_phone");
        mobile_phone = mobile_phone != null ? mobile_phone.replace("\"", "") : mobile_phone;

        String office_phone = (String) map.get("office_phone");
        office_phone = office_phone != null ? office_phone.replace("\"", "") : office_phone;

        String profession = (String) map.get("profession");
        profession = profession != null ? profession.replace("\"", "") : profession;

        String research = (String) map.get("research");
        research = research != null ? research.replace("\"", "") : research;

        String label = (String) map.get("label");
        label = label != null ? label.replace("\"", "") : label;

        int ccf_a_num = (String) map.get("ccf_a_num") != null ?
                Integer.parseInt((String) map.get("ccf_a_num")) : 0;

        int ccf_b_num = (String) map.get("ccf_b_num") != null ?
                Integer.parseInt((String) map.get("ccf_b_num")) : 0;

        int ccf_c_num = (String) map.get("ccf_c_num") != null ?
                Integer.parseInt((String) map.get("ccf_c_num")) : 0;

        int patent_num = (String) map.get("patent_num") != null ?
                Integer.parseInt((String) map.get("ccf_a_num")) : 0;

        int copyright_num = (String) map.get("copyright_num") != null ?
                Integer.parseInt((String) map.get("copyright_num")) : 0;

        int national_project_num = map.get("nationalProjectNum") != null ?
                Integer.parseInt(map.get("nationalProjectNum").toString()) : 0;

        String social_service = (String) map.get("social_service");
        social_service = social_service != null ? social_service.replace("\"", "") : social_service;

        int cite_sum = (String) map.get("cite_sum") != null ?
                Integer.parseInt((String) map.get("cite_sum")) : 0;

        int doctor_num = (String) map.get("doctor_num") != null ?
                Integer.parseInt((String) map.get("doctor_num")) : 0;

        int master_num = (String) map.get("master_num") != null ?
                Integer.parseInt((String) map.get("master_num")) : 0;

        String graduated = (String) map.get("graduated");
        graduated = graduated != null ? graduated.replace("\"", "") : graduated;

        String sex = (String) map.get("sex");
        sex = sex != null ? sex.replace("\"", "") : sex;

        String birth_date = (String) map.get("birth_date");
        birth_date = birth_date != null ? birth_date.replace("\"", "") : birth_date;

        String nationality = (String) map.get("nationality");
        nationality = nationality != null ? nationality.replace("\"", "") : nationality;

        String carrer = (String) map.get("carrer");
        carrer = carrer != null ? carrer.replace("\"", "") : carrer;

        String people = (String) map.get("ethnic");
        people = people != null ? people.replace("\"", "") : people;

        String birth_place = (String) map.get("birth_place");
        birth_place = birth_place != null ? birth_place.replace("\"", "") : birth_place;

        String achievement = (String) map.get("achievement");
        achievement = achievement != null ? achievement.replace("\"", "") : achievement;

        String origan = (String) map.get("origan");
        origan = origan != null ? origan.replace("\"", "") : origan;

        // System.out.println(map.get("add_date").getClass().getName()); //java.lang.Integer
        long add_date = System.currentTimeMillis();

        int provience_project_num = map.get("provience_project_num") != null ?
                Integer.parseInt((String) map.get("provience_project_num")) : 0;

        int paperLevel1Num = Integer.parseInt((String) map.get("paper_level_1_num"));

        int paperLevel2Num = Integer.parseInt((String) map.get("paper_level_2_num"));

        int paperLevel3Num = Integer.parseInt((String) map.get("paper_level_3_num"));

        String sql = String.format(sqlFormat, expert_id, name_ch, name_en, mail, title, degree, mobile_phone,
                office_phone, profession, research, label, ccf_a_num, ccf_b_num, ccf_c_num, patent_num,
                copyright_num, national_project_num, social_service, cite_sum, doctor_num, master_num, graduated, sex,
                birth_date, nationality, carrer, people, birth_place, achievement, origan, add_date,
                provience_project_num, paperLevel1Num, paperLevel2Num, paperLevel3Num);
        // 执行插入操作
        // MysqlDBHelper.ExecuteUpdate(mysqlConnection1, sql);
        return expert_id;
    }

    /**
     * 更新表t_expert_award
     *
     * @param map
     * @param expert_id
     */
    public static void updateAward(Map<String, Object> map, int expert_id) {
        // 构造mysql插入语句
        String sqlFormat = "insert into t_expert_award (expert_id, paper_id, add_date) values (\"%s\", \"%s\", \"%s\")";
        String sql = String.format(sqlFormat, expert_id, map.get("paper_id"), System.currentTimeMillis());
        // System.out.println(sql);
        // 执行插入语句
        // MysqlDBHelper.ExecuteUpdate(mysqlConnection1, sql);
    }

    /**
     * 获取同一专家实体的融合信息
     *
     * @param map ES中的每一条专家记录的信息
     * @return 融合后的专家信息，增加了论文信息、基金信息
     * @throws SQLException
     * @throws IOException
     */
    private static Map<String, Object> getSameExpertInfo(Map<String, Object> map) throws SQLException, IOException,
            BadHanyuPinyinOutputFormatCombination {
        // 获取es中专家的姓名和单位名称，姓名用来查找同名专家，单位名称用来消歧。
        String expertId = (String) map.get("expert_id");
        String expertName = (String) map.get("name");
        String expertUnit = map.get("unit") == null ? "" : (String) map.get("unit");

        UpdateExpertAward.updatePatentId(expertId, expertName, expertUnit, mysqlConnection1);

        // 查询同名专家的中文论文记录
        HashMap<String, String> info = getPaperInfo(expertName, expertUnit);
        map.putAll(info);

        int foundNum = getFoundNum(expertName, expertUnit);
        map.put("nationalProjectNum", foundNum);

        return map;
    }

    private static int getFoundNum(String expertName, String expertUnit) throws SQLException {
        int foundNum = 0;
        // 查询包含该专家姓名的基金记录
        ResultSet foundationResultSet = getFoundation(mysqlConnection1, expertName);
        while (foundationResultSet.next()) {
            // 获取基金记录里的单位信息
            String principalUnit = foundationResultSet.getString("principal_unit");
            // 进行消歧，判断是否是同一个专家
            if (principalUnit.contains(expertUnit) || expertUnit.contains(principalUnit)
                    || ExpertFuser.getLevenshtein(expertUnit, principalUnit) >= 0.6) {
                // System.out.println("基金单位相似度：" + ExpertFuser.getLevenshtein(expertUnit, principalUnit));
                // 统计国家级项目总数
                foundNum++;
            }
        }
        return foundNum;
    }

    private static HashMap<String, String> getPaperInfo(String expertName, String expertUnit) throws SQLException, IOException {
        HashMap<String, String> info = new HashMap<String, String>();

        ResultSet paperSet = getPaperSet(mysqlConnection2, expertName);
        int ccfANum = 0, ccfBNum = 0, ccfCNum = 0, paper_level_1_num = 0, paper_level_2_num = 0,
                paper_level_3_num = 0, citeSum = 0;
        String paperId = "";
        while (paperSet.next()) {
            // 获取论文记录里的专家单位
            String paperUnit = paperSet.getString("author_unit_ch");
            String name = "";
            // 进行消歧，判断是否是同一个专家
            if (paperUnit.contains(expertUnit) || ExpertFuser.getLevenshtein(expertUnit, paperUnit) >= 0.6) {
                // System.out.println("论文单位相似度：" + ExpertFuser.getLevenshtein(expertUnit, paperUnit));

                // 获取论文发表的期刊名称
                String cjournal = paperSet.getString("cjournal");
                // 查询该期刊的影响因子，按影响因子大小分为1、2、3等级。
                float influenceFactor = getInfluenceFactorByJournalName(cjournal);
                if (influenceFactor >= 2) {
                    paper_level_1_num++;
                } else if (influenceFactor >= 1) {
                    paper_level_2_num++;
                } else {
                    paper_level_3_num++;
                }
                // 获取当前论文记录的id
                paperId += paperSet.getString("paper_id") + "####";

                // 统计该专家的论文被引总数
                citeSum += paperSet.getInt("cite_num");

                // 获取期刊的英文名称，统计该专家ccfA、B、C三类论文的数量
                String ejournal = paperSet.getString("ejournal");
                ccfANum += ExpertFuser.getCcfnum("ccfA", ejournal);
                ccfBNum += ExpertFuser.getCcfnum("ccfB", ejournal);
                ccfCNum += ExpertFuser.getCcfnum("ccfC", ejournal);
            }
        }
        info.put("ccfANum", String.valueOf(ccfANum));
        info.put("ccfBNum", String.valueOf(ccfBNum));
        info.put("ccfCNum", String.valueOf(ccfCNum));
        info.put("paper_level_1_num", String.valueOf(paper_level_1_num));
        info.put("paper_level_2_num", String.valueOf(paper_level_2_num));
        info.put("paper_level_3_num", String.valueOf(paper_level_3_num));
        info.put("citeSum", String.valueOf(citeSum));
        info.put("paperId", paperId);

        return info;
    }


    // 中文拼音首字母转大写
    private static String toUpCase(String str) {
        StringBuffer newstr = new StringBuffer();
        newstr.append((str.substring(0, 1)).toUpperCase()).append(
                str.substring(1, str.length()));

        return newstr.toString();
    }

}