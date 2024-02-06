package abacus.oidc.github;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class IndexController {

    @GetMapping(path = "/")
    public HashMap<String, String> index() {
        // get a successful user login
        // String result = "src: "+request.getRemoteAddr()+" / dest:
        // "+request.getServerName()+"\n";
        // return result;
        OAuth2User user = ((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
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