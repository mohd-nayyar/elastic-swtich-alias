package com.example.elasticsearchswitchalias.config;

import com.example.elasticsearchswitchalias.vo.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Component("restClient")
public class RestClient implements RestConstants {
	private final Logger logger = LoggerFactory.getLogger(RestClient.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	public RestResponse get(String url, Map<String, String> paramMap, Map<String, String> headerMap, String body) {
        if ( paramMap != null && paramMap.size() > 0){
            boolean isFirst = true;
            for (Entry<String,String> entry: paramMap.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                if ( isFirst){
                    url =  url + "?";
                    isFirst =false;
                } else{
                    url = url + "&";
                }
                url= url + key + "=" + value;

            }
        }
		return excecute(url, HttpMethod.GET, paramMap, headerMap, body);
	}

	public RestResponse post(String url, Map<String, String> paramMap, Map<String, String> headerMap, String body) {
		RestResponse restResponse = new RestResponse();
		try {
			HttpHeaders requestHeaders = new HttpHeaders();
			if(headerMap != null && !headerMap.isEmpty()) {
				Set<Entry<String, String>> entrySet = headerMap.entrySet();
				for(Entry<String, String> entry : entrySet) {
					requestHeaders.set(entry.getKey(), entry.getValue());
				}
			}
			requestHeaders.set("Accept", "text/html,application/xml;q=0.9,application/xhtml+xml,text/xml;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");

            MultiValueMap<String, String> mapa = new LinkedMultiValueMap<String, String>();
			mapa.setAll(paramMap);
            ResponseEntity<String> responseEntity = null;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

			builder.queryParams(mapa);

			Long startTime = System.currentTimeMillis();


			if(body != null && !body.trim().equals("")) {
				HttpEntity<String> httpEntity = new HttpEntity<String>(body, requestHeaders);
				responseEntity = restTemplate.exchange( builder.toUriString(), HttpMethod.POST, httpEntity, String.class);
			} else {
                HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(mapa, requestHeaders);
                responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, httpEntity, String.class);
			}

            Long endTime = System.currentTimeMillis();
            logger.debug("Total time for method: POST URL: " + url + ",  " + (endTime -startTime));

			if(responseEntity != null) {
				restResponse.setStatusCode(responseEntity.getStatusCode().value());
				restResponse.setHttpStatusCode(responseEntity.getStatusCode());
				restResponse.setResponse(responseEntity.getBody());
				restResponse.setStatusMessage(responseEntity.getStatusCode().getReasonPhrase());
				restResponse.setStatus(STATUS_SUCCESS);
                restResponse.setHttpHeaders(responseEntity.getHeaders());
			}
		} catch(Exception ex) {
			logger.error("Exception occured: " + ex.toString());

			if(ex instanceof HttpClientErrorException) {
                HttpClientErrorException httpClientErrorException = (HttpClientErrorException) ex;
                restResponse.setStatusCode(httpClientErrorException.getStatusCode().value());
				restResponse.setHttpStatusCode(httpClientErrorException.getStatusCode());
                restResponse.setResponse(httpClientErrorException.getResponseBodyAsString());
                restResponse.setStatusMessage(httpClientErrorException.getStatusCode().getReasonPhrase());
            }else {
                restResponse.setStatusCode(0);
				restResponse.setHttpStatusCode(HttpStatus.valueOf(0));
                restResponse.setResponse(ex.getMessage());
                restResponse.setStatusMessage(ex.getMessage());
            }
            restResponse.setStatus(STATUS_FAILED);
		}
		return  restResponse;

	}

	public RestResponse put(String url, Map<String, String> paramMap, Map<String, String> headerMap, String body) {
		if ( paramMap != null && paramMap.size() > 0){
			boolean isFirst = true;
			for (Entry<String,String> entry: paramMap.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				if ( isFirst){
					url =  url + "?";
					isFirst =false;
				} else{
					url = url + "&";
				}
				url= url + key + "=" + value;

			}
		}
		return excecute(url, HttpMethod.PUT, paramMap, headerMap, body);
	}

    public RestResponse delete(String url, Map<String, String> paramMap, Map<String, String> headerMap, String body) {
        return excecute(url, HttpMethod.DELETE, paramMap, headerMap, body);
    }

