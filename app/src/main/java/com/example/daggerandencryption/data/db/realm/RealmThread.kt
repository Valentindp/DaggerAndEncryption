package com.example.daggerandencryption.data.db.realm

import android.os.Handler
import android.os.HandlerThread
import kotlinx.coroutines.android.asCoroutineDispatcher

object RealmThread {

    private val threadR = HandlerThread("RealmThread")
    private val handler = Handler(getThread().looper)

    private fun getThread(): HandlerThread {
        if (!threadR.isAlive) threadR.start()
        return threadR
    }

    val dispatcher = handler.asCoroutineDispatcher()

}
