import com.google.cloud.texttospeech.v1.*;
import com.google.cloud.translate.v3.*;
import com.google.protobuf.ByteString;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TranslateAndGenerateAudio implements Runnable {
    private final String phrase;
    private final String outputFilename;
    private final String language;

    public TranslateAndGenerateAudio(String phrase, String outputFilename, String language) {
        this.phrase = phrase;
        this.outputFilename = outputFilename;
        this.language = language;
    }

    @Override
    public void run() {
        try {
            try {
                ArrayList<String> translations = translateText(phrase, language);
                String mp3Filename = generateAudio(phrase);
                if(translations.isEmpty()) {
                    String message = "Empty Result for " + phrase;
                    System.out.println(message);
                    throw new RuntimeException(message);
                }
                String formattedTranslations = formatTranslations(translations);
                String toWrite = phrase
                        + " <br><br> " +
                        "[sound:" + mp3Filename + "] "
                        + "\t" + formattedTranslations;
                writeToFile(toWrite);
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed translating: " + phrase);
        }
    }

    // Set and pass variables to overloaded translateText() method for translation.
    // Translate text to target language.
    private ArrayList<String> translateText(String text, String lang)
            throws IOException {
        String projectId = "translate-raidrin";
        String targetLanguage = lang;

        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            // Supported Locations: `global`, [glossary location], or [model location]
            // Glossaries must be hosted in `us-central1`
            // Custom Models must use the same location as your model. (us-central1)
            LocationName parent = LocationName.of(projectId, "global");

            // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setTargetLanguageCode(targetLanguage)
                            .addContents(text)
                            .build();

            TranslateTextResponse response = client.translateText(request);

            ArrayList<String> translations = new ArrayList<>();

            // Display the translation for each input text provided
            for (Translation translation : response.getTranslationsList()) {
                String translatedText = translation.getTranslatedText();
                if(isNotBlank(translatedText))
                    translations.add(translatedText);
            }
            return translations;
        }
    }

    private String generateAudio(String phrase) throws IOException {
        String mp3Filename =  Base64.getEncoder().encodeToString(phrase.getBytes())
                .replace('/', '_')
                .replace('+', '-')
                + ".mp3";
        String localMp3Filename = "audio/" + mp3Filename;
        Path path = Paths.get(localMp3Filename);

        // Do not rewrite audio if it already exists
        if (Files.exists(path)) {
            return mp3Filename;
        }
        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder().setText(phrase).build();

            // Build the voice request, select the language code ("en-US") and the ssml voice gender
            // ("neutral")
            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setLanguageCode("fr-FR")
                            .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                            .build();

            // Select the type of audio file you want returned
            AudioConfig audioConfig =
                    AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

            // Perform the text-to-speech request on the text input with the selected voice parameters and
            // audio file type
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Get the audio contents from the response
            ByteString audioContents = response.getAudioContent();

            // Write the response to the output file.
            try (OutputStream out = new FileOutputStream(localMp3Filename)) {
                out.write(audioContents.toByteArray());
                System.out.println("Audio content written to file \"" + localMp3Filename + "\"");
            }
            return mp3Filename;
        }
    }

    private synchronized void writeToFile(String toWrite) throws IOException {
        File myTsv = new File(outputFilename);
        FileWriter myTsvFileWriter = new FileWriter(myTsv, true);
        myTsvFileWriter.write(toWrite);
        myTsvFileWriter.close();
    }

    private String formatTranslations(ArrayList<String> translations) {
        if(translations.size() > 1) {
            return translations.stream()
                    .reduce("", (a, i) -> a + " <br><br> / <br><br> " + i + "\n");
        }
        return translations.get(0) + "\n";
    }
}
