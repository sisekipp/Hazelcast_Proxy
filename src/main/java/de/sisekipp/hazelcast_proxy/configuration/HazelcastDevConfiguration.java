package de.sisekipp.hazelcast_proxy.configuration;

import com.atomikos.icatch.config.UserTransactionService;
import com.atomikos.icatch.config.UserTransactionServiceImp;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.hazelcast.config.Config;
import com.hazelcast.config.QueueConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jta.atomikos.AtomikosProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.transaction.UserTransaction;

@Configuration()
@Profile("dev")
public class HazelcastDevConfiguration {

    @Value("${hazelcastproxy.jta.logname}")
    private String logFileName;

    @Bean
    public Config hazelCastConfig() {
        Config config = new Config();
        config.setInstanceName("hazelcast-instance")
                .addQueueConfig(new QueueConfig().setName("webhook-queue").setMaxSize(0));
        return config;
    }

    @Bean(initMethod = "init", destroyMethod = "shutdownForce")
    @ConditionalOnMissingBean(UserTransactionService.class)
    public UserTransactionService userTransactionService() throws Throwable {
        AtomikosProperties properties = new AtomikosProperties();
        properties.setLogBaseName(logFileName);


        return new UserTransactionServiceImp(properties.asProperties());
    }
}
