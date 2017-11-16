package Outputs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class TextOutput {
    public void createText(boolean itineraryMade, String string, String fileName) {
        BufferedWriter writer = null;
        try {
            String absolutePath = new File("").getAbsolutePath();
            absolutePath += "/src/Data/Output/" + fileName;

            File logFile = new File(absolutePath);
            writer = new BufferedWriter(new FileWriter(logFile));

            if(itineraryMade){
                writer.write(string);
            }
            else {
                writer.write("Not found");
            }

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
