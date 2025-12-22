package lib.distlock.core;

import java.util.concurrent.TimeUnit;

public interface DistributedLockExecutor {

    boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit);

    void unlock(String key);
}
