package com.benhurqs.sumup.main.presentation.contracts

interface MainView{
    fun showLoading()
    fun hideLoading()
    fun showError()
    fun hideError()
    fun isAdded(): Boolean
}