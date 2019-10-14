package com.forezp;

import com.forezp.limiter.HostAddrKeyResolver;
import com.forezp.limiter.UriKeyResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ScFGatewayLimiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScFGatewayLimiterApplication.class, args);
    }

    /**
     * 在高并发的系统中，往往需要在系统中做限流，一方面是为了防止大量的请求使服务器过载，导致服务不可用，另一方面是为了防止网络攻击。
     * <p>
     * 常见的限流方式，比如Hystrix适用线程池隔离，超过线程池的负载，走熔断的逻辑。
     * 在一般应用服务器中，比如tomcat容器也是通过限制它的线程数来控制并发的；也有通过时间窗口的平均速度来控制流量。
     * 常见的限流纬度有比如通过Ip来限流、通过uri来限流、通过用户访问频次来限流。
     * <p>
     * 一般限流都是在网关这一层做，比如Nginx、Openresty、kong、zuul、Spring Cloud Gateway等；也可以在应用层通过Aop这种方式去做限流。
     *
     * RequestRateLimiterGatewayFilterFactory+lua脚本(request_rate_limiter.lua)实现了令牌桶的方式。
     */


    /**
     * 根据Hostname进行限流
     * http://localhost:8081
     * 1) "request_rate_limiter.{0:0:0:0:0:0:0:1}.tokens"
     * 2) "request_rate_limiter.{0:0:0:0:0:0:0:1}.timestamp"
     *
     * @return
     */
    @Bean
    public HostAddrKeyResolver hostAddrKeyResolver() {
        return new HostAddrKeyResolver();
    }

    /**
     * 根据uri去限流
     * http://localhost:8081/name
     * 1) "request_rate_limiter.{/name}.timestamp"
     * 2) "request_rate_limiter.{/name}.tokens"
     *
     * @return
     */
    @Bean
    public UriKeyResolver uriKeyResolver() {
        return new UriKeyResolver();
    }

    /**
     * 用户的维度去限流
     * http://localhost:8081?user=zhangsan
     * 1) "request_rate_limiter.{zhangsan}.tokens"
     * 2) "request_rate_limiter.{zhangsan}.timestamp"
     *
     * @return
     */
    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
    }
}
