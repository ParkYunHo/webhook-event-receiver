package com.john.webhook.webhook.adapter.`in`.web

import com.john.webhook.common.constants.GithubConstants
import com.john.webhook.common.dto.BaseResponse
import com.john.webhook.common.exception.NotSupportedEventTypeException
import com.john.webhook.webhook.application.port.`in`.IssueCommentUseCase
import com.john.webhook.webhook.application.port.`in`.PullRequestReviewCommentUseCase
import com.john.webhook.webhook.application.port.`in`.PullRequestReviewUseCase
import com.john.webhook.webhook.application.port.`in`.PullRequestUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * @author yoonho
 * @since 2022.12.25
 */
@RestController
class WebhookController(
    private val issueCommentUsecase: IssueCommentUseCase,
    private val pullRequestUsecase: PullRequestUseCase,
    private val pullRequestReviewUsecase: PullRequestReviewUseCase,
    private val pullRequestReviewCommentUsecase: PullRequestReviewCommentUseCase
) {
    companion object {
        private const val GITHUB_EVENT_TYPE_HEADER = "X-GitHub-Event"
    }

    @PostMapping(path = ["/api/github/{channel}"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun github(
        @PathVariable channel: String, @RequestHeader(GITHUB_EVENT_TYPE_HEADER) eventType: String, @RequestBody payload: String
    ): BaseResponse {

        when(eventType) {
            GithubConstants.GithubEventType.GITHUB_EVENT_PULL_REQUEST.code -> pullRequestUsecase.processPullRequest(payload, channel)
            GithubConstants.GithubEventType.GITHUB_EVENT_PULL_REQUEST_REVIEW.code -> pullRequestReviewUsecase.processPullRequestReview(payload, channel)
            GithubConstants.GithubEventType.GITHUB_EVENT_PULL_REQUEST_REVIEW_COMMENT.code -> pullRequestReviewCommentUsecase.processPullRequestReviewComment(payload, channel)
            GithubConstants.GithubEventType.GITHUB_EVENT_ISSUE_COMMENT.code -> issueCommentUsecase.processIssueComment(payload, channel)
            else -> throw NotSupportedEventTypeException(eventType)
        }

        return BaseResponse().successNoContent()
    }
}