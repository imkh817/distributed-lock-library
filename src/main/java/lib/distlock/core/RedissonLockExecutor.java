package lib.distlock.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class RedissonLockExecutor implements DistributedLockExecutor{

    private final RedissonClient redissonClient;

    @Override
    public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit) {
        RLock rLock = redissonClient.getLock(key);
        try {
            return rLock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            log.error("인터럽트 발생", e);
            return false;
        }
    }

    @Override
    public void unlock(String key) {
        RLock rLock = redissonClient.getLock(key);
        try {
            if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        } catch (IllegalMonitorStateException e) {
            log.error("이미 락이 해제되었습니다.", e);
        }
    }
}
