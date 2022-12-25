package com.john.webhook.webhook.application.port.out

/**
 * @author yoonho
 * @since 2022.12.25
 */
interface NotificationPort {
    fun notify(channel: String, message: String)
}