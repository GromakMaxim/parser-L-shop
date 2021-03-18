package org.example.SettingsReaders;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EndpointsFileReader {
    private final String endpointsSettingsFilePath = "endpoints-settings.txt";

    public List<String> getEndpoints() {
        ArrayList<String> result = new ArrayList<>();
        for (String line : parseFile()) {
            if (line.split(":")[0].equalsIgnoreCase("endpoint")) {
                result.add(line.split(":")[1]);
            }
        }
        return result;
    }

    public String getURL() {
        for (String line : parseFile()) {
            if (line.split(":")[0].equalsIgnoreCase("URL")) {
                return line.split(":")[1] + ":"+line.split(":")[2];
            }
        }
        return null;
    }

    private String[] parseFile() {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fin = new FileInputStream(endpointsSettingsFilePath)) {
            int i = -1;
            while ((i = fin.read()) != -1) {//читаем все символы
                sb.append((char) i);
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        String fileString = sb.toString();
        return fileString
                .replace(" ", "")//очистить пробелы, переносы строки и пр
                .replace("\r", "")
                .replace("\n", "")
                .trim()
                .split(";");//дробим файл по разделителям
    }
}
