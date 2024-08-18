package utilities;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class HttpClientUtil {

    private static final String AUTHORIZATION_TOKEN = "pk_10938217_71YTFKGQZ4TOCJNOP7WBJJ4W8978V53W";
    private static final String BASE_URL = "https://api.clickup.com/api/v2/list/";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static String fetchValidStatus(String listId) throws IOException, InterruptedException {
        String url = BASE_URL + listId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", AUTHORIZATION_TOKEN)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode jsonResponse = objectMapper.readTree(response.body());

        if (jsonResponse.has("statuses") && jsonResponse.get("statuses").isArray()) {
            // Assuming the first status is valid for simplicity
            return jsonResponse.get("statuses").get(0).get("status").asText();
        }
        throw new RuntimeException("No valid status found for the list");
    }

    public static String createTask(String[] results, String credentialsUser, String listId) throws IOException, InterruptedException {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now().withNano(0); // Remove nanoseconds
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")); // Format to HH:mm:ss
        ObjectNode json = objectMapper.createObjectNode();

        // Default values
        json.put("name", "");
        json.put("description", "");
        json.put("markdown_description", "");

        int passedCount = 0;
        int failedCount = 0;
        Set<String> uniqueResults = new LinkedHashSet<>(); // Using a Set to prevent duplicates

        // Process each test result
        for (String result : results) {
            String formattedResult = formatTestResult(result);
            uniqueResults.add(formattedResult); // Ensures only unique results are added
            if (result.contains("Passed")) {
                passedCount++;
            } else if (result.contains("Failed")) {
                failedCount++;
            }
        }

        int totalTests = passedCount + failedCount;

        StringBuilder markdownDescription = new StringBuilder();
        markdownDescription.append(String.format("Total tests executed: %d\nfailed: %d / passed: %d\n\n", totalTests, failedCount, passedCount));
        markdownDescription.append("Test Results:\n");

        for (String uniqueResult : uniqueResults) {
            markdownDescription.append(uniqueResult).append("\n");
        }

        // Set the ticket name
        String title = "Home UI Automation Tests";

        // Determine the status based on test results
        String status;
        if (failedCount > 0) {
            status = "FAIL"; // If any test fails, set status to "FAIL"
        } else {
            status = "PASS"; // If all tests pass, set status to "PASS"
        }

        // Set JSON fields
        json.put("name", title);
        json.put("status", status);
        String formattedDateTime = currentDate.toString() + " / Time: " + formattedTime;
        String markdown = String.format("Account name: %s\nDate: %s\n%s", credentialsUser, formattedDateTime, markdownDescription.toString().trim());
        json.put("markdown_description", markdown);

        String postData = objectMapper.writeValueAsString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + listId + "/task?custom_task_ids=true&team_id=123"))
                .header("Authorization", AUTHORIZATION_TOKEN)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(postData))
                .build();

        System.out.println("Sending request to ClickUp for list ID: " + listId);

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Log the response for debugging
        System.out.println("Response status code for list ID " + listId + ": " + response.statusCode());
        System.out.println("Response body for list ID " + listId + ": " + response.body());

        // Parse the response to get the task ID
        JsonNode jsonResponse = objectMapper.readTree(response.body());
        if (jsonResponse.has("id")) {
            System.out.println("Task created successfully with ID: " + jsonResponse.get("id").asText());
        } else {
            System.out.println("No ID found in the response for list ID " + listId);
        }

        return response.body();
    }

    private static String formatTestResult(String result) {
        // Assumes the result is in the format "Test: XYZ - Result: Passed"
        if (result.contains(" - Result: ")) {
            String testName = result.substring(result.indexOf("Test: ") + 6, result.indexOf(" - Result: "));
            String testResult = result.substring(result.lastIndexOf("Result: ") + 8);

            // Ensure the test name is in the correct format, remove any spaces, and standardize the casing
            testName = testName.trim().replace(" ", "");
            return String.format("Test: %s - %s", testName, testResult);
        }
        // If the format is different, return as is
        return result;
    }
}
