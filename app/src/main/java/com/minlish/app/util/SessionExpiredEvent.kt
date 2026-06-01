package com.minlish.app.util
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


object SessionExpiredEvent {
    private val _flow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val flow = _flow.asSharedFlow()

    fun emit() {
        _flow.tryEmit(Unit)
    }
}