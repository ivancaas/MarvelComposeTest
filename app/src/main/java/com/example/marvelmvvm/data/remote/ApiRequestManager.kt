package com.example.marvelmvvm.data.remote

import com.example.marvelmvvm.BuildConfig
import java.math.BigInteger
import java.security.MessageDigest



object ApiRequestManager {

    fun generateHashQuery(timestamp: String) =
        (timestamp + BuildConfig.MARVEL_PRIV_KEY + BuildConfig.MARVEL_PUB_KEY).toMD5()

    fun String.toMD5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
    }
}
