export interface ISubTitle {
  id: number;
  code?: string | null;
  designation?: string | null;
}

export type NewSubTitle = Omit<ISubTitle, 'id'> & { id: null };
