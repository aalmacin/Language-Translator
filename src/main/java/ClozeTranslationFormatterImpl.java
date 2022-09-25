public class ClozeTranslationFormatterImpl implements TranslationFormatter {
    @Override
    public String format(TranslatedItem translatedItem) {
        return String.format(
                "%s <br><br> %s <br><br> \t <br> [sound:%s] \n",
                translatedItem.getToBeTranslated(),
                translatedItem.getTranslation(),
                translatedItem.getAudioFileName()
        );
    }
}
