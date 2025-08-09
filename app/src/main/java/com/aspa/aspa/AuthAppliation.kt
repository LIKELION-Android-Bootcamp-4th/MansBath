package com.aspa.aspa

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class AuthApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화
        KakaoSdk.init(this, "YOUR_NATIVE_APP_KEY")
    }
}