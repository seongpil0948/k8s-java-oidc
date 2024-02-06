package abacus.oidc.sample1;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;

@RestController
public class IndexController {

    // logger
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(IndexController.class);

    @GetMapping(path = "/")
    public HashMap<String, String> index(HttpServletRequest request) {
        // get a successful user login
        String result = "src: " + request.getRemoteAddr() + " / dest:// " + request.getServerName() + "\n";
        // logging received request
        log.info("receive request" + result);
        OAuth2User user = ((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        // logging user attributes
        log.info("user attributes: " + user.getAttributes().toString());
        return new HashMap<String, String>() {
            {
                put("hello", user.getAttribute("name"));
                put("your email is", user.getAttribute("email"));
            }
        };
    }

    @GetMapping(path = "/unauthenticated")
    public HashMap<String, String> unauthenticatedRequests() {
        return new HashMap<String, String>() {
            {
                put("this is ", "unauthenticated endpoint");
            }
        };
    }
}