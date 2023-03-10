package com.john.webhook.webhook.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.john.webhook.common.constants.GithubConstants
import com.john.webhook.common.exception.BadRequestException
import com.john.webhook.common.exception.InternalServerErrorException
import com.john.webhook.common.exception.NotSupportedActionTypeException
import com.john.webhook.common.utils.GithubUtils
import com.john.webhook.webhook.application.port.`in`.IssueCommentUseCase
import com.john.webhook.webhook.application.port.`in`.PullRequestReviewCommentUseCase
import com.john.webhook.webhook.application.port.`in`.PullRequestReviewUseCase
import com.john.webhook.webhook.application.port.`in`.PullRequestUseCase
import com.john.webhook.webhook.application.port.out.NotificationPort
import org.springframework.stereotype.Service

/**
 * @author yoonho
 * @since 2022.12.25
 */
@Service
class WebhookService(
    private val objectMapper: ObjectMapper,
    private val notificationPort: NotificationPort
): PullRequestUseCase, PullRequestReviewUseCase, PullRequestReviewCommentUseCase, IssueCommentUseCase {

    override fun processPullRequest(payload: String, channel: String) {
        try{
            val info = GithubUtils.parse(payload)

            if(!info.baseBranch.equals("master")) {
                return
            }

            when(info.actionType) {
                GithubConstants.GithubActionType.PULL_REQUEST_ACTION_TYPE_OPENED.code -> {
                    val message = GithubConstants.GithubMessageTemplate.PULL_REQUEST_DEFAULT_TEMPLATE.code.formatted(
                        GithubConstants.GithubMessageType.CREATED_MESSAGE.code, info.repoName, info.assignee, info.title, info.gitLink
                    )
                    notificationPort.notify(channel, message)
                }
                GithubConstants.GithubActionType.PULL_REQUEST_ACTION_TYPE_CLOSED.code -> {
                    if(info.merged.equals("true")){
                        val message = GithubConstants.GithubMessageTemplate.PULL_REQUEST_DEFAULT_TEMPLATE.code.formatted(
                            GithubConstants.GithubMessageType.MERGED_MESSAGE.code, info.repoName, info.assignee, info.title, info.gitLink
                        )
                        notificationPort.notify(channel, message)
                    }else{
                        val message = GithubConstants.GithubMessageTemplate.PULL_REQUEST_DEFAULT_TEMPLATE.code.formatted(
                            GithubConstants.GithubMessageType.CLOSED_MESSAGE.code, info.repoName, info.assignee, info.title, info.gitLink
                        )
                        notificationPort.notify(channel, message)
                    }
                }
                GithubConstants.GithubActionType.PULL_REQUEST_ACTION_TYPE_REVIEW_REQUESTED.code -> {
                    val message = GithubConstants.GithubMessageTemplate.PULL_REQUEST_REVIEW_REQUESTED_TEMPLATE.code.formatted(info.repoName, info.assignee, info.title, info.gitLink)
                    notificationPort.notify(channel, message)
                }
                else -> throw NotSupportedActionTypeException()
            }
        }catch (be: BadRequestException) {
            throw be
        }catch (e: Exception) {
            throw InternalServerErrorException()
        }
    }

    override fun processPullRequestReview(payload: String, channel: String) {
        val info = GithubUtils.parse(payload)

        if(!info.baseBranch.equals("master")) {
            return
        }

        when(info.actionType) {
            GithubConstants.GithubActionType.PULL_REQUEST_REVIEW_ACTION_TYPE_SUBMITTED.code -> {
                val message = GithubConstants.GithubMessageTemplate.PULL_REQUEST_REVIEW_SUBMITTED_TEMPLATE.code.formatted(info.reviewer, info.repoName, info.title, info.gitLink)
                notificationPort.notify(channel, message)
            }
            else -> throw NotSupportedActionTypeException()
        }
    }

    override fun processPullRequestReviewComment(payload: String, channel: String) {
        val info = GithubUtils.parse(payload)

        if(!info.baseBranch.equals("master")) {
            return
        }

        when(info.actionType) {
            GithubConstants.GithubActionType.PULL_REQUEST_REVIEW_COMMENT_ACTION_TYPE_EDITED.code -> {
                val message = GithubConstants.GithubMessageTemplate.PULL_REQUEST_REVIEW_COMMENT_EDITED_TEMPLATE.code.formatted(info.repoName, info.reviewer, info.title, info.gitLink)
                notificationPort.notify(channel, message)
            }
            else -> throw NotSupportedActionTypeException()
        }
    }

    override fun processIssueComment(payload: String, channel: String) {
        val info = GithubUtils.parse(payload)

        if(!info.baseBranch.equals("master")) {
            return
        }

        when(info.actionType) {
            GithubConstants.GithubActionType.ISSUE_COMMENT_ACTION_TYPE_CREATED.code -> {
                val message = GithubConstants.GithubMessageTemplate.ISSUE_COMMENT_CREATED_TEMPLATE.code.formatted(info.repoName, info.reviewer, info.title, info.gitLink)
                notificationPort.notify(channel, message)
            }
            GithubConstants.GithubActionType.ISSUE_COMMENT_ACTION_TYPE_EDITED.code -> {
                val message = GithubConstants.GithubMessageTemplate.ISSUE_COMMENT_EDITED_TEMPLATE.code.formatted(info.repoName, info.reviewer, info.title, info.gitLink)
                notificationPort.notify(channel, message)
            }
            else -> throw NotSupportedActionTypeException()
        }
    }
}