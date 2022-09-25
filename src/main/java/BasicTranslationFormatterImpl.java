public class BasicTranslationFormatterImpl implements TranslationFormatter {
    @Override
    public String format(TranslatedItem translatedItem) {
        return String.format(
                "%s <br><br> \t %s <br><br> [sound:%s]\n",
                translatedItem.getTranslation(),
                translatedItem.getToBeTranslated(),
                translatedItem.getAudioFileName()
        );
    }
}
