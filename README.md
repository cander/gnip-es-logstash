# Load tweets from Gnip into ElasticSearch

This is a little demo to show how to get Tweets via [Gnip Powertrack](https://gnip.com/) into [ElasticSearch](https://www.elastic.co/products/elasticsearch)
using [Logstash](https://www.elastic.co/products/logstash).  It uses some [sample
code](https://github.com/twitter/hbc/blob/master/hbc-example/src/main/java/com/twitter/hbc/example/EnterpriseStreamExample.java) from Twitter and the Twitter [Hosebird](https://github.com/twitter/hbc) Java library
to fetch the tweets.  There is a [Twitter plug-in](https://www.elastic.co/guide/en/logstash/current/plugins-inputs-twitter.html) for Logstash, but from what I could tell, it doesn't work with Gnip.

Originally, I was thinking that I could write a Logstash plug-in using Hosebird, but I quickly saw that would be a lot of work.  Instead, I opted to use the [pipe plug-in](https://www.elastic.co/guide/en/logstash/current/plugins-inputs-pipe.html) for Logstash.  This has a number of nice benefits:
* it's dead simple to implement - a project on GitHub is practically overkill.
* the plugin will not only start our external process that downloads the tweets, but it will also re-start it if it dies.
* separating Logstash from the downloading program makes things very simple to debug, and you can use the downloader by itself to fetch and save tweets.
* having a separate program eliminates any possible dependency collisions - the downloader has its dependecies, and Logstash has its own dependencies.

## Prerequisites
* Gnip - this is a paid service from Twitter.  You have to signup and get approved for it.
* Java - I used Java8u77
* Logstash - I used 2.3.1
* [Gradle](http://gradle.org/) - I used 2.12

## Build

* `gradle`

There is no step 2!  Gradle builds a script in `build/install/gnip-es-logstash`
that will run the downloader (`EnterpriseStreamExample`).  It takes arguments for: login/email, password, and app-name.  

## Running it
Assuming that you have some rules set up for your PowerTrack stream:
* Edit the `logstash.conf` file to put in your Gnip credentials.  If your
ElasticSearch is something other than `localhost`, you might have to edit
that section of `logstash.conf`.
* Run Logstash: `logstash -f logstash.conf`

One thing to note: the downloader fetches a fixed number of tweets and then exits.  Logstash will then restart it for you.  It would be a lot more efficient to replace that fixed loop with a forever loop.  I have a fixed loop for when I just want to grab a block of tweets and save them to a file.

