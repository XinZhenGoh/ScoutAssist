package com.aws.codestar.teamprime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/** Simple class to start up the application.
 *
 * @SpringBootApplication adds:
 *  @Configuration
 *  @EnableAutoConfiguration
 *  @ComponentScan
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    private static String scrapoxyHost = "13.59.132.196";
    private static String scrapoxyPort = "8888";

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {

        System.setProperty("http.proxyHost", scrapoxyHost);
        System.setProperty("http.proxyPort", scrapoxyPort);
        System.setProperty("https.proxyHost", scrapoxyHost);
        System.setProperty("https.proxyPort", scrapoxyPort);
        System.setProperty("http.proxySet", "true");
        System.setProperty("https.proxySet", "true");
        SpringApplication.run(Application.class, args);

        System.out.println("______________________   _____      _____    __________ __________ .___    _____   ___________   ");
        System.out.println("\\__    ___/\\_   _____/  /  _  \\    /     \\   \\______   \\\\______   \\|   |  /     \\  \\_   _____/          ");
        System.out.println("  |    |    |    __)_  /  /_\\  \\  /  \\ /  \\   |     ___/ |       _/|   | /  \\ /  \\  |    __)_              ");
        System.out.println("  |    |    |        \\/    |    \\/    Y    \\  |    |     |    |   \\|   |/    Y    \\ |        \\                    ");
        System.out.println("  |____|   /_______  /\\____|__  /\\____|__  /  |____|     |____|_  /|___|\\____|__  //_______  /                   ");
        System.out.println("                   \\/         \\/         \\/                     \\/              \\/         \\/                    ");
        System.out.println("  _________                        __       _____                   .__           __       _____  __________ .___ ");
        System.out.println(" /   _____/  ____   ____   __ __ _/  |_    /  _  \\    ______  ______|__|  _______/  |_    /  _  \\ \\______   \\|   |");
        System.out.println(" \\_____  \\ _/ ___\\ /  _ \\ |  |  \\\\   __\\  /  /_\\  \\  /  ___/ /  ___/|  | /  ___/\\   __\\  /  /_\\  \\ |     ___/|   |");
        System.out.println(" /        \\\\  \\___(  <_> )|  |  / |  |   /    |    \\ \\___ \\  \\___ \\ |  | \\___ \\  |  |   /    |    \\|    |    |   |");
        System.out.println("/_______  / \\___  >\\____/ |____/  |__|   \\____|__  //____  >/____  >|__|/____  > |__|   \\____|__  /|____|    |___|");
        System.out.println("        \\/      \\/                               \\/      \\/      \\/          \\/                 \\/                ");

    }

}
