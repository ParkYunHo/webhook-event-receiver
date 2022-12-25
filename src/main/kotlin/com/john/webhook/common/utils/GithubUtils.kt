package com.john.webhook.common.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.john.webhook.common.constants.GithubConstants
import com.john.webhook.webhook.application.dto.GithubWebhookInfo
import org.slf4j.LoggerFactory

/**
 * @author yoonho
 * @since 2022.12.25
 */
object GithubUtils {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private lateinit var objectMapper: ObjectMapper

    fun init(objectMapper: ObjectMapper) {
        this.objectMapper = objectMapper
    }

    fun parse(payload: String): GithubWebhookInfo {
        val jsonNode = objectMapper.readTree(payload)

        return GithubWebhookInfo(
            actionType = jsonNode.at(GithubConstants.GithubPayloadPath.ACTION_TYPE.code).asText(),
            title = jsonNode.at(GithubConstants.GithubPayloadPath.TITLE.code).asText(),
            assignee = jsonNode.at(GithubConstants.GithubPayloadPath.ASSIGNEE.code).asText(),
            reviewer = jsonNode.at(GithubConstants.GithubPayloadPath.REVIEWER.code).asText(),
            gitLink = jsonNode.at(GithubConstants.GithubPayloadPath.URL.code).asText(),
            repoName = jsonNode.at(GithubConstants.GithubPayloadPath.REPO_NAME.code).asText(),
            baseBranch = jsonNode.at(GithubConstants.GithubPayloadPath.BASE_BRANCH.code).asText(),
            merged = jsonNode.at(GithubConstants.GithubPayloadPath.MERGED.code).asText()
        )
    }
}