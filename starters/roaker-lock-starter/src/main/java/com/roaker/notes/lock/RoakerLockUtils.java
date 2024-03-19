package com.roaker.notes.lock;

import com.roaker.notes.commons.constants.ErrorCodeConstants;
import com.roaker.notes.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.locks.LockRegistry;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class RoakerLockUtils {
    private final LockRegistry lockRegistry;

    public <T> T runWithLock(String lockKey, Callable<T> callable) {
        Lock lock = null;
        try {
            lock = lockRegistry.obtain(lockKey);
        } catch (Exception e) {
            log.error("Unable to obtain lock[{}]", lockKey, e);
            throw new ServerException(ErrorCodeConstants.REQUIRE_LOCK_ERROR);
        }
        try {
            if (lock.tryLock()) {
                return callable.call();
            }
        } catch (Exception e) {
            log.error("Unable to obtain lock[{}]", lockKey, e);
        } finally {
            lock.unlock();
        }
        throw new ServerException(ErrorCodeConstants.REQUIRE_LOCK_ERROR);
    }

    public  void runWithLock(String lockKey, Runnable runnable) {
        Lock lock = null;
        try {
            lock = lockRegistry.obtain(lockKey);
        } catch (Exception e) {
            log.error("Unable to obtain lock[{}]", lockKey, e);
            throw new ServerException(ErrorCodeConstants.REQUIRE_LOCK_ERROR);
        }
        try {
            if (lock.tryLock()) {
                runnable.run();
            }
        } catch (Exception e) {
            log.error("Unable to obtain lock[{}]", lockKey, e);
        } finally {
            lock.unlock();
        }
        throw new ServerException(ErrorCodeConstants.REQUIRE_LOCK_ERROR);
    }
}
