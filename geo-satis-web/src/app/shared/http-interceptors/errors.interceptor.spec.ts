import {
  HttpClient,
  HttpErrorResponse,
  HTTP_INTERCEPTORS,
} from '@angular/common/http';
import {
  HttpTestingController,
  HttpClientTestingModule,
} from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ErrorsInterceptor } from './errors.interceptor';

describe('ErrorsInterceptor', () => {
  let client: HttpClient;
  let controller: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: HTTP_INTERCEPTORS,
          useClass: ErrorsInterceptor,
          multi: true,
        },
      ],
    });

    client = TestBed.inject(HttpClient);
    controller = TestBed.inject(HttpTestingController);
  });

  it('should control the response errors', (done) => {
    let response: any | null = null;
    let error: HttpErrorResponse | null = null;

    client.get('/test').subscribe(
      (res) => (response = res),
      (err) => (error = err),
      () => {
        expect(response).toBeFalsy();
        expect(error).toBeFalsy();
        done();
      }
    );

    const request = controller.expectOne('/test');
    request.flush('', { status: 400, statusText: 'Bad Request' });
  });
});
