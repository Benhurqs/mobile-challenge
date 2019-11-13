package com.benhurqs.sumup.commons.data

interface APICallback<RESPONSE, ERRROR> {
    fun onStart()
    fun onError(error: ERRROR)
    fun onFinish()
    fun onSuccess(response: RESPONSE)
}
