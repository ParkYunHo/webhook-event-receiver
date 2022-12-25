package com.john.webhook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebhookEventReceiverApplicationTests {
    fun main(args: Array<String>) {
        runApplication<WebhookEventReceiverApplication>(*args)
    }
}
