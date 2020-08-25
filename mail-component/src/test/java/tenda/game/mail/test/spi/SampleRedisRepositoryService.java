package tenda.game.mail.test.spi;

import redis.clients.jedis.Jedis;
import tenda.game.mail.PlayerMail;
import tenda.game.mail.PlayerSystemMailReference;
import tenda.game.mail.SystemMail;
import tenda.game.mail.api.RepositoryService;
import tenda.game.mail.spi.RepositoryServiceProvider;

import java.io.*;
import java.util.Arrays;

public class SampleRedisRepositoryService implements RepositoryServiceProvider {
    @Override
    public RepositoryService provide() {
        return new RepositoryService() {

            @Override
            public void save(PlayerMail mail) {
                try (Jedis jedis = new Jedis("192.168.2.180",32772)) {
                    jedis.set("test".getBytes(), serialize(mail));
                }
            }

            @Override
            public void save(SystemMail mail) {

            }

            @Override
            public void save(PlayerSystemMailReference mail) {

            }

            @Override
            public <T> T load(long id) {
                try(Jedis jedis = new Jedis("192.168.2.180",32772)){

                    byte[] bytes = jedis.get("test".getBytes());
                    return (T) deserialize(bytes);
                }
            }

            byte[] NULL = "(nil)".getBytes();
            private byte[] serialize(Object object) {
                if (object == null)
                    return NULL;
                ObjectOutputStream objectOutputStream = null;
                try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();){
                    objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(object);
                    return byteArrayOutputStream.toByteArray();
                } catch (Exception e) {
                    throw new RuntimeException("实例化失败：" + object.getClass().getName() + "未实现java.io.Serializable或全局变量未实现", e);
                }
            }

            private Object deserialize(byte[] bytes) {
                if (bytes != null) {
                    if (Arrays.equals(NULL, bytes))
                        return null;
                    ByteArrayInputStream byteArrayOutputStream = null;
                    try {
                        byteArrayOutputStream = new ByteArrayInputStream(bytes);
                        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayOutputStream);
                        return objectInputStream.readObject();
                    } catch (Exception e) {
                        throw new RuntimeException("实例化失败", e);
                    }
                }
                return null;
            }

        };
    }
}
