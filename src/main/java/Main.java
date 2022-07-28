import com.google.cloud.Tuple;

import java.io.*;
import java.util.List;

public class Main {

    public static List<Tuple<String, String>> readFile(String fileName) {
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            throw new RuntimeException("Args must be passed. Input File name, Output File name");
        }

        String inputFilename = args[0];
        String outputFilename = args[1];
        // Example
//        String language = "en";
        String language = args[2];

        CsvReader<List<Tuple<String, String>>> csvReader = new CsvReaderImpl();
        TranslateCsvItems translateCsvItems = new TranslateCsvItemsImpl();
        List<Tuple<String, String>> items = csvReader.readCsv(inputFilename);
        List<Tuple<String, String>> translatedItems = translateCsvItems.translate(items, language);
        // TODO: Implement lightweight database
//        writeFile(items, outputFilename, language);
    }

    private static void writeFile(List<Tuple<String, String>> items, String outputFilename, String language) throws IOException {
        // Create file
        File myTsv = new File(outputFilename);
        if (myTsv.createNewFile()) {
            System.out.println("File created.");
        } else {
            throw new RuntimeException("File already exists");
        }

//        items.forEach(phrase -> {
//            Thread translateThread = new Thread(new TranslateAndGenerateAudio(phrase, outputFilename, language));
//            translateThread.start();
//        });
    }

}
