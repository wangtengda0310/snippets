package tenda.game.mail.test.spi;

import org.junit.jupiter.api.Test;
import tenda.game.mail.MailService;
import tenda.game.mail.PlayerMail;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class MailComponentTest {
    @Test
    void test() {
        MailService mailService = new MailService() {};
        mailService.save(new PlayerMail());
        assertNotNull(mailService.loadPlayerMail(0));
    }
}
