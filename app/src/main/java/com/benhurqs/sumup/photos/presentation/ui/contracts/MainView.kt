package com.benhurqs.sumup.photos.presentation.ui.contracts

interface MainView{
    fun showLoading()
    fun hideLoading()
    fun showError(error: String)
    fun isAdded(): Boolean
}