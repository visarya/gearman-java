package net.johnewart.gearman.server.config;

import net.johnewart.gearman.engine.queue.factories.JobQueueFactory;
import net.johnewart.gearman.engine.queue.factories.MemoryJobQueueFactory;
import net.johnewart.gearman.engine.queue.factories.PostgreSQLPersistedJobQueueFactory;
import net.johnewart.gearman.engine.queue.factories.RedisPersistedJobQueueFactory;
import net.johnewart.gearman.server.config.persistence.PostgreSQLConfiguration;
import net.johnewart.gearman.server.config.persistence.RedisConfiguration;

public class PersistenceEngineConfiguration {

    private static final String ENGINE_MEMORY = "memory";
    private static final String ENGINE_REDIS = "redis";
    private static final String ENGINE_POSTGRES = "postgres";

    private RedisConfiguration redis;
    private PostgreSQLConfiguration postgreSQL;
    private String engine;
    private JobQueueFactory jobQueueFactory;

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public RedisConfiguration getRedis() {
        return redis;
    }

    public void setRedis(RedisConfiguration redis) {
        this.redis = redis;
    }

    public PostgreSQLConfiguration getPostgreSQL() {
        return postgreSQL;
    }

    public void setPostgreSQL(PostgreSQLConfiguration postgreSQL) {
        this.postgreSQL = postgreSQL;
    }

    public JobQueueFactory getJobQueueFactory() {
        if(jobQueueFactory == null) {
            switch (getEngine()) {
                case ENGINE_MEMORY:
                    jobQueueFactory = new MemoryJobQueueFactory();
                    break;
                case ENGINE_POSTGRES:
                    jobQueueFactory = new PostgreSQLPersistedJobQueueFactory(
                            postgreSQL.getHost(),
                            postgreSQL.getPort(),
                            postgreSQL.getDbName(),
                            postgreSQL.getUser(),
                            postgreSQL.getPassword()
                    );
                    break;
                case ENGINE_REDIS:
                    jobQueueFactory = new RedisPersistedJobQueueFactory(
                            redis.getHost(),
                            redis.getPort()
                    );
                    break;
                default:
                    jobQueueFactory = null;
            }
        }

        return jobQueueFactory;
    }
}
