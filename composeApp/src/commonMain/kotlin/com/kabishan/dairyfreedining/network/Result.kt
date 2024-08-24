package com.kabishan.dairyfreedining.network

enum class Status {
    SUCCESS, FAILURE
}

data class Result<out T>(val status: Status, val data: T?, val code: Int?, val message: String?) {
    companion object {
        fun <T> success(data: T): Result<T> = Result(status = Status.SUCCESS, data = data, code = null, message = null)
        fun <T> error(code: Int?, message: String?): Result<T> = Result(status = Status.FAILURE, data = null, code = code, message = message)
    }
}