package dbutil;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @description:
 * @author: Zeng Jianrong
 * @date: 2019/1/3
 */
public class ElasticsearchClientManager {
    public final static String HOST = "10.1.48.212";
    public final static int PORT = 9300;//http请求的端口是9200，客户端是9300

    /**
     * 创建连接集群的TransportClient
     * @return TransportClient
     * @throws UnknownHostException
     */
    public static TransportClient createClient() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "NLP-es-app").put("client.transport.sniff", true).build();
        TransportClient transportClient = new PreBuiltTransportClient(settings).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT));
        return transportClient;
    }
}
