import dayjs from "dayjs/esm";

import { IDecision, NewDecision } from "./decision.model";

export const sampleWithRequiredData: IDecision = {
  id: 12184,
  decisionNumber: "cadre coupable rapide",
  decisionDate: dayjs("2025-05-03T20:48"),
};

export const sampleWithPartialData: IDecision = {
  id: 24966,
  decisionNumber: "de manière à ce que vétuste",
  decisionDate: dayjs("2025-05-04T08:51"),
};

export const sampleWithFullData: IDecision = {
  id: 12277,
  decisionNumber: "bzzz cocorico",
  decisionDate: dayjs("2025-05-04T06:24"),
};

export const sampleWithNewData: NewDecision = {
  decisionNumber: "bien que admirablement à condition que",
  decisionDate: dayjs("2025-05-03T15:27"),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
