import com.opencsv.CSVReader;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Main {

    public static ArrayList<String> readFile(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);

        CSVReader csvReader = new CSVReader(fileReader);
        String[] nextRecord;

        ArrayList<String> items = new ArrayList<>();
        while ((nextRecord = csvReader.readNext()) != null) {
            for (String cell : nextRecord) {
                if(isNotBlank(cell)) {
                    items.add(cell);
                }
            }
        }
        return items;
    }

    public static void main(String[] args) throws IOException {
        if(args.length < 2) {
            throw new RuntimeException("Args must be passed. Input File name, Output File name");
        }

        String inputFilename = args[0];
        String outputFilename = args[1];
//        String language = args[2];
        String language = "en";

        ArrayList<String> items = readFile(inputFilename);
        writeFile(items, outputFilename, language);
    }

    private static void writeFile(ArrayList<String> items, String outputFilename, String language) throws IOException {
        // Create file
        File myTsv = new File(outputFilename);
        if(myTsv.createNewFile()) {
            System.out.println("File created.");
        } else {
            throw new RuntimeException("File already exists");
        }

        items.forEach(phrase -> {
            Thread translateThread = new Thread(new TranslateAndGenerateAudio(phrase, outputFilename, language));
            translateThread.start();
        });
    }

}
