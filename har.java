package com.example.selenium;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;

public class Hello {

    public static void captureHttpTraffic(String targetUrl) {
        // Start the BrowserMob proxy
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.setTrustAllServers(true);
        proxy.start(0); // 0 = auto select port

        // Enable HAR capture
        proxy.newHar("http-capture");

        // Convert BrowserMob proxy to Selenium proxy
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        // Set up ChromeDriver with proxy
        ChromeOptions options = new ChromeOptions();
        options.setProxy(seleniumProxy);
        options.addArguments("--headless"); // optional
//        options.addArguments("--headless"); // optional
        options.addArguments("--ignore-certificate-errors");
//        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(targetUrl);

            // Retrieve and print HAR entries
            Har har = proxy.getHar();
            File harFile = new File("output.har");
            har.writeTo(harFile);
            har.getLog().getEntries().forEach(entry -> {
                System.out.println("➡️ Request URL: " + entry.getRequest().getUrl());
                System.out.println("⬅️ Response Code: " + entry.getResponse().getStatus());
                System.out.println("--------------------------------------");
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
            proxy.stop();
        }
    }

    public static void main(String[] args) {
        captureHttpTraffic("https://www.youtube.com/");
    }
}

