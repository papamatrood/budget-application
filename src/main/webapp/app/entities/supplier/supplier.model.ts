export interface ISupplier {
  id: number;
  companyName?: string | null;
  address?: string | null;
  phone?: string | null;
  nifNumber?: string | null;
  commercialRegister?: string | null;
  bankAccount?: string | null;
  mandatingEstablishment?: string | null;
  email?: string | null;
  website?: string | null;
  description?: string | null;
  contactFirstname?: string | null;
  contactlastname?: string | null;
}

export type NewSupplier = Omit<ISupplier, 'id'> & { id: null };
