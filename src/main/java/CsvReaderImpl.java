import com.google.cloud.Tuple;
import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderImpl implements CsvReader<List<Tuple<String, String>>> {
    @Override
    public List<Tuple<String, String>> readCsv(String fileName) {
        return readCsv(fileName, ',');
    }

    @Override
    public List<Tuple<String, String>> readCsv(String fileName, char separator) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Failed to read file: %s", fileName), e);
        }

        CSVReader csvReader = new CSVReader(fileReader, separator);
        String[] nextRecord;

        List<Tuple<String, String>> items = new ArrayList<>();
        try {
            while ((nextRecord = csvReader.readNext()) != null) {
                String x;
                String y = null;
                if (nextRecord.length == 2) {
                    x = nextRecord[0];
                    y = nextRecord[1];
                } else if (nextRecord.length == 1) {
                    x = nextRecord[0];
                } else {
                    throw new RuntimeException("CSV columns are more than needed");
                }
                Tuple<String, String> newTuple = Tuple.of(x, y);
                items.add(newTuple);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to open CSV", e);
        }
        return items;
    }
}
