package tenda.game.mail.api;

import tenda.game.mail.PlayerMail;
import tenda.game.mail.PlayerSystemMailReference;
import tenda.game.mail.SystemMail;

public interface RepositoryService {
    void save(PlayerMail mail);
    void save(SystemMail mail);
    void save(PlayerSystemMailReference mail);
    <T> T load(long id);
}
