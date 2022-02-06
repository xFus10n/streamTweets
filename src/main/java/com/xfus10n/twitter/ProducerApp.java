package com.xfus10n.twitter;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import twitter4j.*;
import twitter4j.Status;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

public class ProducerApp {
    public static void main(String[] args) throws Exception {
        final LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<>(1000);

//        if (!Utilz.argsAreGood(args)) return;

        String topicName = "tweets";//args[4];
        //String[] arguments = args.clone();
        //String[] keyWords = Arrays.copyOfRange(arguments, 5, arguments.length);
        String[] keyWords = {"winter"};

        // Create twitterstream using the configuration
        TwitterStream twitterStream = Utilz.getTwitterStream();
        StatusListener listener = new StatusListenerImpl(queue);
        twitterStream.addListener(listener);

        // Filter keywords
        FilterQuery query = new FilterQuery().track(keyWords);
        twitterStream.filter(query);

        final Producer<String, String> producer = Utilz.getKafkaProducer();
        int j = 0;

        // poll for new tweets in the queue. If new tweets are added, send them
        // to the topic
        while (true) {
            Status ret = queue.poll();

            if (ret == null) {
                Thread.sleep(100);
                // i++;
            } else {
                for (HashtagEntity hashtage : ret.getHashtagEntities()) {
//                    System.out.println("Tweet:" + ret);
                    System.out.println("Hashtag: " + hashtage.getText());
                    producer.send(new ProducerRecord<>(topicName, Integer.toString(j++), ret.getText()));
                }
            }
        }
        // producer.close();
        // Thread.sleep(500);
        // twitterStream.shutdown();
    }
}
