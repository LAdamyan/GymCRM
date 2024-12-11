package org.gymCrm.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
class FileUtilTest {
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("test", ".json");
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    void testReadData() throws IOException {
        TestData testData = new TestData();
        testData.setName("Test Name");
        testData.setDateTime(LocalDateTime.of(2024, 5, 23, 12, 0));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(objectMapper.writeValueAsString(testData));
        }

        TestData readData = FileUtil.readData(tempFile.getAbsolutePath(), TestData.class);

        assertNotNull(readData);
        assertEquals("Test Name", readData.getName());
        assertEquals(LocalDateTime.of(2024, 5, 23, 12, 0), readData.getDateTime());
    }

    @Setter
    @Getter
    static class TestData {
        private String name;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime dateTime;

    }

}