package ivan.distance.service;

public interface PointService {
    Double calculateDirectDistance(double lat1, double lon1, double lat2, double lon2);

    Double findDistanceMyOsrm(double lat1, double lon1, double lat2, double lon2);
}
