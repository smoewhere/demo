package org.fan.local.utils.lock;

import java.sql.Time;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/2/7 10:19
 */
public class JedisLockManager {

  private final JedisPool jedisPool;


  private static final String LOCK_SCRIPT =
      "local lockClientId = redis.call('GET', KEYS[1])\n" +
          "if lockClientId == ARGV[1] then\n" +
          "  redis.call('PEXPIRE', KEYS[1], ARGV[2])\n" +
          "  return 1\n" +
          "elseif not lockClientId then\n" +
          "  redis.call('SET', KEYS[1], ARGV[1], 'PX', ARGV[2])\n" +
          "  return 1\n" +
          "end\n" +
          "return 0";

  private static final String UNLOCK_SCRIPT =
      "local lockClientId = redis.call('GET', KEYS[1]);\n"
          + "if lockClientId == ARGV[1] then\n"
          + "  redis.call('DEL',KEYS[1]);\n"
          + "  return 0;\n"
          + "elseif not lockClientId then\n"
          + "  return 2;\n"
          + "end\n"
          + "return 1;\n";

  private static ThreadLocal<String> CLIENT_ID_HOLDER = new ThreadLocal<>();

  public JedisLockManager(@Nonnull JedisPool jedisPool) {
    this.jedisPool = jedisPool;
  }

  public boolean lock(String key) {
    return false;
  }

  public boolean tryLock(String key, long timeOut) {
    long now = System.currentTimeMillis();
    long expire = now + TimeUnit.MILLISECONDS.convert(timeOut, TimeUnit.SECONDS);
    String clientId = UUID.randomUUID().toString();
    CLIENT_ID_HOLDER.set(clientId);
    try {
      boolean acquired;
      while (!(acquired = (obtainLock(key, clientId))) && expire >= System.currentTimeMillis()) {
        Thread.sleep(100);
      }
      if (!acquired) {
        System.out.println("获取锁失败！");
      }
      return acquired;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private boolean obtainLock(String key, String clientId) {
    try (Jedis jedis = jedisPool.getResource()) {
      Long result = (Long) jedis.eval(LOCK_SCRIPT, 1, key, clientId, "200000");
      return result.intValue() == 1;
    }
  }

  public void unlock(String key) {
    try (Jedis resource = jedisPool.getResource()) {
      String clientId = CLIENT_ID_HOLDER.get();
      Long result = (Long) resource.eval(UNLOCK_SCRIPT, 1, key, clientId);
      if (result == null) {
        throw new IllegalStateException("返回值为空，执行脚本错误");
      }
      switch (result.intValue()) {
        case 0:
          System.out.println("unlock lock_test success");
          break;
        case 1:
          throw new IllegalStateException("this client not hold this lock");
        case 2:
          throw new IllegalStateException("lock is expire by store");
        default:
          throw new IllegalStateException("未知错误");
      }
    } finally {
      CLIENT_ID_HOLDER.remove();
    }
  }

}
