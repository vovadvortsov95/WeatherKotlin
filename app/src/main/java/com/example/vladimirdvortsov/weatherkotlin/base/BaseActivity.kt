package com.example.vladimirdvortsov.weatherkotlin.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity( @LayoutRes layoutRes : Int) : AppCompatActivity(layoutRes) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        observeChanges()
    }

    protected abstract fun initView()

    protected abstract fun observeChanges()

}