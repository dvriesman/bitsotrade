# BITSOTRADE

BITSOTRADE is a experiment of using JavaFX with Spring Boot to create an application that simulate a contrarian trading strategy using as backend the services
provides by Bitso cryptocurrency exchange. 

## Frameworks/Libraries
- JavaFX for UI 
- Spring Boot for Websocket and dependecy injection
- Retrofit for simple REST client
- JUnit+Spring boot for some tests
- json lib + jacson to handle json

## How to Compile (and execute unit tests)
Gradle (https://gradle.org/) is the tool used in this project. 
There's a gradle wrapper in this project that can be used to compile, so, just clone the project and run the following commands:

```
# cd bitsotrade
# ./gradlew build
```

## Creating a JavaFX Jar application

```
# ./gradlew jfxJar
```
The executable jar will be available in ./build/jfx/app/project-jfx.jar


## Run Application
If you prefer just execute the application, run as bellow:

```
# ./gradlew jfxRun
```

## Checklist

| Feature | File name | Method name |
| --- | --- | --- |
| Schedule the polling of trades over REST | TradingService.java  | updateTrade  |
| Request a book snapshot over REST  | OrderBookService.java | init  |
| Listen for diff-orders over websocket | OrderBookWebSocketHandler.java | handleTextMessage |
| Replay diff-orders | OrderBookService.java | updateOrderBook |
| Use config option X to request recent trades | TradingService.java | updateTrade |
| Use config option X to limit number of ASKs displayed in UI | OrderBookService.java | getLimit |
| The loop that causes the trading algorithm to reevaluate | TradingStrategy.java  | countTickets |