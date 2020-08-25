package tenda.game.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tenda.game.mail.api.RepositoryService;
import tenda.game.mail.spi.RepositoryServiceProvider;

import java.util.HashSet;
import java.util.ServiceLoader;

public interface MailService {
    class foobar {
        static HashSet<RepositoryService> load;
        static Logger logger;
        static {
            ServiceLoader.load(Logger.class)
                    .findFirst()
                    .ifPresentOrElse(findLogger->logger = findLogger
                            ,()->logger = LoggerFactory.getLogger(MailService.class));

            load = new HashSet<>();
            ServiceLoader.load(RepositoryServiceProvider.class).forEach(provider -> load.add(provider.provide()));
            logger.info("RepositoryServiceProvider: {}",load.toString());
        }
    }

    default void save(PlayerMail mail) {
        foobar.load.forEach(service->{
            service.save(mail);
        });}
    default void save(SystemMail mail) {
        foobar.load.forEach(service->{
            service.save(mail);
        });}

    default PlayerMail loadPlayerMail(long i) {
        PlayerMail[] find = new PlayerMail[1];
        foobar.load.forEach(service->{
            find[0] = service.load(i);
        });
        return find[0];
    }

    default SystemMail loadPlayerMail(long id, SystemMail reference) {
        return null;
    }

    default SystemMail loadSystemMail(long id) {
        return null;
    }
}
