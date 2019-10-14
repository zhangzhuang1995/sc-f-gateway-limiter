package com.forezp.limiter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ZhangZhuang
 * @date 2019/10/14
 * @description
 */
public class HostAddrKeyResolver implements KeyResolver {
    /**
     * KeyResolver需要实现resolve方法，比如根据Hostname进行限流，则需要用hostAddress去判断。
     * 实现完KeyResolver之后，需要将这个类的Bean注册到Ioc容器中。
     * 根据Hostname限流
     *
     * @param exchange
     * @return
     */
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}
