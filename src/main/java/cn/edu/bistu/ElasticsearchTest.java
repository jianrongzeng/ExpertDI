package cn.edu.bistu;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElasticsearchTest {
    public final static String HOST = "10.1.48.212";
    public final static int PORT = 9300;//http请求的端口是9200，客户端是9300

    public void test1() throws UnknownHostException {
        //创建客户端

        Settings settings = Settings.builder().put("cluster.name", "NLP-es-app").put("client.transport.sniff", true).build();
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT));

        // SearchRequestBuilder srb = client.prepareSearch("expert").setTypes("t_basic_expert_information");
        // SearchResponse sr = srb.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
        // SearchHits hits = sr.getHits();
        // for (SearchHit hit : hits) {
        //     System.out.println(hit.getSourceAsString());
        // }
        // System.out.println(hits.getTotalHits());

        // QueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchAllQuery());
        // SearchResponse searchResponse = client.prepareSearch("expert").setTypes("t_basic_expert_information")
        //         .setSize(100).setQuery(queryBuilder).setScroll(new TimeValue(10000)).execute()
        //         .actionGet();
        // int page = (int) (searchResponse.getHits().getTotalHits() / 10);
        // for (int i = 0; i < page; i++) {
        //     searchResponse = client.prepareSearchScroll(searchResponse.getScrollId())
        //             .setScroll(new TimeValue(10000)).execute()
        //             .actionGet();
        //     scrollOutput(searchResponse);
        // }
        // System.out.println(searchResponse.getHits().totalHits());
        // //关闭客户端
        // client.close();
    }

    private void scrollOutput(SearchResponse searchResponse) {
        for (SearchHit searchHit : searchResponse.getHits()) {
            System.out.println(searchHit.getSource().get("name"));
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        ElasticsearchTest test = new ElasticsearchTest();
        test.test1();
    }
}