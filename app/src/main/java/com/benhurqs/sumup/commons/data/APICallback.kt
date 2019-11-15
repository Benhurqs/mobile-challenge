package com.benhurqs.sumup.commons.data

interface APICallback<RESPONSE> {
    fun onStart()
    fun onError()
    fun onFinish()
    fun onSuccess(response: RESPONSE)
}
