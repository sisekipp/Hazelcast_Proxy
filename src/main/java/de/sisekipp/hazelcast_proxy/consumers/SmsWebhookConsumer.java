package de.sisekipp.hazelcast_proxy.consumers;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.hazelcast.core.*;
import com.hazelcast.transaction.HazelcastXAResource;
import com.hazelcast.transaction.TransactionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.transaction.*;
import javax.transaction.xa.XAResource;

@Component
@Profile("consumer")
public class SmsWebhookConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsWebhookConsumer.class);

    private HazelcastXAResource xaResource;

    public SmsWebhookConsumer(HazelcastInstance instance) {
        this.xaResource = instance.getXAResource();
    }

    @Scheduled(fixedRate = 1000L)
    public void read() throws SystemException, NotSupportedException, RollbackException, HeuristicRollbackException, HeuristicMixedException {
        UserTransactionManager tm = new UserTransactionManager();
        tm.begin();

        Transaction transaction = tm.getTransaction();
        transaction.enlistResource(xaResource);
        TransactionContext context = xaResource.getTransactionContext();
        TransactionalQueue<MultiValueMap<String,String>> queue = context.getQueue("webhook-queue");
        MultiValueMap<String,String> message = queue.poll();
        if(message != null) {
            LOGGER.info("Get Message: {}", message);
        }
        transaction.delistResource(xaResource, XAResource.TMSUCCESS);

        tm.commit();



    }
}
