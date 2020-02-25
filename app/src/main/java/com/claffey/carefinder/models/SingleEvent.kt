package com.claffey.carefinder.models

class SingleEvent<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return when {
            hasBeenHandled -> null
            else -> {
                hasBeenHandled = true
                content
            }
        }
    }

    fun peekContent(): T = content
}