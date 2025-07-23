package ink.charter.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author charter
 * @create 2025/07/16
 */
@SpringBootApplication
@EnableScheduling
public class CharterApplication {

  public static void main(String[] args) {
    SpringApplication.run(CharterApplication.class, args);
  }
}
