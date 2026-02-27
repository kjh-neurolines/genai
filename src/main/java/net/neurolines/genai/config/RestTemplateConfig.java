package net.neurolines.genai.config;

import net.neurolines.genai.handler.RestTemplateResponseErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    String neuroAPi = "https://api.neurochem.io";

    String dopeApi = "http://103.57.61.102:12001";

    String authApi = "http://auth.neurochem.io/api";

    String chatApi = "http://103.57.61.102:14010";


    @Bean("neuroApiRestTemplate")
    public RestTemplate neuroTemplate(RestTemplateBuilder restTemplateBuilder)
    {
        return restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .connectTimeout (Duration.ofMillis(120000))
                .readTimeout(Duration.ofMillis(120000))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                .rootUri(neuroAPi)
                .errorHandler(new RestTemplateResponseErrorHandler())
                .defaultHeader(HttpHeaders.CONNECTION, "close") // Keep-Alive 비활성화

                .build();
    }


    @Bean("dopeApiRestTemplate")
    public RestTemplate dopeTemplate(RestTemplateBuilder restTemplateBuilder)
    {
        return restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .connectTimeout(Duration.ofMillis(600000))
                .readTimeout(Duration.ofMillis(600000))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                .rootUri(dopeApi)
                .errorHandler(new RestTemplateResponseErrorHandler())
                .defaultHeader(HttpHeaders.CONNECTION, "close") // Keep-Alive 비활성화

                .build();
    }

    @Bean("authApiRestTemplate")
    public RestTemplate authTemplate(RestTemplateBuilder restTemplateBuilder)
    {
        return restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .connectTimeout(Duration.ofMillis(600000))
                .readTimeout(Duration.ofMillis(600000))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                .rootUri(authApi)
                .errorHandler(new RestTemplateResponseErrorHandler())
                .defaultHeader(HttpHeaders.CONNECTION, "close") // Keep-Alive 비활성화

                .build();
    }

    @Bean("chatApiRestTemplate")
    public RestTemplate chatTemplate(RestTemplateBuilder restTemplateBuilder)
    {
        return restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .connectTimeout(Duration.ofMillis(600000))
                .readTimeout(Duration.ofMillis(600000))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                .rootUri(chatApi)
                .errorHandler(new RestTemplateResponseErrorHandler())
                .defaultHeader(HttpHeaders.CONNECTION, "close") // Keep-Alive 비활성화

                .build();
    }
}