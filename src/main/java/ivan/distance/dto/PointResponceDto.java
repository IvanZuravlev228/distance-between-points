package ivan.distance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PointResponceDto {
    private final Double distance;

    public PointResponceDto(Double distance) {
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }
}
