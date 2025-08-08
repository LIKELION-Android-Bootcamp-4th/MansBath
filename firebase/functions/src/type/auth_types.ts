export enum SNS {
  GOOGLE = "google",
  KAKAO = "kakao",
  NAVER = "naver",
}

// 카카오 프로필 정보
export interface KakaoProfile {
  nickname: string;
  thumbnail_image_url?: string; // ?는 선택적(optional) 속성을 의미합니다.
  profile_image_url?: string;
}

// 카카오 계정 정보 (개인정보 동의 항목 포함)
export interface KakaoAccount {
  profile_nickname_needs_agreement?: boolean;
  profile_image_needs_agreement?: boolean;
  profile: KakaoProfile;

  has_email?: boolean;
  email_needs_agreement?: boolean;
  is_email_valid?: boolean;
  is_email_verified?: boolean;
  email?: string;
}

// 최종 카카오 사용자 정보 객체
export interface KakaoUser {
  id: number;
  kakao_account: KakaoAccount;
}
