import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class AudioNameGenerator {
    public String generate(String str) {
        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8) + ".mp3";
    }
}
