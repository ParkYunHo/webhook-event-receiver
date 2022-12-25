package com.john.webhook.webhook.application.dto

/**
 * @author yoonho
 * @since 2022.12.25
 */
data class GithubWebhookInfo(
   val actionType: String,
   val title: String,
   val assignee: String,
   val reviewer: String,
   val gitLink: String,
   val repoName: String,
   val baseBranch: String,
   val merged: String
)