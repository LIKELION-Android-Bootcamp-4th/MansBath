package com.aspa2025.aspa2025.di

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.aspa2025.aspa2025.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GoogleSignInModule {
    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager =
        // 구글 자격증명을 띄우는 인스턴스
        CredentialManager.create(context)

    @Provides
    @Singleton
    fun provideGoogleIdOption(@ApplicationContext context: Context): GetGoogleIdOption =
        // 구글 로그인 관련 설정을 담기
        GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .build()

    @Provides //Binds는 추상 객체이어서 생성자를 마음대로 꾸밀 수 있지만,
    // 이부분은 외부 객체를 가져와서 사용하는 것이여서 Provides를 사용함.
    @Singleton
    fun provideGetCredentialRequest(opt: GetGoogleIdOption): GetCredentialRequest =
        // GetGoogleIdOption을 토대로 요청
        GetCredentialRequest.Builder()
            .addCredentialOption(opt)
            .build()

}