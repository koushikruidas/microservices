package com.gateway.ApiGateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.util.StringUtils;
import org.springframework.http.HttpHeaders;

 


/*@Component
public class TokenRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

	private WebClient client = WebClient.builder().baseUrl("lb://http://spring-security-server").build();
	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

				// Check if a JWT token is present in the request header
				String token = request.getHeaders().getFirst("Authorization");
				if (token == null || token.isEmpty()) {
					String jwtToken = signin();
					if (jwtToken == null) {
						// If the Security Server returns an error, return an error to the client
						exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
						return exchange.getResponse().setComplete();
					}
					token = jwtToken;
				}
				else {
					boolean isValid = client.get().uri("/security/api/auth/validateToken/"+token).retrieve().bodyToMono(Boolean.class).block();
					if(!isValid) {
						signin();
					}
				}

				// Add the token to the request header and forward the request to the downstream
				// service
				ServerHttpRequest.Builder requestBuilder = request.mutate();
				requestBuilder.header("Authorization", token);
				ServerHttpRequest modifiedRequest = requestBuilder.build();
				return chain.filter(exchange.mutate().request(modifiedRequest).build());
//			}
//			return chain.filter(exchange);
		};
		
	}
	
	public String signin() {
		// If no token is present, send the request to the Security Server to generate
		// one
		String jwtToken = client.post().uri("/security/api/auth/signin").retrieve().bodyToMono(String.class).block();
		return jwtToken;
	}
}*/


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private WebClient.Builder webClient;
//	private WebClient client = WebClient.builder().baseUrl("http://spring-security-server").build();

    public AuthenticationFilter(WebClient.Builder webClient) {
        super(Config.class);
        this.webClient = webClient;
    }

    @SuppressWarnings("deprecation")
	@Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String token = headers.get(HttpHeaders.AUTHORIZATION).get(0);
            if (!StringUtils.isEmpty(token)) {
//            	System.out.println("Token: "+token);
                // Verify token using the spring-security-server
                return webClient.baseUrl("http://spring-security-server").build().get()
                        .uri("/security/api/auth/validateToken/"+token)
                        .retrieve()
                        /*
                        
                        * .retrieve() is a method provided by the Spring WebFlux WebClient 
                        * that allows you to retrieve the response from the remote service as a reactive stream. 
                        * 
                        * It returns a BodyExtractor instance, which can be used to extract the response body.
                        * 
                        * .bodyToMono(Boolean.class) is a method provided by the Spring WebFlux ClientResponse class 
                        * that converts the response body to a Mono of the specified type.
                        * In this case, it converts the response body to a Mono<Boolean>. 
                        * 
                        * Mono is a reactive stream publisher that emits zero or one item. In this case, 
                        * the Mono will emit a single Boolean value representing whether the user is authorized or not.                        *
                        *
                        */
                        .bodyToMono(Boolean.class)
                        .flatMap(isAuthorized -> {  
//                        	System.out.println("PRINT: "+isAuthorized);
                        	/*
                        
                        * While flatMap is commonly used to flatten nested data structures, 
                        * it is also useful for chaining reactive streams and handling asynchronous operations 
                        * in a functional reactive programming style.
                        * 
                        * In reactive programming, flatMap is used to chain asynchronous operations together. 
                        * It is similar to the map operator, but with an important difference. 
                        * When you use map, you apply a function to each item in a stream, 
                        * and emit the results as a new stream.
                        * 
                        * With flatMap, you can apply a function that returns another stream, 
                        * and then flatten that stream into a single stream. 
                        * This allows you to chain asynchronous operations together, 
                        * 
                        */
                            if (isAuthorized) {
                                // Add the token to downstream request headers
                                exchange.getRequest().mutate()
                                /*
                                 * In Spring WebFlux, ServerHttpRequest and ServerHttpResponse objects are immutable. 
                                 * This means that you can't modify them directly once they are created.
                                 * 
                                 * However, you can create a new request or response object with modifications 
                                 * using the mutate() method.
                                 * 
                                 * So, when you call exchange.getRequest().mutate(), 
                                 * you are creating a new mutable copy of the original request object. 
                                 * This allows you to modify the request headers, cookies, path, etc. as needed.
                                 * 
                                 * After you make the desired modifications, you can call build() 
                                 * to create a new immutable request object with the changes applied.
                                 * 
                                 * */
                                        .header("X-Auth-Token", token)
                                        .build();
                                return chain.filter(exchange);
                            } else {
                                // Token is not authorized, send to the spring-security-server
//                                return webClient.baseUrl("http://spring-security-server").build().post()
//                                        .uri("/security/api/auth/signin")
//                                        .retrieve()
//                                        .toBodilessEntity()
//                                        /*
//                                         * The retrieve() method is used to retrieve the response body as a Mono. 
//                                         * Then, the toBodilessEntity() method is called to convert the response to a Mono of ResponseEntity<Void>. 
//                                         * Finally, the flatMap() method is used to handle the response based on the status code.
//                                         * */
//                                        .flatMap(response -> {
//                                            HttpStatus status = response.getStatusCode();
//                                            if (status.is2xxSuccessful()) {
//                                                return chain.filter(exchange);
//                                            } else {
//                                                exchange.getResponse().setStatusCode(status);
//                                                return exchange.getResponse().setComplete();
//                                            }
//                                        });
                            	 exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                 return exchange.getResponse().setComplete();
                            }
                        });
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    public static class Config {
    }
}
