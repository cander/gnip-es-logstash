# gnip-es-logstash
This is a little demo to show how to get Tweets via Gnip into ElasticSearch
using Logstash.  It uses some [sample
code](https://github.com/twitter/hbc/blob/master/hbc-example/src/main/java/com/twitter/hbc/example/EnterpriseStreamExample.java)
to fetch the tweets.

## Prerequisites
* Gnip - this is a paid service from Twitter.  You have to signup and get approved for it.
* Java - I used Java8u77
* Gradle - I used 2.12
* Logstash - I used 2.3.1

## Build

* `gradle`

There is no step 2!  It builds a script in `build/install/gnip-es-logstash`
that will run the scraper.  It takes arguments for: login/email, password,
and app-name.

## Running it
* Edit the `logstash.conf` file to put in your Gnip credentials.  If your
ElasticSearch is something other than `localhost`, you might have to edit
that section of `logstash.conf`.
* Run logstash: `logstash -f logstash.conf`

