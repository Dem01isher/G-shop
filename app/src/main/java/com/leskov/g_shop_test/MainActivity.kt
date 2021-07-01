package com.leskov.g_shop_test

import com.leskov.g_shop_test.core.activity.BaseBindingActivity
import com.leskov.g_shop_test.databinding.ActivityMainBinding

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_main

}