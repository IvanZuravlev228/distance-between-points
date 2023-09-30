package ivan.distance.intercaptor;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import ivan.distance.key.KeyProvider;
import ivan.distance.key.RateLimit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RateLimit pricingPlanService;
    private final KeyProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String apiKey = jwtTokenProvider.resolveToken(request);
        if (apiKey == null || apiKey.isEmpty()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Header: Api-key");
            return false;
        }

        if (apiKey.length() != 64) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid value in API key header");
            return false;
        }

        Bucket tokenBucket = pricingPlanService.resolveBucket(apiKey);
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader("Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                    "You have reached your request limit");
            return false;
        }
    }
}
