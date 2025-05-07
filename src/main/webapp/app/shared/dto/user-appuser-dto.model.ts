/* eslint-disable prettier/prettier */
import { FamilySituationEnum } from "app/entities/enumerations/family-situation-enum.model";
import { GenderEnum } from "app/entities/enumerations/gender-enum.model";
import dayjs from "dayjs/esm";

export interface IUserAppUserDTO {
  id: number;
  // User fields
  login: string;
  firstName?: string | null;
  lastName?: string | null;
  imageUrl?: string | null;
  email: string;
  activated?: boolean;
  langKey?: string;
  authorities: string[];

  // AppUser fields
  accountStatus?: boolean | null;
  phoneNumber?: string | null;
  birthDate?: dayjs.Dayjs | null;
  birthPlace?: string | null;
  gender?: keyof typeof GenderEnum | null;
  familySituation?: keyof typeof FamilySituationEnum | null;
  position?: string | null;
  address?: string | null;
  jhipsterUserId?: number | null;
}

export type NewUserAppUserDTO = Omit<IUserAppUserDTO, "id"> & { id: null };
