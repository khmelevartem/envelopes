package com.tubetoast.envelopes.common.di

interface Api

inline fun <reified T : Api> api(): T = DI.api(T::class.java)

@Suppress("UNCHECKED_CAST")
object DI {

    private val apis = mutableMapOf<Class<*>, Api>()

    fun <T : Api> api(clazz: Class<T>): T = apis.getOrPut(clazz) {
        clazz.getConstructor().newInstance()
    } as T
}
