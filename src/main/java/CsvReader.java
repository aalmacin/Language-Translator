public interface CsvReader<T> {
    T readCsv(String fileName);
    T readCsv(String fileName, char separator);
}
