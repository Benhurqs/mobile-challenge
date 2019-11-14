package com.benhurqs.sumup.photos.presentation.ui.contracts

interface MainView{
    fun showLoading()
    fun hideLoading()
    fun isAdded(): Boolean
}