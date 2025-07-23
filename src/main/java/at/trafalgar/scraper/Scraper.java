package at.trafalgar.scraper;

import at.trafalgar.Main;
import at.trafalgar.utils.HTTP;
import at.trafalgar.utils.Logger;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class Scraper {
    public static void run(String url) {
        final String prefix = "[" + url + "] ";

        at.trafalgar.utils.Thread.runAsync(() -> {
            if (!url.startsWith("https://") && !url.startsWith("http://")) {
                Logger.log(prefix + "URL muss https:// oder http:// enthalten!", true);
                return;
            }

            if (!HTTP.isWellFormed(url)) {
                Logger.log(prefix + "[" + url + "] URL ist ungültig!", true);
                return;
            }

            if (!HTTP.isReachable(url)) {
                Logger.log(prefix + "[" + url + "] URL ist nicht erreichbar!", true);
                return;
            }

            Logger.log(prefix + "Starte Scraping...");

            java.util.logging.Logger.getLogger("org.openqa.selenium.remote").setLevel(Level.OFF);
            java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
            System.setProperty("webdriver.chrome.silentOutput", "true");

            WebDriverManager.chromedriver().setup();

            ChromeDriverService service = new ChromeDriverService.Builder()
                    .withSilent(true)
                    .build();

            ChromeOptions options = new ChromeOptions()
                    .addArguments("--headless", "--log-level=3")
                    .setExperimentalOption("excludeSwitches", Collections.singletonList("enable-logging"));

            WebDriver driver = new ChromeDriver(service, options);

            try (PrintWriter pw = new PrintWriter(new FileWriter("output.csv", false), true)) {
                pw.println("Pattern,Text,Href");
                driver.get(url);
                Logger.log(prefix + "Seite geladen");

                Thread.sleep(3000);
                Document docPage = Jsoup.parse(driver.getPageSource());

                for (String pat : Main.patternField.getText().split(",")) {
                    pat = pat.trim();
                    if (pat.isEmpty()) continue;
                    Logger.log(prefix + "Scanne nach Präfix: " + pat);

                    List<Element> elements = docPage.select("a[href^=" + pat + "]");
                    Logger.log(prefix + "  Gefunden: " + elements.size() + " Links");

                    for (Element element : elements) {
                        String text = element.text().replaceAll("\\s+", " ").trim();
                        String href = element.attr("href");
                        pw.printf("\"%s\",\"%s\",\"%s\"%n", pat, text, href);
                        Logger.log("   • " + text + " → " + href);
                    }
                }

                Logger.log(prefix + "Scraping abgeschlossen! Datei: output.csv");
            } catch (Exception e) {
                Logger.log(prefix + "Fehler: " + e.getMessage());
            } finally {
                driver.quit();
            }
        });
    }

    public static void runMultiple(List<String> urls) {
        for (String url : urls) {
            run(url);
        }
    }
}
