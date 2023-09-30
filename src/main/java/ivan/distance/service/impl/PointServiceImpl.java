package ivan.distance.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.google.gson.Gson;
import ivan.distance.exception.ConnectionException;
import ivan.distance.exception.InvalidDataPoints;
import ivan.distance.exception.NonExistingRoute;
import ivan.distance.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointServiceImpl implements PointService {
    private static final int EARTH_RADIUS_M = 6371000;
    private static final String BASE_URL_OSRM = "http://router.project-osrm.org/route/v1/driving/";

    @Override
    public Double calculateDirectDistance(double lat1, double lon1, double lat2, double lon2) {
        areCoordinatesValid(lat1, lon1, lat2, lon2);
        return haversine(lat1, lon1, lat2, lon2);
    }

    @Override
    public Double findDistanceMyOsrm(double lat1, double lon1, double lat2, double lon2) {
        areCoordinatesValid(lat1, lon1, lat2, lon2);
        return findDistanceByMap(lat1, lon1, lat2, lon2).orElseThrow(() ->
                new RuntimeException("Can't find a way from: " + lat1 + " and " + lon1
                        + " to " + lat2 + " and " + lon2));
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        // Haversine formula: a = sin²(Δlat/2) + cos(lat1) * cos(lat2) * sin²(Δlon/2)
        double a = Math.pow(Math.sin(Math.toRadians(lat2 - lat1) / 2), 2)
                + Math.pow(Math.sin(Math.toRadians(lon2 - lon1) / 2), 2)
                * Math.cos(lat1) * Math.cos(lat2);
        double center = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS_M * center;
    }

    private String createUrl(double lat1, double lon1, double lat2, double lon2) {
        return BASE_URL_OSRM + lat1 + "," + lon1 + ";" + lat2 + "," + lon2 + "?overview=false";
    }

    private HttpURLConnection createConnection(String surl) {
        HttpURLConnection con;
        try {
            final URL url = new URL(surl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
        } catch (IOException e) {
            throw new ConnectionException("Can't create connection to OSRM");
        }
        return con;
    }

    private Optional<Double> findDistanceByMap(double lat1, double lon1,
                                               double lat2, double lon2) {
        String url = createUrl(lat1, lon1, lat2, lon2);
        HttpURLConnection con = createConnection(url);
        Gson gson = new Gson();
        Double distance;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            Map<String, Object> jsonMap = gson.fromJson(content.toString(), Map.class);
            distance = (Double) ((List<Map<String, Object>>)
                    ((List<Map<String, Object>>) jsonMap.get("routes"))
                            .get(0).get("legs")).get(0).get("distance");

        } catch (Exception ex) {
            throw new NonExistingRoute("Existing route");
        }
        return Optional.ofNullable(distance);
    }

    private void areCoordinatesValid(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 < -90 || lat1 > 90 || lon1 < -180 || lon1 > 180)
                || (lat2 < -90 || lat2 > 90 || lon2 < -180 || lon2 > 180)) {
            throw new InvalidDataPoints("Invalid data points");
        }
    }
}
