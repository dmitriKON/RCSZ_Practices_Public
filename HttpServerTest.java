package backend;

import com.sun.net.httpserver.HttpsServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;

public class HttpServerTest {

    public static MyHttpServer myHttpServer;

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @BeforeAll
    public static void setup_httpserver() throws Exception {
        myHttpServer.start_http();
    }

    public static String get_jwt() throws IOException, InterruptedException, ParseException {
        String json = new StringBuilder()
                .append("{")
                .append("\"username\":\"" + "superuser" + "\",")
                .append("\"password\":\"" + "111111" + "\"")
                .append("}").toString();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:5000/login"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(response.body());
        return (String) obj.get("jwt");
    }

    @Test
    public void test_login() throws SQLException, IOException, InterruptedException, ParseException {
        System.out.println("-------------- Test Login --------------");
        String json = new StringBuilder()
                .append("{")
                .append("\"username\":\"" + "superuser" + "\",")
                .append("\"password\":\"" + "111111" + "\"")
                .append("}").toString();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:5000/login"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(response.body());
        assert (obj.containsKey("jwt"));
        assert (response.statusCode() == 200);
    }

    @Test
    public void test_addition() throws SQLException, IOException, ParseException, InterruptedException {
        System.out.println("-------------- Test Addition --------------");
        String json = new StringBuilder()
                .append("{")
                .append("\"name\":\"" + "Macbook89" + "\",")
                .append("\"group_name\":\"" + "Technics" + "\",")
                .append("\"price\":" + 100.8 + ",")
                .append("\"amount\":" + 10 + ",")
                .append("\"about\":\"" + "Macbook is not a book" + "\",")
                .append("\"producer\":\"" + "Apple" + "\"")
                .append("}").toString();

        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:5000/api/good"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + get_jwt())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assert (response.statusCode() == 201);

        json = new StringBuilder()
                .append("{")
                .append("\"name\":\"" + "Cool" + "\",")
                .append("\"about\":\"" + "Cool group" + "\"")
                .append("}").toString();

        request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:5000/api/group"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + get_jwt())
                .build();

        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assert (response.statusCode() == 201);
    }

    @Test
    public void test_get_group_and_goods() throws SQLException, IOException, ParseException, InterruptedException {
        System.out.println("-------------- Test Get --------------");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:5000/api/goods/"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Authorization", "Bearer " + get_jwt())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(response.body());
        assert (obj.containsKey("result"));
        assert (response.statusCode() == 200);

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:5000/api/goods/" + "Macbook"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Authorization", "Bearer " + get_jwt())
                .build();

        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        parser = new JSONParser();
        obj = (JSONObject) parser.parse(response.body());
        assert (obj.get("goods_name").equals("Macbook"));
        assert (response.statusCode() == 200);

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:5000/api/group/"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Authorization", "Bearer " + get_jwt())
                .build();

        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        parser = new JSONParser();
        obj = (JSONObject) parser.parse(response.body());
        assert (obj.containsKey("result"));
        assert (response.statusCode() == 200);

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:5000/api/group/" + "Food"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Authorization", "Bearer " + get_jwt())
                .build();

        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        parser = new JSONParser();
        obj = (JSONObject) parser.parse(response.body());
        assert (obj.get("group_name").equals("Food"));
        assert (response.statusCode() == 200);
    }

    @Test
    public void test_update() throws SQLException, IOException, ParseException, InterruptedException {
        System.out.println("-------------- Test Update --------------");
        String json = new StringBuilder()
                .append("{")
                .append("\"about\":\"" + "About us to be done" + "\"")
                .append("}").toString();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:5000/api/group/" + "Food"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + get_jwt())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assert (response.statusCode() == 204);

        json = new StringBuilder()
                .append("{")
                .append("\"about\":\"" + "About us to be done" + "\"")
                .append("}").toString();

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:5000/api/goods/" + "Macbook"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Authorization", "Bearer " + get_jwt())
                .build();

        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assert (response.statusCode() == 204);
    }


    @Test
    public void test_delete() throws SQLException, IOException, ParseException, InterruptedException {
        System.out.println("-------------- Test Delete --------------");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:5000/api/goods/" + "Berries"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + get_jwt())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assert (response.statusCode() == 204);

    }

    @AfterAll
    public static void drop_tables() throws Exception {
        myHttpServer.finish_http();
    }
}
