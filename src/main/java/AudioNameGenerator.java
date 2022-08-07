import java.util.Base64;

public class AudioNameGenerator {
    public String generate(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes())
                .replace('/', '_')
                .replace('+', '-')
                + ".mp3";
    }
}
