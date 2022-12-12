import java.nio.file.Files;
import java.nio.file.Path;

public class AudioFileCheckerImpl implements AudioFileChecker {
    @Override
    public boolean exists(String fileName) {
        return Files.exists(Path.of(fileName));
    }
}
