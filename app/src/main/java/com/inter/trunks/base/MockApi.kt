package ru.mos.polls.kbase.mock

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import java.io.BufferedReader
import java.io.InputStreamReader

object MockApi {

    /**
     * Получение объекта из json в assets
     * @param name путь к .json файлу
     * @param type [TypeToken] для описания типа возвращаемого объекта
     */
    fun <Result> asset(name: String, type: TypeToken<Result>): Observable<Result> =
            javaClass.getResourceAsStream("/assets/$name").use { input ->
                val reader = BufferedReader(InputStreamReader(input))
                val r = Gson().fromJson<Result>(reader, type.type)
                Observable.just(r)
            }

}