import {logger} from "firebase-functions";
import {getFirestore, FieldValue} from "firebase-admin/firestore";
import {getAuth} from "firebase-admin/auth";
import {Request, Response} from "express";
import {SNS} from "../type/auth_types";

/**
 * Verifies a Google ID token and stores the user info in Firestore.
 *
 * @param {Request} req Express request containing the Google ID token in `body.token`
 * @param {Response} res Express response object
 */
export async function loginWithGoogle(req: Request, res: Response) {
  const idToken = req.body.token as string;

  if (!idToken) {
    return res.status(400).json({error: "Token is required."});
  }

  try {
    const decodedToken = await getAuth().verifyIdToken(idToken);
    const {uid, email, name} = decodedToken;

    const userRef = getFirestore().collection("users").doc(uid);
    await userRef.set({
      sns: SNS.GOOGLE,
      uid,
      email,
      name,
      lastLogin: FieldValue.serverTimestamp(),
    }, {merge: true});

    logger.info(`User verified and data saved: ${uid}`);

    return res.status(200).json({
      status: "success",
      uid,
      message: "Google User authenticated successfully.",
    });
  } catch (error) {
    logger.error("Error verifying token:", error);
    return res.status(401).json({error: "Unauthorized: Invalid token."});
  }
}
