/*
 * Copyright 2020-present AO Raiffeisenbank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package raiffeisen.sbp.sdk.web;

import raiffeisen.sbp.sdk.model.Response;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.stream.Stream;

public class SdkHttpClient {

    private final HttpClient httpClient;

    public SdkHttpClient() {
        this(HttpClient.newHttpClient());
    }

    SdkHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }


    public Response getRequest(String url, Map<String, String> headers) throws IOException, URISyntaxException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .headers(mapHeaders(headers))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new Response(response.statusCode(), response.body());
    }

    public Response postRequest(String url, Map<String, String> headers, String entity) throws URISyntaxException, InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .headers(mapHeaders(headers))
                .POST(HttpRequest.BodyPublishers.ofString(entity))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new Response(response.statusCode(), response.body());
    }

    public Response deleteRequest(String url, Map<String, String> headers) throws URISyntaxException, InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .headers(mapHeaders(headers))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new Response(response.statusCode(), response.body());
    }

    private String[] mapHeaders(Map<String, String> headers) {
        return headers.entrySet().stream()
                .flatMap(x -> Stream.of(x.getKey(), x.getValue()))
                .toArray(String[]::new);
    }

}
