import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.Min;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.aggregations.metrics.ValueCount;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class Aggregation {
    @SuppressWarnings("resource")
    public static void main(String[] args) {

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("classindex");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        searchSourceBuilder.aggregation(AggregationBuilders.sum("sum").field("fees"));
        searchSourceBuilder.aggregation(AggregationBuilders.avg("avg").field("fees"));
        searchSourceBuilder.aggregation(AggregationBuilders.min("min").field("fees"));
        searchSourceBuilder.aggregation(AggregationBuilders.max("max").field("fees"));
        searchSourceBuilder.aggregation(AggregationBuilders.cardinality("cardinality").field("fees"));
        searchSourceBuilder.aggregation(AggregationBuilders.count("count").field("fees"));

        searchRequest.source(searchSourceBuilder);
        Map<String, Object> map = null;

        try {
            SearchResponse searchResponse = null;
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (SearchHit hit : searchHit) {
                    map = hit.getSourceAsMap();
                    System.out.println("Index data:" + Arrays.toString(map.entrySet().toArray()));

                }
            }

            Sum sum = searchResponse.getAggregations().get("sum");
            double result = sum.getValue();
            System.out.println("aggs Sum: " + result);
            Avg aggAvg = searchResponse.getAggregations().get("avg");
            double valueAvg = aggAvg.getValue();
            System.out.println("aggs Avg::" + valueAvg);
            Min aggMin = searchResponse.getAggregations().get("min");
            double minOutput = aggMin.getValue();
            System.out.println("aggs Min::" + minOutput);
            Max aggMax = searchResponse.getAggregations().get("max");
            double maxOutput = aggMax.getValue();
            System.out.println("aggs Max::" + maxOutput);
            Cardinality aggCadinality = searchResponse.getAggregations().get("cardinality");
            long valueCadinality = aggCadinality.getValue();
            System.out.println("aggs Cadinality::" + valueCadinality);
            ValueCount aggCount = searchResponse.getAggregations().get("count");
            long valueCount = aggCount.getValue();
            System.out.println("aggs Count::" + valueCount);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

