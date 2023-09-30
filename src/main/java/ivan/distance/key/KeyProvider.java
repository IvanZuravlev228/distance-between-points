package ivan.distance.key;

import com.google.common.hash.Hashing;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

@Component
public class KeyProvider {
    private static final String REQUEST_HEADER = "Api-key";

    public String createToken(String pass) {
        return Hashing.sha256()
                .hashString(pass, StandardCharsets.UTF_8)
                .toString();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(REQUEST_HEADER);
    }
}
