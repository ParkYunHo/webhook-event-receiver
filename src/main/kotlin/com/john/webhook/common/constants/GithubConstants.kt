package com.john.webhook.common.constants

/**
 * @author yoonho
 * @since 2022.12.25
 */
class GithubConstants {
    enum class GithubEventType(val code: String) {
        GITHUB_EVENT_PULL_REQUEST("pull_request"),
        GITHUB_EVENT_PULL_REQUEST_REVIEW("pull_request_review"),
        GITHUB_EVENT_PULL_REQUEST_REVIEW_COMMENT("pull_request_review_comment"),
        GITHUB_EVENT_ISSUE_COMMENT("issue_comment")
    }

    enum class GithubActionType(val code: String) {
        PULL_REQUEST_ACTION_TYPE_OPENED("opened"),
        PULL_REQUEST_ACTION_TYPE_CLOSED("closed"),
        PULL_REQUEST_ACTION_TYPE_REVIEW_REQUESTED("review_requested")
    }

    enum class GithubPayloadPath(val code: String) {
        ACTION_TYPE("/action"),
        TITLE("/pull_request/title"),
        ASSIGNEE("/pull_request/user/login"),
        REVIEWER("/requested_reviewer/login"),
        URL("/pull_request/html_url"),
        REPO_NAME("/pull_request/head/repo/name"),
        BASE_BRANCH("/pull_request/base/ref"),
        MERGED("/merged")
    }

    enum class GithubMessageTemplate(val code: String) {
        PULL_REQUEST_DEFAULT_TEMPLATE("️\uD83C\uDF1F풀 리퀘스트 %s!\uD83C\uDF1F%n%n도메인 : %s%n요청자 : %s%n제목 : %s%nURL : %s%n"),
        PULL_REQUEST_REVIEW_REQUESTED_TEMPLATE("️\uD83C\uDF1F풀 리퀘스트 요청!\uD83C\uDF1F%n%n%s 님이 코드 리뷰를 부탁하였습니다!%n도메인 : %s%n제목 : %s%nURL : %s%n"),
        PULL_REQUEST_REVIEW_SUBMITTED_TEMPLATE("\uD83C\uDF1F코드 리뷰 도착!\uD83C\uDF1F%n%n%s 님이 코드 리뷰를 작성해주셨습니다!%n도메인 : %s%n제목 : %s%nURL : %s%n"),
        PULL_REQUEST_REVIEW_COMMENT_EDITED_TEMPLATE("️\uD83C\uDF1F코드 리뷰 댓글 내용 변경!\uD83C\uDF1F%n%n%s 님이 작성한 코드 리뷰 댓글 내용이 변경되었습니다!%n도메인 : %s%n제목 : %s%nURL : %s%n"),
        ISSUE_COMMENT_CREATED_TEMPLATE("️\uD83C\uDF1F댓글 추가 !\uD83C\uDF1F%n%n%s 님이 풀 리퀘스트에 댓글을 달아주었습니다!%n도메인 : %s%n제목 : %s%nURL : %s%n"),
        ISSUE_COMMENT_EDITED_TEMPLATE("️\uD83C\uDF1F댓글 수정 !\uD83C\uDF1F%n%n%s 님이 풀 리퀘스트에 댓글을 수정하였습니다!%n도메인 : %s%n제목 : %s%nURL : %s%n")
    }

    enum class GithubMessageType(val code: String) {
        CREATED_MESSAGE("생성"),
        MERGED_MESSAGE("머지"),
        CLOSED_MESSAGE("종료")
    }
}