package com.wiseco.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator guoneiRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();

        routes.route("path_to_guonei", r -> r.path("/guonei").uri("https://news.baidu.com")).build();

        return routes.build();
    }

    @Bean
    public RouteLocator guojRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();

        routes.route("path_to_guoji", r -> r.path("/guoji").uri("https://news.baidu.com")).build();

        return routes.build();
    }
}
