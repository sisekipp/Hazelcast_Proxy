package de.sisekipp.hazelcast_proxy.controller;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/sms")
public class SmsProxyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsProxyController.class);

    private IQueue<MultiValueMap<String, String>> queue;

    public SmsProxyController(HazelcastInstance instance) {
        this.queue = instance.getQueue("webhook-queue");
        this.queue.addItemListener(new ItemListener<MultiValueMap<String, String>>() {
            @Override
            public void itemAdded(ItemEvent<MultiValueMap<String, String>> itemEvent) {
                LOGGER.info("Queue item added, {}", itemEvent);
            }

            @Override
            public void itemRemoved(ItemEvent<MultiValueMap<String, String>> itemEvent) {
                LOGGER.info("Queue item removed, {}", itemEvent);
            }
        },true);
    }

    @PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String smsWebhook(@RequestBody MultiValueMap<String, String> body) {
        queue.add(body);
        return "";
    }

}
