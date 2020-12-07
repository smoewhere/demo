package org.fan.demo.gateway.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.12.7 13:49
 */
@Slf4j
@RestController
public class HelloController {

  @Resource(name = "cachedCompositeRouteLocator")
  private RouteLocator routeLocator;

  @RequestMapping(path = "/hello")
  public String hello() {
    if (routeLocator == null) {
      return "null";
    }
    Flux<Route> routes = routeLocator.getRoutes();
    Flux<Map<String, Object>> mapFlux = routes.map(this::serialize);
    return "hello";
  }

  Map<String, Object> serialize(Route route) {
    HashMap<String, Object> r = new HashMap<>();
    r.put("route_id", route.getId());
    r.put("uri", route.getUri().toString());
    r.put("order", route.getOrder());
    r.put("predicate", route.getPredicate().toString());
    if (!CollectionUtils.isEmpty(route.getMetadata())) {
      r.put("metadata", route.getMetadata());
    }

    ArrayList<String> filters = new ArrayList<>();

    for (int i = 0; i < route.getFilters().size(); i++) {
      GatewayFilter gatewayFilter = route.getFilters().get(i);
      filters.add(gatewayFilter.toString());
    }

    r.put("filters", filters);
    return r;
  }

}