	public RestResponse head(String url, Map<String, String> paramMap, Map<String, String> headerMap, String body) {
		if ( paramMap != null && paramMap.size() > 0){
			boolean isFirst = true;
			for (Entry<String,String> entry: paramMap.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				if ( isFirst){
					url =  url + "?";
					isFirst =false;
				} else{
					url = url + "&";
				}
				url= url + key + "=" + value;

			}
		}
		return excecute(url, HttpMethod.HEAD, paramMap, headerMap, body);
	}
	
	private RestResponse excecute(String url, HttpMethod method, Map<String, String> paramMap, Map<String, String> headerMap, String body) {
		RestResponse restResponse = new RestResponse();
		try {
			HttpHeaders requestHeaders = new HttpHeaders();
			if(headerMap != null && !headerMap.isEmpty()) {
				Set<Entry<String, String>> entrySet = headerMap.entrySet();
				for(Entry<String, String> entry : entrySet) {
					requestHeaders.set(entry.getKey(), entry.getValue());
				}
			}
			requestHeaders.set("Accept", "text/html,application/xml;q=0.9,application/xhtml+xml,text/xml;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
	
			HttpEntity<String> httpEntity = null;
			if(body != null && !body.trim().equals("")) {
				httpEntity = new HttpEntity<String>(body, requestHeaders);
			} else {
				httpEntity = new HttpEntity<String>(requestHeaders);
			}
            Long startTime = System.currentTimeMillis();
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, httpEntity, String.class, paramMap);
			Long endTime = System.currentTimeMillis();
            logger.debug("Total time for method: " + method.name() + " URL: " + url + ",  " + (endTime -startTime));
			if(responseEntity != null) {
				restResponse.setStatusCode(responseEntity.getStatusCode().value());
				restResponse.setHttpStatusCode(responseEntity.getStatusCode());
				restResponse.setResponse(responseEntity.getBody());
				restResponse.setStatusMessage(responseEntity.getStatusCode().getReasonPhrase());
				restResponse.setStatus(STATUS_SUCCESS);
			}
		} catch(Exception ex) {
			logger.error("Exception occured:"+ex.toString());

            if(ex instanceof HttpClientErrorException) {
				logger.error("HttpClientErrorException {}",ex);
                HttpClientErrorException httpClientErrorException = (HttpClientErrorException) ex;
                restResponse.setStatusCode(httpClientErrorException.getStatusCode().value());
				restResponse.setHttpStatusCode(httpClientErrorException.getStatusCode());
                restResponse.setResponse(httpClientErrorException.getResponseBodyAsString());
                restResponse.setStatusMessage(httpClientErrorException.getStatusCode().getReasonPhrase());
                logger.error("HttpClientErrorException with statusCode {}", httpClientErrorException.getStatusCode().value());
            }else if(ex instanceof HttpServerErrorException) {
				logger.error("HttpServerErrorException {}" ,ex);
            	HttpServerErrorException httpServerErrorException = (HttpServerErrorException) ex;
                restResponse.setStatusCode(httpServerErrorException.getStatusCode().value());
				restResponse.setHttpStatusCode(httpServerErrorException.getStatusCode());
                restResponse.setResponse(httpServerErrorException.getResponseBodyAsString());
                restResponse.setStatusMessage(httpServerErrorException.getStatusCode().getReasonPhrase());
				logger.error("HttpServerErrorException with statusCode {}", httpServerErrorException.getStatusCode().value());
			}else {
				logger.error("HttpClientErrorException {}",ex);
				restResponse.setStatusCode(500);
				restResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
				restResponse.setResponse(ex.getMessage());
				restResponse.setStatusMessage(ex.getMessage());
			}
            restResponse.setStatus(STATUS_FAILED);

		}
		return  restResponse;
	}

//    @PostConstruct
    public void init() {
        try {
            List<HttpMessageConverter<?>> convertorList = restTemplate.getMessageConverters();
            for (HttpMessageConverter converter : convertorList) {
                if (converter instanceof StringHttpMessageConverter) {
                    Field charsetField = converter.getClass().getField("DEFAULT_CHARSET");
                    Charset charset = Charset.forName("UTF-8");

                    charsetField.setAccessible(true);

                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(charsetField, charsetField.getModifiers() & ~Modifier.FINAL);

                    charsetField.set(null, charset);
                }
            }
        } catch (Exception ex) {
            logger.error("Error in setting charset");
        }
    }

}
