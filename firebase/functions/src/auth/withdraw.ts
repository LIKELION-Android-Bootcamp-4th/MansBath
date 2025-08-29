import * as functions from "firebase-functions/v1";
import {getFirestore} from "firebase-admin/firestore";


export const withdraw = functions
  .region("asia-northeast3")
  .auth.user()
  .onDelete(async (user) => {
    try {
      const uid = user.uid;
      const db = getFirestore();

      await db.recursiveDelete(db.collection("users").doc(uid));

      functions.logger.info(`✅ Firestore 사용자 문서 삭제 완료 (uid: ${uid})`);
    } catch (err) {
      functions.logger.error("❌ Firestore 사용자 문서 삭제 중 오류 발생", err);
    }
  }
  );
