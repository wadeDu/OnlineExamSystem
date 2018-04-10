package org.mislab.api;

import java.util.ArrayList;
import java.util.List;

public class Problem {
    private final String title;
    private final String description;
    private final List<TestData> testdata;
    
    public Problem(String title, String desc) {
        this.title = title;
        this.description = desc;
        
        testdata = new ArrayList<>();
    }
    
    public void addTestData(String input, String output) {
        testdata.add(new TestData(input, output));
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<TestData> getTestData() {
        return testdata;
    }
    
    public class TestData {
        public final String input;
        public final String output;
        
        public TestData(String input, String output) {
            this.input = input;
            this.output = output;
        }
    }
}
