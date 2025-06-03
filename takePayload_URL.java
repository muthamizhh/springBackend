import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v135.network.Network;
import org.openqa.selenium.devtools.v135.network.model.Request;
import org.openqa.selenium.devtools.v135.network.model.Response;
import org.openqa.selenium.devtools.v135.network.model.RequestId;

import org.openqa.selenium.By;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

public class App {

    static class RequestData {
        public String url;
        public String postData;
        public String method;

        public RequestData(String url, String postData, String method) {
            this.url = url;
            this.postData = postData;
            this.method = method;
        }
    }

    public static void main(String[] args) throws Exception {
        // If needed: System.setProperty("webdriver.edge.driver", "path/to/msedgedriver");

        EdgeOptions options = new EdgeOptions();
        EdgeDriver driver = new EdgeDriver(options);

        DevTools devTools = driver.getDevTools();
        devTools.createSession();

        List<RequestData> capturedRequests = new ArrayList<>();

        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        devTools.addListener(Network.requestWillBeSent(), event -> {
            Request request = event.getRequest();
            String url = request.getUrl();
            String postData = request.getPostData().orElse(null);
            String method = request.getMethod();
//            int status =   res
            capturedRequests.add(new RequestData(url, postData, method));
            System.out.println("Captured: " + url + " Payload: " + postData +" Method: "+method);
        });

        driver.get("https://httpbin.org/forms/post");


// ...

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
