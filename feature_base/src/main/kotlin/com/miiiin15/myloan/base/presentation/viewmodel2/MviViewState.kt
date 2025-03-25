package com.miiiin15.myloan.base.presentation.viewmodel2

import android.os.Bundle

interface MviViewState

// 화면 상태를 저장하고 복원하는 데 사용
interface MviViewStateSaver<S : MviViewState> {
    fun S.toBundle(): Bundle // 확장 함수로 상태를 Bundle로 변환
    fun restore(bundle: Bundle?): S // 상태를 Bundle에서 복원
}