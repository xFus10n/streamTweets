package com.xfus10n.twitter;

import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

import com.xfus10n.twitter.Utilz.CLI;
import com.xfus10n.twitter.Utilz.Utilz;
import com.xfus10n.twitter.propertiesReader.Reader;
import com.xfus10n.twitter.twitter.StatusListenerImpl;
import org.apache.commons.cli.CommandLine;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import twitter4j.*;
import twitter4j.Status;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

public class ProducerApp {
    private final static LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<>(1000);
    private static Producer<String, String> producer;

    public static void main(String[] args) throws Exception {

        final CLI cli = new CLI();
        CommandLine cmd = cli.CLIparser(args);
        Properties tweeterProps = Reader.readProperties(cmd.getOptionValue("t"));

        String topicName = "default";
        if (cmd.hasOption("d")) topicName = cmd.getOptionValue("d");
        if (cmd.hasOption("p")) {
            Properties kafkaProps = Reader.readProperties(cmd.getOptionValue("p"));
            producer = Utilz.getKafkaProducer(kafkaProps);
        }

        String proxy = "";
        if (cmd.hasOption("f"))  proxy = cmd.getOptionValue("f");

        String[] keyWords = {"spring"}; //can be multiple

        // Create twitterstream using the configuration
        TwitterStream twitterStream = Utilz.getTwitterStream(tweeterProps, proxy);
        StatusListener listener = new StatusListenerImpl(queue);
        twitterStream.addListener(listener);

        // Filter keywords
        FilterQuery query = new FilterQuery().track(keyWords);
        twitterStream.filter(query);


        int j = 0; //id
        try {
            while (true) {
                Status ret = queue.poll();

                if (ret == null) {
                    Thread.sleep(100);
                    // i++;
                } else {
                    for (HashtagEntity hashtage : ret.getHashtagEntities()) {
                        System.out.println("Tweet:" + ret);
                        System.out.println("Hashtag: " + hashtage.getText());
                        if (producer != null) producer.send(new ProducerRecord<>(topicName, Integer.toString(j++), ret.getText()));
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // producer.close();
        // Thread.sleep(500);
         twitterStream.shutdown();
    }
}
