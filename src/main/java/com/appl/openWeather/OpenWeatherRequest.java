package com.appl.openWeather;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class OpenWeatherRequest {

    //    private String apiBase = "http://api.openweathermap.org/data/2.5/weather?q=";
    private String apiBase = "http://api.openweathermap.org/data/2.5/weather?id=";
    private String apiForecast = "http://api.openweathermap.org/data/2.5/forecast?q=";
    //private String units = "imperial";
    private String units = "metric";
    private String lang = "en";
    private String apiKey = "bf53fcafded9c19bf042311b7de9483b"; // my API Key in openweathermap
    //public final static Logger log = LoggerFactory.getLogger(OpenWeatherMap.class);


    public void getWeather() {
//        String location = "Samara,Ru";          // q= in apiBase
        String location = "499099";              // id= in apiBase
        String[] arrayStr = fetchCurrentWeather(location); // q= in apiBase

        System.out.println("City        :  " + arrayStr[13]);
        System.out.println("City id     :  " + arrayStr[2]);
        System.out.println("Weather     :  " + arrayStr[0]);
        System.out.println("Temperature :  " + arrayStr[1] + " °С");
        System.out.println("Feels like  :  " + arrayStr[5] + " °С");
        double k = 0.750063755419211;
        DecimalFormat formatter = new DecimalFormat("#0.00");
        String mmrtst = formatter.format(Double.parseDouble(arrayStr[4]) * k);
        System.out.println("Pressure    :  " + mmrtst + " mm.rt.st  (" + arrayStr[4] + " kPa)");
        System.out.println("Humidity    :  " + arrayStr[6] + " %");
        System.out.println("Visibility  :  " + arrayStr[12]);
        System.out.println("Wind speed  :  " + arrayStr[9] + " m/s");
        String windDegree = arrayStr[10];
        System.out.println("Wind degree :  " + windDegree + " °");
        System.out.println("Wind        :  " + getWindDirection(windDegree));
        System.out.println("Clouds      :  " + arrayStr[11] + " %");
        System.out.println("Sunrise     :  " + convertLongToTime(arrayStr[14]));
        System.out.println("Sunset      :  " + convertLongToTime(arrayStr[15]));
        long length = dayLengthMilliseconds(arrayStr[14], arrayStr[15]);
        System.out.println("Day length: :  " + getDayLength(length));

        // wtite weather
        logWeatherToDB(arrayStr);
    }


    private String[] fetchCurrentWeather(String location) throws JSONException {
        String[] result = new String[20];
        result[0] = "error";

        try {
            JSONObject obj = fetch(location);
            result[0]  = obj.getJSONArray("weather").getJSONObject(0).get("description").toString();
            result[1]  = obj.getJSONObject("main").get("temp").toString();
            result[2]  = location;
            result[3]  = obj.getJSONArray("weather").getJSONObject(0).get("id").toString();
            result[4]  = obj.getJSONObject("main").get("pressure").toString();
            result[5]  = obj.getJSONObject("main").get("feels_like").toString();
            result[6]  = obj.getJSONObject("main").get("humidity").toString();
            result[7]  = obj.getJSONObject("main").get("temp_min").toString();
            result[8]  = obj.getJSONObject("main").get("temp_max").toString();
            result[9]  = obj.getJSONObject("wind").get("speed").toString();
            result[10] = obj.getJSONObject("wind").get("deg").toString();
            result[11] = obj.getJSONObject("clouds").get("all").toString();

            result[12] = null;
            if (obj.has("visibility")) {
                result[12] = obj.get("visibility").toString();
            }
            result[13] = obj.get("name").toString();
            result[14] = obj.getJSONObject("sys").get("sunrise").toString();
            result[15] = obj.getJSONObject("sys").get("sunset").toString();

            return result;
        } catch (Exception e) {
            System.out.println("openweathermap error ( api ) : " + e);
            return result;
        }
    }


    /**
     * Return a sentence describing the weather
     */
    public JSONObject fetch(String location) throws IOException, JSONException {
        String apiUrl = apiBase +
                location +
                //URLEncoder.encode(location, "utf-8") +
                "&appid=" + apiKey
                + "&mode=json"
                + "&units=" + units
                //+ "&lang=" + lang
                ;
        System.out.println("apiUrl == " + apiUrl);
        URL url = new URL(apiUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.connect();

        JSONObject response = null;
        try {
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String responseStr = br.readLine();
            response = new JSONObject(responseStr);

            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Error exception: " + e);
        }

        System.out.println("Response == " + response);
        return response;

    }


    public String getWindDirection(String windDegree) {
        String direction = "";
        double angle = Double.parseDouble(windDegree);
        if (angle > 348.75 || angle < 11.25) {         direction = "N";
        } else if (angle > 11.25 && angle < 33.75)   { direction = "N,NE";
        } else if (angle > 33.75 && angle < 56.25)   { direction = "NE";
        } else if (angle > 56.25 && angle < 78.15)   { direction = "E,NE";
        } else if (angle > 78.15 && angle < 101.25)  { direction = "E";
        } else if (angle > 101.25 && angle < 123.75) { direction = "E,SE";
        } else if (angle > 123.75 && angle < 146.25) { direction = "SE";
        } else if (angle > 146.25 && angle < 168.75) { direction = "S,SE";
        } else if (angle > 168.75 && angle < 191.25) { direction = "S";
        } else if (angle > 191.25 && angle < 213.75) { direction = "S,SW";
        } else if (angle > 213.75 && angle < 236.25) { direction = "SW";
        } else if (angle > 236.25 && angle < 258.75) { direction = "W,SW";
        } else if (angle > 258.75 && angle < 281.25) { direction = "W";
        } else if (angle > 281.25 && angle < 303.75) { direction = "W,NW";
        } else if (angle > 303.75 && angle < 326.75) { direction = "NW";
        } else if (angle > 326.75 && angle < 348.75) { direction = "N,NW"; }
        return direction;
    }


    private String convertLongToTime(String moment) {
        LocalDateTime sunriseTime = new LocalDateTime(1000 * Long.parseLong(moment));
        String sunriseTimeString = sunriseTime.toLocalTime().toString();
        sunriseTimeString = sunriseTimeString.split("\\.")[0];
        return sunriseTimeString;
    }


    private void logWeatherToDB(String[] arrayStr) {
        String city = arrayStr[13];
        int city_id = Integer.parseInt(arrayStr[2]);
        String weather = arrayStr[0];
        int temperature = (int) Double.parseDouble(arrayStr[1]);
        double feels_like = Double.parseDouble(arrayStr[5]);
        int pressure = Integer.parseInt(arrayStr[4]);
        int humidity = Integer.parseInt(arrayStr[6]);

        int visibility = 0;
        if (arrayStr[12] != null) {
            visibility = Integer.parseInt(arrayStr[12]);
        }

        int wind_speed = (int) Double.parseDouble(arrayStr[9]);
        String windDegree = arrayStr[10];
        int wind_degree = Integer.parseInt(windDegree);
        String wind = getWindDirection(windDegree);
        int clouds = Integer.parseInt(arrayStr[11]);
        long sunrise = Long.parseLong(arrayStr[14]);
        long sunset  = Long.parseLong(arrayStr[15]);

        long length = dayLengthMilliseconds(arrayStr[14], arrayStr[15]);
        String dayLength = getDayLength(length);

        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/db1?currentSchema=schema1", "postgres", "pass4p");

            DateTime current = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            String currentDate = fmt.print(current);
            System.out.println("currentDate = " + currentDate);                             //            LocalDate dateFormat = new LocalDate();   //            System.out.println("dateFormat = " + dateFormat.toString());


            String insertQuery = "insert into weather" +
                    "(time, city, city_id, weather, temperature, feels_like, pressure, humidity, visibility," +
                    " wind_speed, wind_degree, wind, clouds, sunrise, sunset, sunrise_time, sunset_time, day_length)" +
                    " values ('" +
                    currentDate + "','" +
                    city + "','" +
                    city_id + "','" +
                    weather + "','" +
                    temperature + "','" +
                    feels_like + "','" +
                    pressure + "','" +
                    humidity + "','" +
                    visibility + "','" +
                    wind_speed + "','" +
                    wind_degree + "','" +
                    wind + "','" +
                    clouds + "','" +
                    sunrise + "','" +
                    sunset + "','" +
                    convertLongToTime(arrayStr[14]) + "','" +
                    convertLongToTime(arrayStr[15]) + "','" +
                    dayLength +
                    "')";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            System.out.println("logWeatherToDB SQL exception: " + sqle);
        }
    }


    private String getDayLength(Long seconds){
        return (seconds / 3600) + "h " + ((seconds % 3600) / 60) + "m " + (seconds % 60) + "s";
    }


    private long dayLengthMilliseconds(String sunrise, String sunset) {
        return Long.parseLong(sunset) - Long.parseLong(sunrise);
    }


}
