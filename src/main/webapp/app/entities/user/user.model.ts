/* eslint-disable prettier/prettier */
// export interface IUser {
//   id: number;
//   login?: string | null;
// }


export interface IUser {
  id: number;
  login?: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string;
  activated?: boolean;
  langKey?: string;
  authorities?: string[];
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
}