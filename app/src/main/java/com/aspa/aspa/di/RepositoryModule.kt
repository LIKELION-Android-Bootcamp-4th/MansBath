package com.aspa.aspa.di

import com.aspa.aspa.data.remote.StudyFireStoreDataSource
import com.aspa.aspa.data.repository.GoogleRepositoryImpl
import com.aspa.aspa.data.repository.AuthRepository
import com.aspa.aspa.data.repository.StudyFireStoreRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // 앱 전역 설정
interface RepositoryModule {
    @Binds //추상 메서드 처리
    @Singleton // 앱 생명주기 동안 인스턴스 1개만
    fun bindAuthRepository(
        impl : GoogleRepositoryImpl
    ): AuthRepository
}
// 1. AuthRepository를 요구
// 2. hilt가 바인딩 규칙을 보고
//3. GoogleRepositoryImpl을 AuthRepository로 주입