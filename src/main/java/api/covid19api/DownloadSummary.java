package api.covid19api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class DownloadSummary {
    private String requestGet(String url, CloseableHttpClient client) throws Exception {
        String entityString;
        HttpGet httpGet = new HttpGet(url);

        try (CloseableHttpResponse response = client.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            entityString = IOUtils.toString(entity.getContent(), "UTF-8");
            EntityUtils.consume(entity);
            httpGet.releaseConnection();
        }
        return entityString;
    }

    public String downloadSummary() {
        String summary = null;
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            summary = requestGet("https://api.covid19api.com/summary", client);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return summary;
    }
    public Summary getSummary(String summary){
        return  new Gson().fromJson(summary, Summary.class);
    }
    public static void main(String[] args) {
        DownloadSummary summary = new DownloadSummary();
        summary.downloadSummary();
    }
}
