package com.learning.vertx.stock;

import io.vertx.core.json.JsonArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class InMemoryData {
  private static final Logger LOG = LoggerFactory.getLogger(InMemoryData.class);


  public static List<String> ASSETS = Arrays.asList("TCS", "WIPRO", "INFOSYS", "JIO", "TATA");

  public static Map<String, Quote> cachedQuotes;

  public static Map<UUID, WatchList> watchListMap = new HashMap<>();

  public static Quote getQuoteForAsset(String assetName) {
    if (cachedQuotes == null) {
      cachedQuotes = new HashMap<>();
      ASSETS.forEach(asset -> cachedQuotes.put(asset, initRandomQuote(asset)));
      LOG.debug(" Quote Map {} ", cachedQuotes.size());
    }
    return cachedQuotes.get(assetName);
  }

  public static WatchList getWatchList(String accountId) {
    if (accountId == null) return null;
    return watchListMap.get(UUID.fromString(accountId));
  }

  public static boolean putWatchList(String uuid, WatchList watchList) {
    if (uuid == null || watchList == null) return false;
    watchListMap.put(UUID.fromString(uuid), watchList);
    return true;
  }

  public static boolean deleteWatchList(String uuid) {
    if (uuid == null) return false;

    Optional<WatchList> mayBeWatchList = Optional.ofNullable(watchListMap.remove(UUID.fromString(uuid)));
    return mayBeWatchList.isPresent();
  }

  private static Quote initRandomQuote(String name) {
    return Quote.builder()
      .asset(new Asset(name))
      .ask(getRandomValue())
      .bid(getRandomValue())
      .lastprice(getRandomValue())
      .volume(getRandomValue())
      .build();
  }

  private static BigDecimal getRandomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 1000));
  }

  public static List<Asset> getAssetsAsList() {
    List<Asset> list = new ArrayList<>();
    ASSETS.stream().map(Asset::new).forEach(list::add);
    return list;
  }
  public static JsonArray getAssetList() {
    JsonArray response = new JsonArray();
    ASSETS.stream().map(Asset::new).forEach(response::add);
    return response;
  }

}

