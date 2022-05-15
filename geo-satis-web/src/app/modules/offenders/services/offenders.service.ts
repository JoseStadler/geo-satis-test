import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { PaginatedList } from 'src/app/shared/models/paginated-list.model';
import { transformObjectToFormData } from 'src/app/shared/util/util.functions';
import { environment } from 'src/environments/environment';
import { Offender } from '../models/offender.model';

@Injectable({
  providedIn: 'root',
})
export class OffendersService {
  private baseUrl = environment.apiUrl + 'offenders';

  constructor(private http: HttpClient) {}

  findOffendersPaged(
    page: number,
    pageSize: number
  ): Observable<PaginatedList<Offender>> {
    return this.http
      .get<PaginatedList<Offender>>(this.baseUrl, {
        params: {
          page,
          pageSize,
        },
      })
      .pipe(
        map((pagedList) => {
          pagedList.content.forEach((offender) => {
            if (offender.picture) {
              offender.picture = `${this.baseUrl}/findPhoto/${offender.picture}`;
            }
          });
          return pagedList;
        })
      );
  }

  saveOffender(offender: Offender, picture?: File): Observable<Offender> {
    return this.http.put<Offender>(
      this.baseUrl,
      this.prepareSaveAndUpdateFormData(offender, picture)
    );
  }

  updateOffender(
    offenderId: number,
    offender: Offender,
    picture?: File
  ): Observable<Offender> {
    return this.http.post<Offender>(
      `${this.baseUrl}/${offenderId}`,
      this.prepareSaveAndUpdateFormData(offender, picture)
    );
  }

  private prepareSaveAndUpdateFormData(
    offender: Offender,
    picture?: File
  ): FormData {
    const formData = new FormData();
    formData.append('offender', transformObjectToFormData(offender));

    if (picture) {
      formData.append('picture', picture);
    }

    return formData;
  }
}
