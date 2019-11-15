package com.benhurqs.sumup.photos.presentation.contracts

interface MainView{
    fun showLoading()
    fun hideLoading()
    fun showError()
    fun hideError()
    fun isAdded(): Boolean
}