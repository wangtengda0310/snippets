package tenda.game.mail.test.spi;

import tenda.game.mail.PlayerMail;
import tenda.game.mail.PlayerSystemMailReference;
import tenda.game.mail.SystemMail;
import tenda.game.mail.api.RepositoryService;
import tenda.game.mail.spi.RepositoryServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class SampleInMemoryRepositoryService implements RepositoryServiceProvider {
    @Override
    public RepositoryService provide() {
        return new RepositoryService() {
            Map<Long, PlayerMail> playerMailMap = new HashMap<>();
            Map<Long, SystemMail> systemMailMap = new HashMap<>();
            Map<Long, PlayerSystemMailReference> playerSystemMailMap = new HashMap<>();

            @Override
            public void save(PlayerMail mail) {
                playerMailMap.put(mail.getId(), mail);
            }

            @Override
            public void save(SystemMail mail) {
                systemMailMap.put(mail.getId(), mail);
            }

            @Override
            public void save(PlayerSystemMailReference mail) {
                playerSystemMailMap.put(mail.getPlayerId(), mail);
            }

            @Override
            public <T> T load(long id) {
                if (playerMailMap.containsKey(id)) return (T)playerMailMap.get(id);
                if (systemMailMap.containsKey(id)) return (T)systemMailMap.get(id);
                if (playerSystemMailMap.containsKey(id)) return (T)playerSystemMailMap.get(id);
                return null;
            }
        };
    }
}
