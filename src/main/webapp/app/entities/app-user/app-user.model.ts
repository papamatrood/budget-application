import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { GenderEnum } from 'app/entities/enumerations/gender-enum.model';
import { FamilySituationEnum } from 'app/entities/enumerations/family-situation-enum.model';

export interface IAppUser {
  id: number;
  accountStatus?: boolean | null;
  lastDateUpdate?: dayjs.Dayjs | null;
  dateCreated?: dayjs.Dayjs | null;
  firstname?: string | null;
  lastname?: string | null;
  phoneNumber?: string | null;
  birthDate?: dayjs.Dayjs | null;
  birthPlace?: string | null;
  gender?: keyof typeof GenderEnum | null;
  familySituation?: keyof typeof FamilySituationEnum | null;
  position?: string | null;
  address?: string | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewAppUser = Omit<IAppUser, 'id'> & { id: null };
