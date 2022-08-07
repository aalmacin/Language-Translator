import com.google.cloud.Tuple;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class TranslateCsvItemsImpl implements TranslateCsvItems {
    private final Translator translator;

    public TranslateCsvItemsImpl() {
        this.translator = new TranslatorImpl();
    }
    @Override
    public List<Tuple<String, String>> translate(List<Tuple<String, String>> items, String lang) {
        return items.stream().map(
                item -> {
                    if (isBlank(item.y())) {
                        String newY = translator.translateText(item.x(), lang).stream().reduce(
                                "",
                                (a, i) -> a != null ? a + "<br>" + i : ""
                        );
                        return Tuple.of(item.x(), newY);
                    }
                    return item;
                }
        ).collect(Collectors.toList());
    }
}
