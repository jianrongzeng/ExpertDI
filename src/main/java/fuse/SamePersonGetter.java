package fuse;

import dbutil.ElasticsearchClientManager;
import dbutil.MySQLConnManager;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.LinkedList;

/**
 * @Description:
 * @Author: Zeng Jianrong
 * @Date: 2019/1/14
 **/
public class SamePersonGetter {

    public static LinkedList<String> nameList = new LinkedList();
    private static TransportClient transportClient;

    static {
        try {
            transportClient = ElasticsearchClientManager.createClient();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static LinkedList<String> getExpertName() throws InterruptedException {
        String name;
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchAllQuery());
        SearchResponse searchResponse = transportClient.prepareSearch("expert").setTypes("t_basic_expert_information")
                .setSize(100).setQuery(queryBuilder).setScroll(new TimeValue(10000)).execute()
                .actionGet();
        int page = (int) (searchResponse.getHits().getTotalHits() / 10);
        for (int i = 0; i < page; i++) {
            searchResponse = transportClient.prepareSearchScroll(searchResponse.getScrollId())
                    .setScroll(new TimeValue(10000)).execute()
                    .actionGet();
            // scrollOutput(searchResponse);
            for (SearchHit searchHit : searchResponse.getHits()) {
                nameList.add((String) searchHit.getSource().get("name"));
            }

        }
        System.out.println(searchResponse.getHits().totalHits());
        //关闭客户端
        transportClient.close();
        return nameList;
    }

    public static void getPaper() {
        Connection connection = MySQLConnManager.creatConnection();
        for (String name : nameList) {
            String sqlFormat = "select * from data.papers where cauthor like \"%s %\" or cauthor like \"% %s %\" or cauthor like \"% %s\"";
            String sql = String.format(sqlFormat, name, name, name);
            System.out.println(sql);
        }
        // System.out.println(connection);
    }

    public static void main(String[] args) throws InterruptedException {
        nameList = SamePersonGetter.getExpertName();
        // for (Object name : nameList) {
        //     System.out.println(name);
        // }

        getPaper();
    }

}
