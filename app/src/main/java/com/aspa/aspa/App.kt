package com.aspa.aspa

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() // 전 주입의 시작점. 무조건 있어야함. hilt에 관련된 설치점이라 생각하면 됨.