import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherApp {

    private static final String API_KEY = "YOUR_OPENWEATHERMAP_API_KEY";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter city name:");
            String city = scanner.nextLine();
            scanner.close();

            String apiUrl = API_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
            String response = sendGetRequest(apiUrl);

            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    displayWeatherData(jsonObject);
                } catch (Exception e) {
                    System.out.println("Error parsing JSON: " + e.getMessage());
                }
            } else {
                System.out.println("Failed to retrieve weather data.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static String sendGetRequest(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } else {
                System.out.println("Failed to retrieve data. Response code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }

    private static void displayWeatherData(JSONObject jsonObject) {
        try {
            System.out.println("Weather Data for " + jsonObject.getString("name"));
            System.out.println("-------------------------------");

            if (jsonObject.has("main")) {
                JSONObject main = jsonObject.getJSONObject("main");
                System.out.println("Temperature: " + main.getDouble("temp") + "°C");
                System.out.println("Feels like: " + main.getDouble("feels_like") + "°C");
                System.out.println("Humidity: " + main.getInt("humidity") + "%");
            }

            if (jsonObject.has("weather")) {
                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                JSONObject weather = weatherArray.getJSONObject(0);
                System.out.println("Weather condition: " + weather.getString("description"));
            }

            System.out.println("-------------------------------");
        } catch (Exception e) {
            System.out.println("Error parsing weather data: " + e.getMessage());
        }
    }
}
