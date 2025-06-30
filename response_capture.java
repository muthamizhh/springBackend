package com.example.selenium;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v137.network.Network;
import org.openqa.selenium.devtools.v137.network.model.Request;
import org.openqa.selenium.devtools.v137.network.model.RequestId;
import org.openqa.selenium.devtools.v137.network.model.Response;

import java.io.File;
import java.util.*;

public class App {

    // Class to hold request data
    static class RequestData {
        public String url;
        public String postData;

        public RequestData(String url, String postData) {
            this.url = url;
            this.postData = postData;
        }
    }

    // Class to hold response data
    static class ResponseData {
        public String url;
        public int status;
        public String body;
        public Map<String, Object> headers;

        public ResponseData(String url, int status, String body, Map<String, Object> headers) {
            this.url = url;
            this.status = status;
            this.body = body;
            this.headers = headers;
        }
    }

    public static void main(String[] args) throws Exception {
        // Set up ChromeDriver
        ChromeOptions options = new ChromeOptions();
        ChromeDriver driver = new ChromeDriver(options);

        // DevTools session
        DevTools devTools = driver.getDevTools();
        devTools.createSession();

        List<RequestData> capturedRequests = new ArrayList<>();
        List<ResponseData> capturedResponses = new ArrayList<>();
        Map<RequestId, String> requestIdToUrl = new HashMap<>();

        // Enable network tracking
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        // Listen for outgoing requests
        devTools.addListener(Network.requestWillBeSent(), event -> {
            Request request = event.getRequest();
            String url = request.getUrl();
            String postData = request.getPostData().orElse(null);
            capturedRequests.add(new RequestData(url, postData));
            requestIdToUrl.put(event.getRequestId(), url);
            System.out.println("Captured Request: " + url + " Payload: " + postData);
        });

        // Listen for incoming responses
        devTools.addListener(Network.responseReceived(), event -> {
            RequestId requestId = event.getRequestId();
            Response response = event.getResponse();
            String url = response.getUrl();
            int status = response.getStatus();
            Map<String, Object> headers = response.getHeaders().toJson();

            try {
                var body = devTools.send(Network.getResponseBody(requestId));
                String responseBody = body.getBody();
                capturedResponses.add(new ResponseData(url, status, responseBody, headers));
                System.out.println("Captured Response: " + url + " Status: " + status);
            } catch (Exception e) {
                System.out.println("Failed to get body for: " + url + " - " + e.getMessage());
            }
        });

        // Open the form and submit data
        driver.get("https://httpbin.org/forms/post");
        driver.findElement(By.name("custname")).sendKeys("Alice");
        driver.findElement(By.name("custtel")).sendKeys("1234567890");
        driver.findElement(By.name("custemail")).sendKeys("alice@example.com");
        driver.findElement(By.tagName("form")).submit();

        // Wait for responses
        Thread.sleep(5000);

        // Write requests and responses to JSON files
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("network-log.json"), capturedRequests);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("network-responses.json"), capturedResponses);

        // Cleanup
        driver.quit();
    }
}
