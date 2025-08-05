import {logger} from "firebase-functions";
import {getFirestore, FieldValue} from "firebase-admin/firestore";
import {getAuth} from "firebase-admin/auth";
import express, {Request, Response} from "express";
import cors from "cors";

const app = express();
app.use(cors({origin: true}));
app.use(express.json());

app.post("/", async (req: Request, res: Response) => {
  const idToken = req.body.token as string;

  if (!idToken) {
    return res.status(400).json({error: "Token is required."});
  }

  try {
    const decodedToken = await getAuth().verifyIdToken(idToken);
    const {uid, email, name} = decodedToken;

    const userRef = getFirestore().collection("users").doc(uid);
    await userRef.set({
      uid,
      email,
      name,
      lastLogin: FieldValue.serverTimestamp(),
    }, {merge: true});

    logger.info(`User verified and data saved: ${uid}`);
    return res.status(200).json({
      status: "success",
      uid: uid,
      message: "User authenticated successfully.",
    });
  } catch (error) {
    logger.error("Error verifying token:", error);
    return res.status(401).json({error: "Unauthorized: Invalid token."});
  }
});

export const authApp = app;
