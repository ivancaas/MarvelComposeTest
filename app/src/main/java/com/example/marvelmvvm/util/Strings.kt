package com.example.marvelmvvm.util

import androidx.annotation.StringRes
import com.example.marvelmvvm.App

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return App.instance.getString(stringRes, *formatArgs)
    }
}