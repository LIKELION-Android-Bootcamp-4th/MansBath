import {onSchedule} from "firebase-functions/v2/scheduler";
import {onRequest} from "firebase-functions/https";
import * as admin from "firebase-admin";
import {getFirestore} from "firebase-admin/firestore";

export const checkFalseStatusScheduler = onSchedule({
  schedule: "0 11 * * *", // 매일 오전 11시, "every 30 seconds": 30초마다
  timeZone: "Asia/Seoul",
  region: "asia-northeast3",
}, async () => {
  return checkQuizStatus();
});

// 테스트용 http 함수
export const testCheckFalseStatus = onRequest({region: "asia-northeast3"}, async (req, res) => {
  await checkQuizStatus();
  res.send("Check finished");
});

/**
 * Firestore에 저장된 퀴즈 데이터를 조회해 안 푼 문제가 있다면 해당 유저에게 알림을 보냅니다.
 */
async function checkQuizStatus() {
  console.log("⏰ Checking firestore for false values...");

  try {
    // 1. 모든 유저 조회
    const usersSnapshot = await getFirestore().collection("users").get();
    userSearchLoop: for (const userDoc of usersSnapshot.docs) {
      console.log(`Searching user: ${userDoc.id}`);
      const quizzesSnapshot = await userDoc.ref.collection("quizzes").get();
      for (const roadmapDoc of quizzesSnapshot.docs) {
        const quizItemsSnapshot = await roadmapDoc.ref.collection("quiz").get();
        for (const doc of quizItemsSnapshot.docs) {
          if (doc.data().status == false) {
            // 2. 해당 유저의 FCM 토큰 가져오기
            const token = userDoc.get("fcmToken");
            if (!token) {
              continue userSearchLoop;
            }

            // 3. 메시지 전송
            const message = {
              token: token,
              notification: {
                title: "🚨 미완료 퀴즈 알림",
                body: "아직 풀지 않은 퀴즈가 있습니다.",
              },
            };

            await admin.messaging().send(message);
            console.log(`✅ Sent FCM to ${userDoc.id}`);
            continue userSearchLoop;
          }
        }
      }
    }
  } catch (error) {
    console.error("❌ Error in scheduler:", error);
  }
}
