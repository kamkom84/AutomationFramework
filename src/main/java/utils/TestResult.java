package utils;

public class TestResult {
    private String testName;
    private String result;

    public TestResult(String testName, String result) {
        this.testName = testName;
        this.result = result;
    }

    public String getTestName() {
        return testName;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Test: " + testName + " - Result: " + result;
    }
}
