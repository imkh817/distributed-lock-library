package lib.distlock.config;

import lib.distlock.aspect.DistributedLockAspect;
import lib.distlock.core.DistributedLockExecutor;
import lib.distlock.core.RedissonLockExecutor;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class DistributedLockAutoConfiguration {

    @Bean
    public DistributedLockAspect distributedLockAspect(DistributedLockExecutor distributedLockExecutor){
        return new DistributedLockAspect(distributedLockExecutor);
    }

    @Bean
    @ConditionalOnMissingBean(DistributedLockExecutor.class)
    public DistributedLockExecutor distributedLockExecutor(RedissonClient redissonClient){
        return new RedissonLockExecutor(redissonClient);
    }
}
