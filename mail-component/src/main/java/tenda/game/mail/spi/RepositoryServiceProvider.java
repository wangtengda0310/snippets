package tenda.game.mail.spi;

import tenda.game.mail.api.RepositoryService;

public interface RepositoryServiceProvider {
    RepositoryService provide();
}
