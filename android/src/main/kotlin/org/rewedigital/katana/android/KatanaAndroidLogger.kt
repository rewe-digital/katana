package org.rewedigital.katana.android

import android.util.Log
import org.rewedigital.katana.Katana

/**
 * Android specific [Katana.Logger] implementation.
 *
 * @see Katana.logger
 */
object KatanaAndroidLogger : Katana.Logger {

    private const val TAG = "KATANA"

    override fun debug(msg: String) {
        Log.d(TAG, msg)
    }

    override fun info(msg: String) {
        Log.i(TAG, msg)
    }

    override fun warn(msg: String) {
        Log.w(TAG, msg)
    }

    override fun error(msg: String, throwable: Throwable?) {
        if (throwable != null) {
            Log.e(TAG, msg, throwable)
        } else {
            Log.e(TAG, msg)
        }
    }
}
