package Outputs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class TextOutput {
    public void createText(String string, String fileName) {
        BufferedWriter writer = null;
        try {
            String absolutePath = new File(Paths.get(".").toAbsolutePath().normalize().toString()).getAbsolutePath();
            absolutePath += "\\" + fileName;
            File logFile = new File(absolutePath);
            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(string);

        } catch (Exception e) {
            System.out.println("Some problem occurred with the output file");

        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
    }
}
