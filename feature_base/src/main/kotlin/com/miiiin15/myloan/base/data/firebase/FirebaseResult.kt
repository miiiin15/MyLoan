package com.miiiin15.myloan.base.data.firebase

sealed interface FirebaseResult <T> {

    class Success<T>(val data: T) : FirebaseResult<T>

    class Error<T>(val code: Int, val message: String?) : FirebaseResult<T>

    class Exception<T>(val throwable: Throwable) : FirebaseResult<T>
}
