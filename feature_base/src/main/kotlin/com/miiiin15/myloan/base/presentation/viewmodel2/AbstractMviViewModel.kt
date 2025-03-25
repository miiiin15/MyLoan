package com.miiiin15.myloan.base.presentation.viewmodel2

import android.os.Build
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miiiin15.myloan.base.BuildConfig
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

abstract class AbstractMviViewModel<I : MviIntent, S : MviViewState, E : MviSingleEvent> :
    MviViewModel<I, S, E>, ViewModel() {
    // 로그 태그의 기본값을 설정하는 변수
    protected open val rawLogTag: String? = null

    // lazy 초기화를 사용하여 로그 태그를 설정
    protected val logTag by lazy(LazyThreadSafetyMode.PUBLICATION) {
        (rawLogTag ?: this::class.java.simpleName).let { tag: String ->
            // API 26 이전에는 태그 길이가 23자로 제한되므로 이를 처리
            if (tag.length <= 23 || Build.VERSION.SDK_INT >= 26) {
                tag
            } else {
                tag.take(23)
            }
        }
    }

    // 단일 이벤트를 처리하기 위한 채널
    private val eventChannel = Channel<E>(Channel.UNLIMITED)

    // Intent를 처리하기 위한 MutableSharedFlow
    private val intentMutableFlow = MutableSharedFlow<I>(extraBufferCapacity = Int.MAX_VALUE)

    // 외부에서 단일 이벤트를 Flow로 접근할 수 있도록 설정
    final override val singleEvent: Flow<E> = eventChannel.receiveAsFlow()

    @MainThread
    final override suspend fun processIntent(intent: I) {
        // 메인 스레드에서 호출되었는지 확인
        check(intentMutableFlow.tryEmit(intent)) { "❌인텐트 전송 실패: $intent" }
        Timber.tag(logTag).d(">>>🟢인텐트 처리: $intent")
    }

    // ViewModel이 제거될 때 호출되는 메서드
    @CallSuper
    override fun onCleared() {
        super.onCleared()
        eventChannel.close()
        Timber.tag(logTag).d("onCleared: 뷰모델이 제거되었습니다.")
    }

    // 단일 이벤트를 채널에 전송하는 메서드
    protected suspend fun sendEvent(event: E) {
        eventChannel.trySend(event)
            .onSuccess { Timber.tag(logTag).d("sendEvent: 이벤트 전송 성공 - $event") }
            .onFailure {
                Timber
                    .tag(logTag)
                    .e(it, "sendEvent: 이벤트 전송 실패 - $event")
            }
            .getOrThrow()
    }

    // Intent를 SharedFlow로 접근할 수 있도록 설정
    protected val intentSharedFlow: SharedFlow<I> get() = intentMutableFlow

    // Flow에 디버그 로그를 추가하는 확장 함수
    protected fun <T> Flow<T>.debugLog(subject: String): Flow<T> =
        if (BuildConfig.DEBUG) {
            onEach { Timber.tag(logTag).d(">>> $subject: $it") }
        } else {
            this
        }

    // SharedFlow에 디버그 로그를 추가하는 확장 함수
    protected fun <T> SharedFlow<T>.debugLog(subject: String): SharedFlow<T> =
        if (BuildConfig.DEBUG) {
            val self = this

            object : SharedFlow<T> by self {
                val subscriberCount = AtomicInteger(0)

                override suspend fun collect(collector: FlowCollector<T>): Nothing {
                    val count = subscriberCount.getAndIncrement()

                    self.collect {
                        Timber.tag(logTag).d(">>> $subject ~ 구독자 $count: $it")
                        collector.emit(it)
                    }
                }
            }
        } else {
            this
        }

    // Flow를 구독 중일 때만 공유하는 SharedFlow로 변환하는 확장 함수
    protected fun <T> Flow<T>.shareWhileSubscribed(): SharedFlow<T> =
        shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    // Flow를 구독 중일 때만 공유하며 초기값을 null로 설정하는 StateFlow로 변환하는 확장 함수
    protected fun <T> Flow<T>.stateWithInitialNullWhileSubscribed(): StateFlow<T?> =
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    // 이미 공유된 SharedFlow를 다시 공유하지 않도록 하는 확장 함수 (사용 금지)
    @Deprecated(
        message = "이 Flow는 이미 viewModelScope에서 공유되었으므로 다시 공유할 필요가 없습니다.",
        replaceWith = ReplaceWith("this"),
        level = DeprecationLevel.ERROR
    )
    protected fun <T> SharedFlow<T>.shareWhileSubscribed(): SharedFlow<T> = this
}