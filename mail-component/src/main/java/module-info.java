import tenda.game.mail.spi.RepositoryServiceProvider;

module mail.component {
    requires slf4j.api;
    uses RepositoryServiceProvider;
    uses org.slf4j.Logger;
}