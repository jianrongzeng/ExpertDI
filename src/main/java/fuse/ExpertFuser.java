package fuse;

import dbutil.DBHelper;
import dbutil.MySQLConnManager;
import model.*;
import org.jetbrains.annotations.Contract;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class ExpertFuser {

    // 关键字在计算相似度上所占的权重
    public static float keywordWeight = 0.5f;
    // 工作单位在计算相似度上所占的权重
    public static float unitWeight = 0.5f;

    // 专家与论文之间的相似度阈值。当专家与论文之间的相似度大于阈值时，则认为它们是同属一个实体。
    public static float thresholdExpertPaper = 0.7f;
    // 专家与基金之间的相似度阈值。当专家与基金之间的相似度大于阈值时，则认为它们是同属一个实体。
    public static float thresholdExpertFoundation = 0.7f;

    // map用来存放待融合的同一实体的id。
    public static HashMap<String, String> map = new HashMap();

    public Paper paper;
    public Foundation foundation;
    public BasicInfo basicInfo;
    public Patent patent;
    public SoftwareCopyright softwareCopyright;

    /**
     * ExpertFuser类的构造函数，参数为Paper、Foundation、BasicInfo、Patent、SoftwareCopyright类的对象。但可能出现初始化
     * ExpertFuser类的对象时有的参数为空的现象，因此参数部分或许可用“可变参数列表”进行优化。
     * @param paper
     * @param foundation
     * @param basicInfo
     * @param patent
     * @param softwareCopyright
     */
    public ExpertFuser(Paper paper, Foundation foundation, BasicInfo basicInfo, Patent patent, SoftwareCopyright softwareCopyright) {
        this.paper = paper;
        this.foundation = foundation;
        this.basicInfo = basicInfo;
        this.patent = patent;
        this.softwareCopyright = softwareCopyright;
    }

    /**
     * 对同名的论文信息、基金信息、专家基本属性信息、专利、软著信息进行消歧
     */
    public void disambiguate() {

        // 论文关键词
        String paperKeyword = paper.getKeyword_ch();
        // 论文作者
        String author = paper.getAuthor_ch();
        // 作者单位
        String author_unit = paper.getAuthor_unit();

        // 基金关键词
        String foundationKeyword = foundation.getKeyword_ch();
        // 基金主持人
        String principal = foundation.getPrincipal();
        // 基金主持人单位
        String principal_unit = foundation.getPrincipal_unit();

        // 专家关键词（领域标签+研究领域）
        String expertKeyword = basicInfo.getLabel() + basicInfo.getResearch();
        // 专家名称
        String expertName = basicInfo.getName();
        // 专家单位
        String unit = basicInfo.getUnit();

        // 专家关键词（领域标签+研究领域）与论文关键词之间的相似度
        float similarityExpertkeywordPaperkeyword = getLevenshtein(expertKeyword, paperKeyword);
        // 专家单位与论文作者单位之间的相似度
        float similarityUnitAuthorunit = getLevenshtein(unit, author_unit);
        // 专家记录与论文记录的最终相似度
        float finalSimilarityExpertPaper = keywordWeight * similarityExpertkeywordPaperkeyword + unitWeight * similarityUnitAuthorunit;

        // 若专家记录与论文记录的最终相似度大于设定的阈值，则认为该专家与论文应该融合，把他们的id放到map中。
        // map中存放消歧后的专家、论文、基金等记录的id字段
        if (finalSimilarityExpertPaper >= thresholdExpertPaper) {
            map.put("expertId", basicInfo.getExpert_id());
            map.put("paperId", paper.getPaper_id());
        }

        // 专家关键词与基金关键词之间的相似度
        float similarityExpertkeywordFoundationkeyword = getLevenshtein(expertKeyword, foundationKeyword);
        // 专家单位与基金主持人之间的相似度
        float similarityUnitPrincipalunit = getLevenshtein(unit, principal_unit);
        // 专家记录与基金记录之间的最终相似度
        float finalSimilarityExpertFoundation = keywordWeight * similarityExpertkeywordFoundationkeyword + unitWeight * similarityUnitPrincipalunit;

        if (finalSimilarityExpertFoundation >= thresholdExpertFoundation) {
            map.put("expertId", basicInfo.getExpert_id());
            map.put("fundId", foundation.getFund_id());
        }

    }

    /**
     * 将同一专家实体的信息进行融合
     * @throws IOException
     * @throws SQLException
     */
    public void integrate() throws IOException, SQLException {

        String expertId, paperId, fundId;

        if (map.containsKey("expertId")) {
            expertId = map.get("expertId");
        }
        if (map.containsKey("paperId")) {
            expertId = map.get("paperId");
        }
        if (map.containsKey("fundId")) {
            expertId = map.get("fundId");
        }

        // 创建FinalExpertInformation类的对象，并给各个字段赋值
        FinalExpertInformation finalExpertInformation = new FinalExpertInformation();
        finalExpertInformation.setName_ch(basicInfo.getName());
        finalExpertInformation.setName_en(paper.getAuchor_en());
        finalExpertInformation.setMail(paper.getMails());
        finalExpertInformation.setTitle(basicInfo.getTitle());
        finalExpertInformation.setDegree(basicInfo.getDegree());
        finalExpertInformation.setMobile_phone(basicInfo.getMobile_phone());
        finalExpertInformation.setOffice_phone(basicInfo.getOffice_phone());
        finalExpertInformation.setProfession(basicInfo.getProfession());
        finalExpertInformation.setResearch(basicInfo.getResearch());
        finalExpertInformation.setLabel(basicInfo.getLabel());
        finalExpertInformation.setCcf_a_num(finalExpertInformation.getCcf_a_num() + getCcfnum("ccfA"));
        finalExpertInformation.setCcf_b_num(finalExpertInformation.getCcf_b_num() + getCcfnum("ccfB"));
        finalExpertInformation.setCcf_c_num(finalExpertInformation.getCcf_c_num() + getCcfnum("ccfC"));
        // finalExpertInformation.setPatent_num();
        // finalExpertInformation.setCopyright_num();
        // finalExpertInformation.setNational_project_num();
        finalExpertInformation.setSocial_service(basicInfo.getSocial_service());
        // finalExpertInformation.setCite_sum();
        finalExpertInformation.setDoctor_num(getStudentNum());
        finalExpertInformation.setMaster_num(getStudentNum());
        // finalExpertInformation.setAuthority();
        finalExpertInformation.setGraduated(basicInfo.getGraduated());
        finalExpertInformation.setSex(basicInfo.getSex());
        finalExpertInformation.setBirth_date(basicInfo.getBirth_date());
        finalExpertInformation.setNationality(basicInfo.getNationality());
        finalExpertInformation.setCarrer(basicInfo.getCarrer());
        finalExpertInformation.setPeople(basicInfo.getPeople());
        finalExpertInformation.setBirth_place(basicInfo.getBirth_place());
        finalExpertInformation.setAchievement(basicInfo.getAchievement());
        // finalExpertInformation.setOrigan();
        finalExpertInformation.setAdd_date(System.currentTimeMillis());

    }

    public int getStudentNum() throws SQLException {
        int studentNum = 0;
        String id = basicInfo.getExpert_id();
        Connection connection = MySQLConnManager.creatConnection();
        String sql = "select count(*) where expert_id = ?";
        Object[] params = {basicInfo.getExpert_id()};
        ResultSet resultSet = DBHelper.ExecuteQuery(connection, sql, params);
        while (resultSet.next()) {
            studentNum = resultSet.getInt(1);
            break;
        }
        return studentNum;
    }

    public int getCcfnum(String type) throws IOException {
        int num = 0;
        String journal = paper.getJuornal_en();
        String ccf;

        Hashtable<String, ArrayList<String>> ccfTable = getCCFList();
        ArrayList<String> ccflist = ccfTable.get(type);

        for (int i = 0; i < ccflist.size(); i++) {
             ccf = ccflist.get(i);
             if (ccf.contains(journal)) {
                 num++;
                 break;
             }
        }

        return num;
    }

    public Hashtable<String, ArrayList<String>> getCCFList() throws IOException {
        Hashtable<String, ArrayList<String>> ccfTable = new Hashtable<>();

        ArrayList<String> ccfAList = readFile("rsource\\ccfA.txt");
        ArrayList<String> ccfBList = readFile("rsource\\ccfB.txt");
        ArrayList<String> ccfCList = readFile("rsource\\ccfC.txt");

        ccfTable.put("ccfA", ccfAList);
        ccfTable.put("ccfB", ccfBList);
        ccfTable.put("ccfC", ccfCList);

        return ccfTable;
    }

    public ArrayList<String> readFile(String filePath) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        FileReader in = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(in);
        String line = "";
        while ((line = reader.readLine()) != null) {
            list.add(line);
        }
        reader.close();
        in.close();
        return list;
    }

    /**
     * 计算两个字符串之间的编辑距离相似度
     * @param str1
     * @param str2
     * @return
     */
    private static float getLevenshtein(String str1, String str2) {
        //计算两个字符串的长度。
        int len1 = str1.length();
        int len2 = str2.length();
        //建立上面说的数组，比字符长度大一个空间
        int[][] dif = new int[len1 + 1][len2 + 1];
        //赋初值，步骤B。
        for (int a = 0; a <= len1; a++) {
            dif[a][0] = a;
        }
        for (int a = 0; a <= len2; a++) {
            dif[0][a] = a;
        }
        //计算两个字符是否一样，计算左上的值
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //取三个值中最小的
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
                        dif[i - 1][j] + 1);
            }
        }
        System.out.println("字符串\"" + str1 + "\"与\"" + str2 + "\"的比较");
        //取数组右下角的值，同样不同位置代表不同字符串的比较
        System.out.println("差异步骤：" + dif[len1][len2]);
        //计算相似度
        float similarity = 1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());
        System.out.println("相似度：" + similarity);
        return similarity;
    }

    /**
     * @param is int型可变参数列表
     * @return 参数is中的最小值
     */
    @Contract(pure = true)
    public static int min(int... is) {
        int min = Integer.MAX_VALUE;
        for (int i : is) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }

    public static void main(String[] args) {
        // System.out.println(getLevenshtein("计算机科学", "计算机科技"));
        System.out.println(System.currentTimeMillis());
    }
}
