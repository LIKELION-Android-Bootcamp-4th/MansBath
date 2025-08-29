import {FieldValue, getFirestore} from "firebase-admin/firestore";
import {onCall, HttpsError} from "firebase-functions/v2/https";
import {provider} from "../type/auth_types";
import {getAuth} from "firebase-admin/auth";

export const loginWithNaver = onCall(
  {region: "asia-northeast3"},
  async (request) => {
    const accessToken = request.data.accessToken as string;
    if (!accessToken) {
      throw new HttpsError("invalid-argument", "Access token is required.");
    }

    // 네이버 프로필 조회
    // 요청 url : https://openapi.naver.com/v1/nid/me
    // 다음과 같은 형식으로 헤더 값에 접근 토큰(access token)을 포함합니다. 토큰 타입은 "Bearer"로 값이 고정되어 있습니다.
    // Authorization: {토큰 타입] {접근 토큰]
    const profileResult = await fetch("https://openapi.naver.com/v1/nid/me", {
      headers: {Authorization: `Bearer ${accessToken}`},
    });

    if (!profileResult.ok) {
      throw new HttpsError("unknown", `Failed to fetch Naver profile: ${profileResult.statusText}`);
    }

    // json 객체로 파싱
    const profile = await profileResult.json();
    if (profile.resultcode !== "00") {
      throw new HttpsError("unknown", `result code is not "00": ${profile.message}`);
    }

    const uid = `${profile.response.id}`;

    // Firestore 사용자 확인 / 신규 생성
    const userRef = getFirestore().collection("users").doc(uid);
    const userSnap = await userRef.get();
    if (!userSnap.exists) {
      await userRef.set({
        provider: provider.NAVER,
        uid: uid,
        email: profile.response.email || "",
        name: profile.response.nickname || "",
        lastLogin: FieldValue.serverTimestamp(),
      });
    } else {
      await userRef.update({
        lastLogin: FieldValue.serverTimestamp(),
      });
    }

    // 파이어베이스 auth 콘솔의 식별자에 email 출력
    const auth = getAuth();
    try {
      // 최초 회원가입 시에만 createUser 호출
      await auth.getUser(uid).catch(async () => {
        await auth.createUser({
          uid,
          email: profile.response.email || "",
        });
      });
    } catch (e) {
      console.error("Auth user sync failed:", e);
    }


    // Firebase 커스텀 토큰 발급
    const customToken = await auth.createCustomToken(uid);
    return customToken;
  }
);
