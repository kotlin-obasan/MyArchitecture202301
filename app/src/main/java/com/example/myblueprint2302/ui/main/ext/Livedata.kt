package com.example.myblueprint2302.ui.main.ext

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T, U : MutableLiveData<T>> U.default(value: T) = apply { setValue(value) }

fun LiveData<String>?.isNotEmpty(): Boolean = this?.value?.isNotEmpty() ?: false

fun LiveData<String>?.isEmpty(): Boolean = this?.value?.isEmpty() ?: false

fun <T> LiveData<T>?.isNotNull(): Boolean = this?.value != null