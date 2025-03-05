package mqtt;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReaderPublisher {

    public static List<String> readCSVLines(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
    public static List<String> readCSV(String csvFile) {
        List<String> lines = new ArrayList<>();
        String[] nextLine;

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            // Read each line from the CSV file
            while ((nextLine = reader.readNext()) != null) {
                // Concatenate the line into a single string
                String content = String.join(", ", nextLine);
                lines.add(content);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
