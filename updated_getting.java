package com.example.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.network.model.Request;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

public class App {

    static class RequestData {
        public String url;
        public String postData;

        public RequestData(String url, String postData) {
            this.url = url;
            this.postData = postData;
        }
    }

    public static void main(String[] args) throws Exception {
        // If needed: System.setProperty("webdriver.edge.driver", "path/to/msedgedriver");

        ChromeOptions options = new ChromeOptions();
        ChromeDriver driver = new ChromeDriver(options);

        DevTools devTools = driver.getDevTools();
        devTools.createSession();

        List<RequestData> capturedRequests = new ArrayList<>();

        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        devTools.addListener(Network.requestWillBeSent(), event -> {
            Request request = event.getRequest();
            String url = request.getUrl();
            String postData = request.getPostData().orElse(null);
            capturedRequests.add(new RequestData(url, postData));
            System.out.println("Captured: " + url + " Payload: " + postData);
        });

        driver.get("https://httpbin.org/forms/post");
        driver.findElement(By.name("custname")).sendKeys("Alice");
        driver.findElement(By.name("custtel")).sendKeys("1234567890");
        driver.findElement(By.name("custemail")).sendKeys("alice@example.com");
        driver.findElement(By.tagName("form")).submit();

        Thread.sleep(5000);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("network-log.json"), capturedRequests);

        driver.quit();
    }
}
