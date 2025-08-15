package com.aspa.aspa.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.functions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module //의존성 생성
@InstallIn(SingletonComponent::class) // 어떤 컴포넌트 사용할지 결정
object FirebaseModule {

    @Provides // 직접 인스턴스 생성해서 반환
    @Singleton // 앱 전역에서 하나만 유지할 것. 인스턴스가 재사용됨
    fun provideAuth() : FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFireStore() : FirebaseFirestore  = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFunctions(): FirebaseFunctions = Firebase.functions("asia-northeast3")

}