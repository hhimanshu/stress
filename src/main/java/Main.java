import org.iq80.leveldb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

public class Main {
    private static final WriteOptions WRITE_OPTS = new WriteOptions().sync(false);
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws IOException, InterruptedException {
        Options options = new Options();
        options.createIfMissing(true);
        options.cacheSize(2 * 1048576);
        DB db = factory.open(new File("target/leveldb/stressdb"), options);
        try {
            startInsertsToLevelDb(db);
            for (; ; ) {
                Thread.sleep(10000);
            }
        } finally {
            // Make sure you close the db to shutdown the
            // database and avoid resource leaks.
            db.close();
        }
    }

    private static void startInsertsToLevelDb(final DB db) {
        class LevelDbInsertTask implements Runnable {
            DB db;

            public LevelDbInsertTask(DB db) {
                this.db = db;
            }

            @Override
            public void run() {
                for (; ; ) {
                    try {
                        db.get(UUID.randomUUID().toString().getBytes());

                        final String key = UUID.randomUUID().toString();
                        final String value = UUID.randomUUID().toString();
                        db.put(key.getBytes(), value.getBytes(), WRITE_OPTS);

                        db.get(key.getBytes());
                    } catch (DBException e) {
                        LOGGER.error("Failed: ", e);
                        throw(e);
                    }
                }
            }
        }


        for (int i = 1; i <= 100; i ++) {
            final Thread thread = new Thread(new LevelDbInsertTask(db));
            thread.setName("Thread-" + i);
            LOGGER.debug(thread.getName() + " initiated");
            thread.start();
        }
    }
}
