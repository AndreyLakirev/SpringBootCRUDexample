package com.lakirev.mentor_exercise1.customer;

import com.lakirev.mentor_exercise1.MentorExercise1Application;
import com.lakirev.mentor_exercise1.customer.model.Customer;
import com.lakirev.mentor_exercise1.json.service.JsonParser;
import com.lakirev.mentor_exercise1.json.service.JsonStreamReader;
import com.lakirev.mentor_exercise1.util.RandomObjectGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MentorExercise1Application.class)
public class CustomerIntegrationTest {
    @Value("${integration-tests.enabled}")
    private boolean testsEnabled;

    @Value("${test.customer.count}")
    private int customerCount;

    private static final String FILE_NAME = "largeJson.txt";

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerIntegrationTest.class);

    @Autowired
    private JsonStreamReader jsonStreamReader;

    @Autowired
    private JsonParser parser;

    @Autowired
    private RandomObjectGenerator objectGenerator;

    @BeforeEach
    private void skipTestOnCondition() {
        assumeTrue(testsEnabled);
    }

    @Test
    void testReadingFromLargeFile() {
        LOGGER.info("Started testing reading from large JSON file");
        LOGGER.info("Writing large test json file: " + customerCount + " customers...");
        writeLargeJsonFile();
        try (InputStream inputStream = new FileInputStream(FILE_NAME)) {
            jsonStreamReader.consumeJsonStream(inputStream, 1024, (s) -> {});
        } catch (Exception e) {
            new File(FILE_NAME).delete();
            fail(e);
        }
        new File(FILE_NAME).delete();
        LOGGER.info("Test was successfully completed!");
    }

    private void writeLargeJsonFile() {
        try (OutputStream outputStream = new FileOutputStream(FILE_NAME)) {
            int step = customerCount / 1000;
            for (int i = 0; i < step; i++) {
                List<Customer> customers = objectGenerator.generateRandomObjectsAsync(Customer.class, 1000, 4).get();
                outputStream.write(parser.toJson(customers).getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            new File(FILE_NAME).delete();
            fail(e);
        }
    }
}
