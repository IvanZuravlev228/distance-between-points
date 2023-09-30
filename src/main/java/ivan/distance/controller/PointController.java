package ivan.distance.controller;

import ivan.distance.dto.PointResponceDto;
import ivan.distance.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/distance")
public class PointController {
    private final PointService pointService;

    @GetMapping("/direct")
    public ResponseEntity<PointResponceDto> getDistance(@RequestParam Double lat1,
                                                        @RequestParam Double lon1,
                                                        @RequestParam Double lat2,
                                                        @RequestParam Double lon2) {
        return new ResponseEntity<>(new PointResponceDto(
                pointService.calculateDirectDistance(lat1, lon1, lat2, lon2)), HttpStatus.OK);
    }

    @GetMapping("/by-map")
    public ResponseEntity<PointResponceDto> getDistanceByMap(@RequestParam Double lat1,
                                                             @RequestParam Double lon1,
                                                             @RequestParam Double lat2,
                                                             @RequestParam Double lon2) {
        return new ResponseEntity<>(new PointResponceDto(
                pointService.findDistanceMyOsrm(lat1, lon1, lat2, lon2)), HttpStatus.OK);
    }
}
