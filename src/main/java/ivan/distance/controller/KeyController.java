package ivan.distance.controller;

import ivan.distance.key.KeyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class KeyController {
    private final KeyProvider jwtTokenProvider;

    @GetMapping("/key/{pass}")
    public ResponseEntity<String> getKey(@PathVariable String pass) {
        return new ResponseEntity<>(jwtTokenProvider.createToken(pass), HttpStatus.CREATED);
    }
}
