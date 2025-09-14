package com.bieniucieniu.hometooling.feat.shared.lib.flow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty

inline operator fun <T> StateFlow<T>.getValue(thisObj: Any?, property: KProperty<*>): T = value

inline operator fun <T> MutableStateFlow<T>.setValue(
    thisObj: Any?,
    property: KProperty<*>,
    value: T,
) {
    this.value = value
}
