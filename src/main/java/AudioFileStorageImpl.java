import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AudioFileStorageImpl implements AudioFileStorage {

    @Override
    public void store(String fileName, byte[] audioContent) {
        String localMp3Path = String.format("audio/%s", fileName);
        Path path = Paths.get(localMp3Path);

        // Do not rewrite audio if it already exists
        if (Files.exists(path)) {
            return;
        }

        try {
            try (OutputStream out = new FileOutputStream(localMp3Path)) {
                out.write(audioContent);
                System.out.printf("Audio content written to file: %s%n", localMp3Path);
            }
        } catch(Exception e) {
            throw new RuntimeException(
                    String.format("Failed to open file: %s", localMp3Path),
                    e
            );
        }
    }
}
