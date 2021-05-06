package Audit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Audit {
    private static Audit audit;
    private FileWriter fileWriter;

    public static Audit getInstance() {
        if (audit == null) {
            audit = new Audit();
        }

        return audit;
    }

    private Audit() {
        try {
            fileWriter = new FileWriter("src/assets/audit.csv", true);
        }

        catch (Exception ignored) {}
    }

    private void append(String actionName) {
        try {
            StringBuilder line = new StringBuilder();
            line.append(actionName)
                    .append(",")
                    .append(new SimpleDateFormat("HH:mm:ss | yyyy.MM.dd").format(new Date()))
                    .append("\n");

            fileWriter.append(line);
            fileWriter.close();
        }

        catch (Exception ignored) {}
    }

    public void userRegistered() {
        append("user registered");
    }

}
