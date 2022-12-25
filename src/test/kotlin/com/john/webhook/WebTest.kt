package com.john.webhook

import com.john.webhook.webhook.adapter.out.web.NotificationAdapter
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [WebhookEventReceiverApplicationTests::class])
class WebTest {

    @Autowired
    private lateinit var notificationAdapter: NotificationAdapter

    @Test
    fun TEST() {
        notificationAdapter.notify("slack", "TEST")
    }

}