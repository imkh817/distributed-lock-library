package lib.distlock.aspect;

import lib.distlock.annotation.DistributedLock;
import lib.distlock.core.DistributedLockExecutor;
import lib.distlock.utils.CustomSpringELParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final DistributedLockExecutor lockExecutor;

    @Around("@annotation(lock)")
    public Object lock(ProceedingJoinPoint proceedingJoinPoint, DistributedLock lock) throws Throwable {

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();

        String key = CustomSpringELParser.getDynamicValue(
                        lock.key(),
                        method,
                        proceedingJoinPoint.getArgs(),
                        proceedingJoinPoint.getTarget())
                .toString();

        boolean isLocked = lockExecutor.tryLock(key, lock.waitTime(), lock.leaseTime(), lock.timeUnit());

        if(!isLocked){
            log.warn("[락 획득 실패] 키: {}", key);
            return false;
        }
        try{
            return proceedingJoinPoint.proceed();
        }catch (InterruptedException e){
            throw e;
        }finally{
            lockExecutor.unlock(key);
            log.info("[락 해제] 키: {}", key);
        }
    }
}
