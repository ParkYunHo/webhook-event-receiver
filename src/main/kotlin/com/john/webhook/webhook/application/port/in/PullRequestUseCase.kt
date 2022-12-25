package com.john.webhook.webhook.application.port.`in`

/**
 * @author yoonho
 * @since 2022.12.25
 */
interface PullRequestUseCase {
    fun processPullRequest(payload: String, channel: String)
}