import { ISubTitle } from 'app/entities/sub-title/sub-title.model';

export interface IChapter {
  id: number;
  code?: string | null;
  designation?: string | null;
  subTitle?: ISubTitle | null;
}

export type NewChapter = Omit<IChapter, 'id'> & { id: null };
