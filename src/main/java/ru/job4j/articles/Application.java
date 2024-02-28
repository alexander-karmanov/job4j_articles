package ru.job4j.articles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.articles.service.SimpleArticleService;
import ru.job4j.articles.service.generator.RandomArticleGenerator;
import ru.job4j.articles.store.ArticleStore;
import ru.job4j.articles.store.WordStore;

import java.io.InputStream;
import java.util.Properties;

public class Application {

    private final Logger logger = LoggerFactory.getLogger(Application.class.getSimpleName());

    public final int targetCount = 1_000_000;

    public static void main(String[] args) {
        var properties = loadProperties();
        var wordStore = new WordStore(properties);
        var articleStore = new ArticleStore(properties);
        var articleGenerator = new RandomArticleGenerator();
        var articleService = new SimpleArticleService(articleGenerator);
        Application app = new Application();
        articleService.generate(wordStore, app.targetCount, articleStore);
    }

    private static Properties loadProperties() {
        Application app = new Application();
        app.logger.info("Загрузка настроек приложения");
        var properties = new Properties();
        try (InputStream in = Application.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(in);
        } catch (Exception e) {
            app.logger.error("Не удалось загрузить настройки. { }", e.getCause());
            throw new IllegalStateException();
        }
        return properties;
    }
}
