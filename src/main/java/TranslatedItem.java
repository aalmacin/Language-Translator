public class TranslatedItem {
    private final String toBeTranslated;
    private final String translation;
    private final String audioFileName;

    public TranslatedItem(String toBeTranslated, String translation, String audioFileName) {
        this.toBeTranslated = toBeTranslated;
        this.translation = translation;
        this.audioFileName = audioFileName;
    }

    public String getToBeTranslated() {
        return toBeTranslated;
    }

    public String getTranslation() {
        return translation;
    }

    public String getAudioFileName() {
        return audioFileName;
    }
}
