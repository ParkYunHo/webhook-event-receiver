package com.john.webhook.webhook.application.port.`in`

/**
 * @author yoonho
 * @since 2022.12.25
 */
interface PullRequestReviewUseCase {
    fun processPullRequestReview(payload: String, channel: String)
}