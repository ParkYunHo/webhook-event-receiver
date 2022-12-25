package com.john.webhook.common.exception

/**
 * @author yoonho
 * @since 2022.12.25
 */
class NotSupportedActionTypeException: RuntimeException {
    constructor(msg: String?): super(msg)
    constructor(): super()
}