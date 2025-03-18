import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * # Weather Data Analyzer
 * This program analyzes weather data from a CSV file.
 *
 * ## Features:
 * - Parses CSV weather data
 * - Calculates:
 *   - Average temperature per month
 *   - Days above a temperature threshold
 *   - Number of rainy days
 * - Uses modern Java features like records, enhanced switch, lambdas, and streams.
 *
 * ## Usage:
 * ```java
 * WeatherDataAnalyzer.main(new String[]{});
 * ```
 */
public class WeatherDataAnalyzer {


    /**
     * Represents a single weather entry.
     * Uses Java Records for simplicity.
     */
    record WeatherRecord(String date, double temperature, double humidity, double precipitation) {}

    /**
     * Parses the CSV file and returns a list of WeatherRecord objects.
     */
    static List<WeatherRecord> parseCSV(String filePath) throws IOException {
        return Files.lines(Path.of(filePath))
                .skip(1) // Skip header row
                .map(line -> line.split(","))
                .map(parts -> new WeatherRecord(parts[0], 
                                                Double.parseDouble(parts[1]), 
                                                Double.parseDouble(parts[2]), 
                                                Double.parseDouble(parts[3])))
                .toList();
    }

    /**
     * Calculates the average temperature for a given month.
     */
    static double averageTemperature(List<WeatherRecord> data, String month) {
        return data.stream()
                .filter(record -> record.date.startsWith(month))
                .mapToDouble(WeatherRecord::temperature)
                .average()
                .orElse(Double.NaN);
    }

    /**
     * Finds days where temperature exceeds the given threshold.
     */
    static List<String> daysAboveThreshold(List<WeatherRecord> data, double threshold) {
        return data.stream()
                .filter(record -> record.temperature > threshold)
                .map(WeatherRecord::date)
                .toList();
    }

    /**
     * Counts the number of rainy days (precipitation > 0).
     */
    static long countRainyDays(List<WeatherRecord> data) {
        return data.stream().filter(record -> record.precipitation > 0).count();
    }

    /**
     * Uses an enhanced switch to categorize weather temperatures.
     */
    static String categorizeTemperature(double temp) {
        return switch ((int) temp) {
            case int t when t >= 35 -> "Hot";
            case int t when t >= 25 -> "Warm";
            case int t when t >= 15 -> "Cool";
            default -> "Cold";
        };
    }

    public static void main(String[] args) throws IOException {
        String filePath = "data/weatherdata.csv";
        List<WeatherRecord> weatherData = parseCSV(filePath);

        System.out.println("""
                ========== Weather Data Analysis ==========
                Average Temperature in August: """ + averageTemperature(weatherData, "2023-08") + """
                
                Days above 30°C: """ + daysAboveThreshold(weatherData, 30) + """
                
                Count of Rainy Days: """ + countRainyDays(weatherData) + """
                """);

        weatherData.forEach(record -> 
                System.out.println(record.date + " was " + categorizeTemperature(record.temperature)));
    }
}
