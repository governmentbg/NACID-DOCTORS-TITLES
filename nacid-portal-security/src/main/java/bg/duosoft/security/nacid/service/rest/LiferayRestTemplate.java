package bg.duosoft.security.nacid.service.rest;

import bg.duosoft.security.nacid.service.rest.objects.BaseResult;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LiferayRestTemplate extends RestTemplate {

    private String username;
    private String password;

    public LiferayRestTemplate(String username, String password) {
        this.username = username;
        this.password = password;
        addAuthentication();
    }

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables) throws RestClientException {
        T res = super.getForObject(url, responseType, urlVariables);
        if (res instanceof BaseResult) {
            checkResponseForErrors((BaseResult) res);
        }
        return res;

    }

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> urlVariables) throws RestClientException {
        T res = super.getForObject(url, responseType, urlVariables);
        if (res instanceof BaseResult) {
            checkResponseForErrors((BaseResult) res);
        }
        return res;
    }

    @Override
    public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
        T res = super.getForObject(url, responseType);
        if (res instanceof BaseResult) {
            checkResponseForErrors((BaseResult) res);
        }
        return res;
    }

    private void addAuthentication() {
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("Username is mandatory for Basic Auth");
        }
        List<ClientHttpRequestInterceptor> interceptors = Arrays.asList((ClientHttpRequestInterceptor) new BasicAuthInterceptor(username, password));
        setRequestFactory(new InterceptingClientHttpRequestFactory(getRequestFactory(), interceptors));
    }
    private void checkResponseForErrors(BaseResult result) {
        if (!StringUtils.isEmpty(result.getException())) {
            throw new RuntimeException("Exception: " + result.getException() + "\nMessage:" + result.getMessage());
        }
    }
}

