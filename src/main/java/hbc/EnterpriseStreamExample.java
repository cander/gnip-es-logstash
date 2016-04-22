/**
 * Read from Gnip write to standard out.
 *
 * This code derived from example code from Twitter, Inc.  The original is at:
 * https://github.com/twitter/hbc/blob/master/hbc-example/src/main/java/com/twitter/hbc/example/EnterpriseStreamExample.java
 **/

// package com.twitter.hbc.example;
package hbc;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.RealTimeEnterpriseStreamingEndpoint;
import com.twitter.hbc.core.processor.LineStringProcessor;
import com.twitter.hbc.httpclient.auth.BasicAuth;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EnterpriseStreamExample {

  public static void run(String username,
                         String password,
                         String account,
                         String label,
                         String product) throws InterruptedException {
    BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);

    BasicAuth auth = new BasicAuth(username, password);

    RealTimeEnterpriseStreamingEndpoint endpoint = new RealTimeEnterpriseStreamingEndpoint(account, product, label);

    // Create a new BasicClient. By default gzip is enabled.
    Client client = new ClientBuilder()
            .name("PowerTrackClient-01")
            .hosts(Constants.ENTERPRISE_STREAM_HOST)
            .endpoint(endpoint)
            .authentication(auth)
            .processor(new LineStringProcessor(queue))
            .build();

    // Establish a connection
    client.connect();

    // Do whatever needs to be done with messages
    for (int msgRead = 0; msgRead < 200; msgRead++) {
      String msg = queue.take();
      System.out.println(msg);
    }

    System.err.println("all done - quitting");
    client.stop();
  }

  public static void main(String[] args) {
    try {
      EnterpriseStreamExample.run(args[0], args[1], args[2], "prod", "track");
    } catch (InterruptedException e) {
      System.err.println(e);
    }
  }
}
