package kz.adamant.bookstore.utils

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class ThrottledLiveData<T>(source: LiveData<T>, delayMs: Long) : MediatorLiveData<T>() {
    val handler = Handler(Looper.getMainLooper())
    var delayMs = delayMs
        private set

    private var isValueDelayed = false
    private var delayedValue: T? = null
    private var delayRunnable: Runnable? = null
        set(value) {
            field?.let { handler.removeCallbacks(it) }
            value?.let { handler.postDelayed(it, delayMs) }
            field = value
        }
    private val objDelayRunnable = Runnable { if (consumeDelayedValue()) startDelay() }

    init {
        addSource(source) { newValue ->
            if (delayRunnable == null) {
                value = newValue
                startDelay()
            } else {
                isValueDelayed = true
                delayedValue = newValue
            }
        }
    }

    fun startThrottling(newDelay: Long = 0L) {
        require(newDelay >= 0L)
        when {
            newDelay > 0 -> delayMs = newDelay
            delayMs < 0 -> delayMs *= -1
            delayMs > 0 -> return
            else -> throw IllegalArgumentException("newDelay cannot be zero if old delayMs is zero")
        }
    }

    fun stopThrottling(immediate: Boolean = false) {
        if (delayMs <= 0) return
        delayMs *= -1
        if (immediate) consumeDelayedValue()
    }

    override fun onInactive() {
        super.onInactive()
        consumeDelayedValue()
    }

    private fun startDelay() {
        delayRunnable = if (delayMs > 0 && hasActiveObservers()) objDelayRunnable else null
    }

    private fun consumeDelayedValue(): Boolean {
        delayRunnable = null
        return if (isValueDelayed) {
            value = delayedValue
            delayedValue = null
            isValueDelayed = false
            true
        } else false
    }
}