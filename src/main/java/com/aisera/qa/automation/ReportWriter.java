package com.aisera.qa.automation;

import java.io.FileWriter;
import java.io.IOException;

public class ReportWriter {
    private FileWriter writer;

    public ReportWriter(String filePath) throws IOException {
        // Initialize the FileWriter and write the header to the CSV file
        writer = new FileWriter(filePath);
        writer.write("Website Name,Status\n");
    }

    public void logResult(String websiteName, String status) throws IOException {
        // Write the result to the CSV file in the format "Website Name,Status"
        writer.write(websiteName + "," + status + "\n");
    }

    public void close() throws IOException {
        // Close the FileWriter to ensure all data is flushed and file is saved
        if (writer != null) {
            writer.close();
        }
    }
}

