import com.google.cloud.Tuple;

import java.util.List;

public interface TranslateCsvItems {
    List<Tuple<String, String>> translate(List<Tuple<String, String>> items, String lang);
}
