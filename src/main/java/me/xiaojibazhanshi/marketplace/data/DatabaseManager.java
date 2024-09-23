package me.xiaojibazhanshi.marketplace.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import me.xiaojibazhanshi.marketplace.Marketplace;
import me.xiaojibazhanshi.marketplace.config.ConfigManager;
import me.xiaojibazhanshi.marketplace.objects.Listing;
import me.xiaojibazhanshi.marketplace.objects.Transaction;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseManager {

    private final Marketplace main;

    private MongoDatabase database;
    private MongoClient mongoClient;

    private final String connectionString;

    private MongoCollection<Document> playerTransactions;
    private MongoCollection<Document> listings;
    private MongoCollection<Document> blackMarketRefreshDate;

    private final ConcurrentHashMap<UUID, Set<Transaction>> playerTransactionDataCache = new ConcurrentHashMap<>();
    private final List<Listing> listingCache = new ArrayList<>();
    @Getter @Setter private long lastBlackMarketRefreshDate;

    public DatabaseManager(Marketplace main, ConfigManager configManager) {
        this.main = main;
        connectionString = configManager.getConnectionString();

        if (connectionString.equalsIgnoreCase("replace this")) {
            Bukkit.getLogger().warning("[Marketplace] Please replace the MongoDB connection string in the config.");
            Bukkit.broadcastMessage("[Marketplace] Please replace the MongoDB connection string in the config.");
            main.getPluginLoader().disablePlugin(main);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> connectToMongoDB(main));
    }

    private void connectToMongoDB(Marketplace main) {
        try {
            mongoClient = new MongoClient(new com.mongodb.MongoClientURI(connectionString));
            database = mongoClient.getDatabase("marketplace");

            initCollections();
            initializeCaches();
            Bukkit.getLogger().info("[Marketplace] Successfully connected to MongoDB.");
        } catch (MongoException e) {
            Bukkit.getLogger().severe("[Marketplace] Failed to connect to MongoDB: " + e.getMessage());
            main.getPluginLoader().disablePlugin(main);
        }
    }

    public void closeMongoClient() {
        if (mongoClient != null) {
            mongoClient.close();
            Bukkit.getLogger().info("[Marketplace] MongoDB connection closed.");
        }
    }

    private void initCollections() {
        playerTransactions = database.getCollection("playerTransactions");
        listings = database.getCollection("listings");
        blackMarketRefreshDate = database.getCollection("blackMarketRefreshDate");
    }

    private void initializeCaches() {
        for (Document doc : playerTransactions.find()) {
            UUID playerId = UUID.fromString(doc.getString("_id"));
            Set<Transaction> transactions = new HashSet<>();

            List<String> serializedTransactions = doc.getList("transactions", String.class);

            if (serializedTransactions == null || serializedTransactions.isEmpty()) {
                playerTransactionDataCache.put(playerId, transactions);
                continue;
            }

            for (String serializedTransaction : serializedTransactions) {
                Transaction transaction = Transaction.deserialize(serializedTransaction);
                transactions.add(transaction);
            }

            playerTransactionDataCache.put(playerId, transactions);
        }

        List<Document> listingDocs = listings.find().into(new ArrayList<>());
        for (Document doc : listingDocs) {
            String serializedListing = doc.getString("serializedData");

            if (serializedListing == null) {
                continue;
            }

            Listing listing = Listing.deserialize(serializedListing);
            listingCache.add(listing);
        }

        Document refreshDoc = blackMarketRefreshDate.find().first();
        if (refreshDoc != null) {
            lastBlackMarketRefreshDate = refreshDoc.getLong("refreshDate");
        } else {
            lastBlackMarketRefreshDate = System.currentTimeMillis();
            blackMarketRefreshDate.insertOne(new Document("_id", "black_market_refresh").append("refreshDate", lastBlackMarketRefreshDate));
        }
    }

    public void clearCaches() {
        listingCache.clear();
        playerTransactionDataCache.clear();
    }

    public void saveData() {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            saveListingsToDatabase();
            saveLastBlackMarketRefreshDate();
            savePlayerTransactionsToDatabase();
        });
    }

    public void addTransaction(UUID playerId, Transaction transaction) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            playerTransactionDataCache.computeIfAbsent(playerId, k -> new HashSet<>()).add(transaction);
        });
    }

    public void addListing(Listing listing) {
        listingCache.add(listing);
    }

    public void removeListing(Listing listing) {
        listingCache.remove(listing);
    }

    private void savePlayerTransactionsToDatabase() {
        for (UUID playerId : playerTransactionDataCache.keySet()) {
            Set<Transaction> transactions = playerTransactionDataCache.get(playerId);
            List<String> serializedTransactions = new ArrayList<>();

            for (Transaction transaction : transactions) {
                serializedTransactions.add(transaction.serialize());
            }

            Document playerDoc = new Document("_id", playerId.toString())
                    .append("transactions", serializedTransactions);

            playerTransactions.replaceOne(new Document("_id", playerId.toString()), playerDoc, new UpdateOptions().upsert(true));
        }
    }

    private void saveListingsToDatabase() {
        listings.deleteMany(new Document());

        for (Listing listing : listingCache) {
            String uniqueId = UUID.randomUUID().toString();

            Document listingDoc = new Document("_id", uniqueId)
                    .append("serializedData", listing.serialize());

            listings.replaceOne(new Document("_id", uniqueId), listingDoc, new UpdateOptions().upsert(true));
        }
    }

    private void saveLastBlackMarketRefreshDate() {
        Document refreshDoc = new Document("_id", "black_market_refresh")
                .append("refreshDate", lastBlackMarketRefreshDate);

        blackMarketRefreshDate.replaceOne(new Document("_id", "black_market_refresh"), refreshDoc, new UpdateOptions().upsert(true));
    }
}