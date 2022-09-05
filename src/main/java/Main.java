import com.google.cloud.Tuple;

import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Args must be passed. Input File name, Output File name");
        }

        final String inputFilename = args[0];
        final String outputFilename = args[1];
        // Example
//        String language = "en";
        final String language = args[2];

        final CsvReader<List<Tuple<String, String>>> csvReader = new CsvReaderImpl();
        final TranslateCsvItems translateCsvItems = new TranslateCsvItemsImpl();
        final TextToSpeechAudioGenerator textToSpeechAudioGenerator =
                new TextToSpeechAudioGeneratorImpl();
        final AudioNameGenerator audioNameGenerator = new AudioNameGenerator();
        final AudioFileStorage audioFileStorage = new AudioFileStorageImpl();
        final AudioFileChecker audioFileChecker = new AudioFileCheckerImpl();
        final AnkiWriter ankiWriter = new AnkiWriterImpl(outputFilename);
        final TranslationFormatter translationFormatter = new TranslationFormatterImpl();

        final List<Tuple<String, String>> items = csvReader.readCsv(inputFilename, '\t');
        final List<Tuple<String, String>> translatedItems = translateCsvItems.translate(items, language);
        translatedItems.forEach(item -> {
            final String toBeTranslated = item.x();
            final String translation = item.y();
            final String audioFileName = audioNameGenerator.generate(toBeTranslated);
            if (!audioFileChecker.exists(audioFileName)) {
                final byte[] audio = textToSpeechAudioGenerator.generateAudio(toBeTranslated);
                audioFileStorage.store(audioFileName, audio);
            }
            TranslatedItem translatedItem = new TranslatedItem(toBeTranslated, translation, audioFileName);
            String translateText = translationFormatter.format(translatedItem);
            try {
                ankiWriter.write(translateText);
            } catch (IOException e) {
                throw new RuntimeException(String.format("Failed to write to anki %s", translateText));
            }
        });
    }
}
