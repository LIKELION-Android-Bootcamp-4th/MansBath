import {logger} from "firebase-functions";
import {onRequest} from "firebase-functions/v2/https";
import {onDocumentCreated} from "firebase-functions/v2/firestore";
import {initializeApp} from "firebase-admin/app";
import {getFirestore} from "firebase-admin/firestore";
import {Request, Response} from "express";


initializeApp();

export const addmessage = onRequest({
  region: "asia-northeast3",
}, async (req: Request, res: Response) => {
  const original = req.query.text;
  const writeResult = await getFirestore()
    .collection("messages")
    .add({original: original});

  res.json({result: `Message with ID: ${writeResult.id} added.`});
});


export const makeuppercase = onDocumentCreated(
  {
    region: "asia-northeast3",
    document: "/messages/{documentId}",
  },
  (event) => {
    const original = event.data?.data().original;

    logger.log("Uppercasing", event.params.documentId, original);

    const uppercase = original.toUpperCase();

    return event.data?.ref.set({uppercase}, {merge: true});
  });
