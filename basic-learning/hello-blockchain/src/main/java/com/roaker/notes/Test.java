package com.roaker.notes;

import java.io.ByteArrayInputStream;
import java.util.Properties;

/**
 * @author lei.rao
 * @since 1.0
 */
public class Test {
    public static void main(String[] args) throws Exception{
        String p="restricted.member.sync.batch.size = 100\n" +
                "es.socketTimeoutMillis = 30000\n" +
                "ES.bcpList.batchSize = 100";
        Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(p.getBytes()));
        properties.stringPropertyNames().forEach(System.out::println);
        System.out.println("Value as Follows");
        properties.stringPropertyNames().forEach(s -> System.out.println(properties.getProperty(s)));
    }
}
