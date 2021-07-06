package com.leskov.g_shop_test.core.extensions

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 *  Created by Android Studio on 7/5/2021 7:09 PM
 *  Developer: Sergey Leskov
 */

fun <T> Single<T>.applyIO(): Single<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.applyIO(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.applyIO(): Flowable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun Completable.applyIO(): Completable =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Maybe<T>.applyIO(): Maybe<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

inline fun <T> Single<List<T>>.listSubscribeBy(
    crossinline onSuccess: (List<T>) -> Unit,
    crossinline onEmptyList: () -> Unit = {},
    noinline onError: (Throwable) -> Unit
): Disposable = this.subscribeBy(
    onSuccess = {
        if (it.isNotEmpty()) {
            onSuccess.invoke(it)
        } else {
            onEmptyList.invoke()
        }
    },
    onError = onError
)

fun <T> Flowable<T>.doOnFirst(onFirstAction: (T) -> Unit): Flowable<T> =
    take(1)
        .doOnNext { onFirstAction.invoke(it) }
        .concatWith(skip(1))