import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
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
    return this.http.get<PaginatedList<Offender>>(this.baseUrl, {
      params: {
        page,
        pageSize,
      },
    });
  }

  saveOffender(offender: Offender): Observable<Offender> {
    return this.http.put<Offender>(
      this.baseUrl,
      this.prepareSaveAndUpdateFormData(offender)
    );
  }

  updateOffender(offenderId: number, offender: Offender): Observable<Offender> {
    return this.http.post<Offender>(
      `${this.baseUrl}/${offenderId}`,
      this.prepareSaveAndUpdateFormData(offender)
    );
  }

  private prepareSaveAndUpdateFormData(offender: Offender): FormData {
    const formData = new FormData();
    formData.append('offender', transformObjectToFormData(offender));

    return formData;
  }
}
