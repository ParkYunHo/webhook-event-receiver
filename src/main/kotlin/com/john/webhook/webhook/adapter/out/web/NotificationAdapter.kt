package com.john.webhook.webhook.adapter.out.web

import com.john.webhook.webhook.application.port.out.NotificationPort
import io.netty.handler.codec.json.JsonObjectDecoder
import jakarta.annotation.PostConstruct
import org.apache.tomcat.util.json.JSONParser
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder

/**
 * @author yoonho
 * @since 2022.12.25
 */
@Component
class NotificationAdapter(
    private val webClientBuilder: WebClient.Builder
): NotificationPort {

    private val log = LoggerFactory.getLogger(this::class.java)
    private lateinit var webClient: WebClient

    @PostConstruct
    fun initWebClient() {
        this.webClient = webClientBuilder.build()
    }

    override fun notify(channel: String, message: String) {
        this.slack(message)
    }

    private fun slack(message: String) {
        try{
            val uriComponents = UriComponentsBuilder
                .fromHttpUrl("https://hooks.slack.com" + "/services/T04GGF8E1NE/B04GGFE2NNN/ZukEYML4zfsiX5zelFhwXZ7C")
                .build(false)

            val body: MultiValueMap<String, String> = LinkedMultiValueMap()
            body.add("username", "Github Notification")
            body.add("text", message)

//            mapOf(
//                "username" to "Github Notification",
//                "text" to message
//            )


            val responseEntity = webClient.post()
                .uri(uriComponents.toUri())
//                .body(body)
//                .bodyValue(body.toString())
                .bodyValue(body)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
//                .onStatus(HttpStatus.BAD_REQUEST.is4xxClientError::equals) {}
                .toBodilessEntity()
                .block()
//                .onStatus(HttpStatus::is4xxClientError) { Mono.error(TokenInvalidException("kakao token invalid")) }
//                .onStatus(HttpStatus::is5xxServerError) { Mono.error(ThirdPartyServerException("kauth internal server error")) }
//                .toEntity(String::class.java)
//                .block()

        }catch(e: Throwable){
            throw e
        }
    }
}