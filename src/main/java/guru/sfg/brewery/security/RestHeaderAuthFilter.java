package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jt on 6/19/20.
 */
@Slf4j
public class RestHeaderAuthFilter extends AbstractRestFilter {

    public RestHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    protected String getPassword(HttpServletRequest request) {
        return request.getHeader("Api-Secret");
    }

    @Override
    protected String getUsername(HttpServletRequest request) {
        return request.getHeader("Api-Key");
    }
}
