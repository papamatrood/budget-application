import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChapter, NewChapter } from '../chapter.model';

export type PartialUpdateChapter = Partial<IChapter> & Pick<IChapter, 'id'>;

export type EntityResponseType = HttpResponse<IChapter>;
export type EntityArrayResponseType = HttpResponse<IChapter[]>;

@Injectable({ providedIn: 'root' })
export class ChapterService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chapters');

  create(chapter: NewChapter): Observable<EntityResponseType> {
    return this.http.post<IChapter>(this.resourceUrl, chapter, { observe: 'response' });
  }

  update(chapter: IChapter): Observable<EntityResponseType> {
    return this.http.put<IChapter>(`${this.resourceUrl}/${this.getChapterIdentifier(chapter)}`, chapter, { observe: 'response' });
  }

  partialUpdate(chapter: PartialUpdateChapter): Observable<EntityResponseType> {
    return this.http.patch<IChapter>(`${this.resourceUrl}/${this.getChapterIdentifier(chapter)}`, chapter, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChapter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChapter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getChapterIdentifier(chapter: Pick<IChapter, 'id'>): number {
    return chapter.id;
  }

  compareChapter(o1: Pick<IChapter, 'id'> | null, o2: Pick<IChapter, 'id'> | null): boolean {
    return o1 && o2 ? this.getChapterIdentifier(o1) === this.getChapterIdentifier(o2) : o1 === o2;
  }

  addChapterToCollectionIfMissing<Type extends Pick<IChapter, 'id'>>(
    chapterCollection: Type[],
    ...chaptersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const chapters: Type[] = chaptersToCheck.filter(isPresent);
    if (chapters.length > 0) {
      const chapterCollectionIdentifiers = chapterCollection.map(chapterItem => this.getChapterIdentifier(chapterItem));
      const chaptersToAdd = chapters.filter(chapterItem => {
        const chapterIdentifier = this.getChapterIdentifier(chapterItem);
        if (chapterCollectionIdentifiers.includes(chapterIdentifier)) {
          return false;
        }
        chapterCollectionIdentifiers.push(chapterIdentifier);
        return true;
      });
      return [...chaptersToAdd, ...chapterCollection];
    }
    return chapterCollection;
  }
}
