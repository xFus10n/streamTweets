package com.xfus10n.twitter.Utilz;

import com.xfus10n.twitter.domainz.TwitterProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import scala.util.parsing.combinator.testing.Str;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Utilz {
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static Producer<String, String> getKafkaProducer() {
        // Add Kafka producer config settings
        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092");
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return new KafkaProducer<>(props);
    }

    public static Producer<String, String> getKafkaProducer(Properties properties) {
        return new KafkaProducer<>(properties);
    }

    public static TwitterStream getTwitterStream(Properties properties, String proxy) throws URISyntaxException {

        String consumerKey = properties.getProperty(TwitterProperties.consumerKey.name());
        String consumerSecret = properties.getProperty(TwitterProperties.consumerSecret.name());
        String accessToken = properties.getProperty(TwitterProperties.accessToken.name());
        String accessTokenSecret = properties.getProperty(TwitterProperties.accessTokenSecret.name());

        // Set twitter oAuth tokens in the configuration
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret);

        if (!proxy.equals("")) {
            URI uri = new URI(proxy);
            String proxyHost = uri.getHost();
            int proxyPort = uri.getPort();
            cb.setHttpProxyHost(proxyHost).setHttpProxyPort(proxyPort);
        }

        // Create twitterstream using the configuration
        return new TwitterStreamFactory(cb.build()).getInstance();
    }
}
