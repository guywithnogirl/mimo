package com.privatechat.app.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val username: String, val password: String)
data class TokenPair(val access: String, val refresh: String)

interface AuthApi {
    @POST("api/auth/token/")
    suspend fun login(@Body request: LoginRequest): TokenPair
}

class AuthRepository(context: Context) {
    private val api = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()))
        .build()
        .create(AuthApi::class.java)

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "auth",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    suspend fun login(username: String, password: String) = withContext(Dispatchers.IO) {
        val tokens = api.login(LoginRequest(username, password))
        prefs.edit().putString("access", tokens.access).putString("refresh", tokens.refresh).apply()
    }
}
