package com.leskov.g_shop.core.extensions

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 *  Created by Android Studio on 3/4/2020 12:01 PM
 *  Developer: Sergey Zorych
 */


fun SwipeRefreshLayout.showRefresh(){
    this.isRefreshing = true
}

fun SwipeRefreshLayout.hideRefresh(){
    this.isRefreshing = false
}