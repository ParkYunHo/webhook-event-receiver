package com.john.webhook.webhook.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.john.webhook.common.constants.GithubConstants
import com.john.webhook.common.exception.BadRequestException
import com.john.webhook.common.exception.InternalServerErrorException
import com.john.webhook.common.exception.NotSupportedActionTypeException
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
    override fun processIssueComment(payload: String, channel: String) {

    }

    override fun processPullRequestReviewComment(payload: String, channel: String) {

    }

    override fun processPullRequestReview(payload: String, channel: String) {

    }

    override fun processPullRequest(payload: String, channel: String) {
        try{
            val jsonNode = objectMapper.readTree(payload)

            val actionType = jsonNode.at(GithubConstants.GithubPayloadPath.ACTION_TYPE.code).asText()
            val title = jsonNode.at(GithubConstants.GithubPayloadPath.TITLE.code).asText()
            val assignee = jsonNode.at(GithubConstants.GithubPayloadPath.ASSIGNEE.code).asText()
            val reviewer = jsonNode.at(GithubConstants.GithubPayloadPath.REVIEWER.code).asText()
            val gitLink = jsonNode.at(GithubConstants.GithubPayloadPath.URL.code).asText()
            val repoName = jsonNode.at(GithubConstants.GithubPayloadPath.REPO_NAME.code).asText()
            val base_branch = jsonNode.at(GithubConstants.GithubPayloadPath.BASE_BRANCH.code).asText()

            if(!base_branch.equals("master")){
                return
            }

            when(actionType) {
                GithubConstants.GithubActionType.PULL_REQUEST_ACTION_TYPE_OPENED.code -> {
                    val message = GithubConstants.GithubMessageTemplate.PULL_REQUEST_DEFAULT_TEMPLATE.code.formatted(
                        GithubConstants.GithubMessageType.CREATED_MESSAGE.code, repoName, assignee, title, gitLink
                    )
                    notificationPort.notify(channel, message)
                }
                GithubConstants.GithubActionType.PULL_REQUEST_ACTION_TYPE_CLOSED.code -> {
                    val merged = jsonNode.at(GithubConstants.GithubPayloadPath.MERGED.code).asText()
                    if("true".equals(merged)){
                        val message = GithubConstants.GithubMessageTemplate.PULL_REQUEST_DEFAULT_TEMPLATE.code.formatted(
                            GithubConstants.GithubMessageType.MERGED_MESSAGE.code, repoName, assignee, title, gitLink
                        )
                        notificationPort.notify(channel, message)
                    }else{
                        val message = GithubConstants.GithubMessageTemplate.PULL_REQUEST_DEFAULT_TEMPLATE.code.formatted(
                            GithubConstants.GithubMessageType.CLOSED_MESSAGE.code, repoName, assignee, title, gitLink
                        )
                        notificationPort.notify(channel, message)
                    }
                }
                GithubConstants.GithubActionType.PULL_REQUEST_ACTION_TYPE_REVIEW_REQUESTED.code -> {
                    val message = GithubConstants.GithubMessageTemplate.PULL_REQUEST_REVIEW_REQUESTED_TEMPLATE.code.formatted(repoName, assignee, title, gitLink)
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
}