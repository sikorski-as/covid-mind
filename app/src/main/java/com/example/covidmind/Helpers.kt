package com.example.covidmind

open class OneShotNotification {
    var hasBeenHandled = false
        private set // Allow external read but not write

    val shouldBeHandled: Boolean
        get() = !hasBeenHandled

    fun markAsHandled() {
        hasBeenHandled = true
    }
}

open class OneShotNotificationWithContent<out T>(private val _content: T) : OneShotNotification() {

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T {
        check(!hasBeenHandled)
        markAsHandled()
        return _content
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = _content
}

