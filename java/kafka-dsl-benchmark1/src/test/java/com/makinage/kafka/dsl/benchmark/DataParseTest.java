package com.makinage.kafka.dsl.benchmark;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

public class DataParseTest {


    static final Logger LOG = LogManager.getLogger(DataParseTest.class.getName());

    @BeforeClass
    public static void setUp() throws IOException {
    }

    @Test
    public void testDateISO8601Parsing() throws IOException, URISyntaxException, ParseException {

        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Instant instant = df1.parse("2020-07-12T00:00:00").toInstant();

        // Default parser can't be used, missing timezone (Z)
        //Instant instant = Instant.parse("2020-07-12T00:00:00Z");
    }
}
