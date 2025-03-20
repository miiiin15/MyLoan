package com.miiiin15.myloan.base.presentation.viewmodel

interface BaseAction<State> {
    fun reduce(state: State): State
}
