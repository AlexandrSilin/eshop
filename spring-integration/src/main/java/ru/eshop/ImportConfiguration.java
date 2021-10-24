package ru.eshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.ConsumerEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.dsl.JpaUpdatingOutboundEndpointSpec;
import org.springframework.integration.jpa.support.PersistMode;
import org.springframework.messaging.MessageHandler;
import ru.eshop.database.persist.BrandRepository;
import ru.eshop.database.persist.CategoryRepository;
import ru.eshop.database.persist.model.Brand;
import ru.eshop.database.persist.model.Category;
import ru.eshop.database.persist.model.Picture;
import ru.eshop.database.persist.model.Product;
import ru.eshop.pictures.service.PictureService;

import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ImportConfiguration {
    private final static Logger logger = LoggerFactory.getLogger(ImportConfiguration.class);
    private final EntityManagerFactory entityManagerFactory;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final PictureService pictureService;
    @Value("${source.directory.path}")
    private String sourceDirPath;
    @Value("${dest.directory.path}")
    private String destDirPath;

    @Autowired
    public ImportConfiguration(EntityManagerFactory entityManagerFactory,
                               CategoryRepository categoryRepository,
                               BrandRepository brandRepository,
                               PictureService pictureService) {
        this.entityManagerFactory = entityManagerFactory;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.pictureService = pictureService;
    }

    @Bean
    public MessageSource<File> sourceDirectory() {
        FileReadingMessageSource messageSource = new FileReadingMessageSource();
        messageSource.setDirectory(new File(sourceDirPath));
        messageSource.setAutoCreateDirectory(true);
        return messageSource;
    }

    @Bean
    public JpaUpdatingOutboundEndpointSpec jpaPersistHandler() {
        return Jpa.outboundAdapter(this.entityManagerFactory)
                .entityClass(Product.class)
                .persistMode(PersistMode.PERSIST);
    }

    @Bean
    public MessageHandler destDirectory() {
        FileWritingMessageHandler messageHandler = new FileWritingMessageHandler(new File(destDirPath));
        messageHandler.setExpectReply(false);
        messageHandler.setDeleteSourceFiles(true);
        messageHandler.setAutoCreateDirectory(true);
        return messageHandler;
    }

    @Bean
    public IntegrationFlow fileMoveFlow() {
        return IntegrationFlows.from(sourceDirectory(), conf -> conf.poller(Pollers.fixedDelay(2000)))
                .filter(msg -> ((File) msg).getName().endsWith(".csv"))
                .transform(new FileToStringTransformer())
                .split(s -> s.delimiters("\n"))
                .transform(this::getProductFromData)
                .handle(jpaPersistHandler(), ConsumerEndpointSpec::transactional).get();
    }

    private Product getProductFromData(String data) {
        logger.info("data = {}", data);
        String[] newData = data.split(",");
        List<Category> categories = categoryRepository.findByName(newData[3]);
        if (categories.isEmpty()) {
            throw new RuntimeException("Category is null");
        }
        List<Brand> brands = brandRepository.findByName(newData[4]);
        if (brands.isEmpty()) {
            throw new RuntimeException("Brand is null");
        }
        Product product = new Product(null, newData[0], new BigDecimal(newData[1]), newData[2],
                categories.get(0), brands.get(0));
        if (newData.length > 5 && !newData[5].isBlank()) {
            try {
                Path pic = Path.of(newData[5]);
                byte[] picData = Files.readAllBytes(pic);
                String filename = pictureService.createPicture(picData);
                Picture picture = new Picture(null, newData[5], "image/svg+xml", filename, product);
                List<Picture> pictures = new ArrayList<>();
                pictures.add(picture);
                product.setPicture(pictures);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return product;
    }
}