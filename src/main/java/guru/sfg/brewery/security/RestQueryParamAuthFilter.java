package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class RestQueryParamAuthFilter extends AbstractRestFilter {


    public RestQueryParamAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    protected String getPassword(HttpServletRequest request) {
        return request.getParameter("Api-Secret");
    }

    @Override
    protected String getUsername(HttpServletRequest request) {
        return request.getParameter("Api-Key");
    }
}
