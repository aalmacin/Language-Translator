import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AnkiWriterImpl implements AnkiWriter {
    private final File myTsv;
    public AnkiWriterImpl(String fileName) {
        myTsv = new File(fileName);
    }

    @Override
    public void write(String text) throws IOException {
        try (FileWriter myTsvFileWriter = new FileWriter(myTsv, true)) {
            myTsvFileWriter.write(text);
        }
    }
}
