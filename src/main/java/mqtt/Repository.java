package mqtt;

import java.util.ArrayList;
import java.util.List;

class Repository {
    // Singleton instance
    private static Repository instance;
    private List<String> csvData;

    // Private constructor to prevent instantiation
    private Repository() {
        csvData = new ArrayList<>();
        System.out.println("Repository is instantiated.");
    }

    // Singleton access method
    public static synchronized Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    // Store each line from the CSV in the repository
    public void addData(List<String> data) {
        csvData.addAll(data);
        System.out.println("Data added to the repository.");
    }

    // Method to retrieve stored data
    public List<String> getData() {
        return new ArrayList<>(csvData);
    }

    public void printData() {
        System.out.println("Stored CSV Data:");
        for (String line : csvData) {
            System.out.println(line);
        }
    }
}
