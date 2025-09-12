package sampleTest.apiFiles;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class api_client {
    private final HttpClient client;

    public api_client() {
        this.client = HttpClient.newHttpClient();
    }

    public HttpResponse<String> send_get_request(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new java.net.URI(url))
            .GET()
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> send_post_request(String url, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new java.net.URI(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
