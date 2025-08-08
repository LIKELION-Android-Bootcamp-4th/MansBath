import {Request, Response} from "express";
import {getAuth} from "firebase-admin/auth";
import axios from "axios";
/**
 * 카카오 로그인 AccessToken을 활용하여 파이어베이스 로그인을 위한 CustomToken을 발급합니다.
 *
 * @param {Request} req `body.token`에 클라이언트에서 발급 받은 AccessToken이 있습니다.
 * @param {Response} res 파이어베이스 로그인을 위한 CustomToken 값입니다.
 */
export async function loginWithKakao(req: Request, res: Response) {
  const kakaoAccessToken = req.body.token as string;

  if (!kakaoAccessToken) {
    return res.status(400).json({error: "Token is required."});
  }
  try {
    // 1. 카카오 API를 호출하여 액세스 토큰으로 사용자 정보 조회
    const kakaoRes = await axios.get("https://kapi.kakao.com/v2/user/me", {
      headers: {
        Authorization: `Bearer ${kakaoAccessToken}`,
      },
    });

    const kakaoUser = kakaoRes.data;
    const kakaoUid = `kakao:${kakaoUser.id}`; // Firebase UID는 고유해야 합니다.

    // 2. Firebase Admin SDK를 사용하여 Custom Token 생성
    const customToken = await getAuth().createCustomToken(kakaoUid);

    // 3. 생성된 Custom Token을 클라이언트에 반환
    return res.status(200).json({customToken});
  } catch (error) {
    console.error("Error creating custom token:", error);
    return res.status(500).json({error: "Failed to create custom token"});
  }
}
