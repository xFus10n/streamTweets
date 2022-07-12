package com.xfus10n.twitter;

import java.net.URL;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

import com.xfus10n.twitter.Utilz.CLI;
import com.xfus10n.twitter.Utilz.Utilz;
import com.xfus10n.twitter.propertiesReader.Reader;
import com.xfus10n.twitter.twitter.StatusListenerImpl;
import org.apache.commons.cli.CommandLine;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import twitter4j.*;
import twitter4j.Status;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

public final class ProducerApp {
    private static final LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<>(1000);
    private static Producer<String, String> producer;

    public static void main(String[] args) throws Exception {

        final CLI cli = new CLI();
        CommandLine cmd = cli.parser(args);

        /* logging */
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL log4j = loader.getResource("log4j.properties");
        PropertyConfigurator.configure(log4j);
        Logger logger = Logger.getLogger("log4j.rootLogger");

        Properties tweeterProps = Reader.readProperties(cmd.getOptionValue("t"));

        String topicName = "default";
        if (cmd.hasOption("d")) topicName = cmd.getOptionValue("d");
        if (cmd.hasOption("p")) {
            Properties kafkaProps = Reader.readProperties(cmd.getOptionValue("p"));
            producer = Utilz.getKafkaProducer(kafkaProps);
        }

        String proxy = "";
        if (cmd.hasOption("f")) proxy = cmd.getOptionValue("f");

        String[] keyWords = {"#"};
        if (cmd.hasOption("k")) keyWords = cmd.getOptionValue("k").replaceAll(" ", "").split(",");

        // Create twitter stream using the configuration
        TwitterStream twitterStream = Utilz.getTwitterStream(tweeterProps, proxy);
        StatusListener listener = new StatusListenerImpl(queue);
        twitterStream.addListener(listener);

        // Filter keywords
        FilterQuery query = new FilterQuery().track(keyWords);
        twitterStream.filter(query);

        /* shutdown */
        Thread shutdownHook = new Thread(()-> shutDown(logger));
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        while (true) {
            if (!queue.isEmpty()) {
                Status ret = queue.poll();
                for (HashtagEntity hashtage : ret.getHashtagEntities()) {
                    logger.info("Hashtag: " + hashtage.getText());
                    logger.info("Tweet:" + ret.getText());
                    if (producer != null) producer.send(new ProducerRecord<>(topicName, hashtage.getText(), ret.getText())); //think of key or partition
                }
            }
        }
    }

    private static void shutDown(Logger logger){
        logger.info("System shutdown");
        if (producer != null) producer.close();
    }
}
