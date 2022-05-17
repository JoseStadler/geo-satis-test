import { fakeAsync, flush, flushMicrotasks, tick } from '@angular/core/testing';
import { transformObjectToFormData } from './util.functions';

describe('Util Functions', () => {
  let object = { name: 'Peter' };
  let blob: Blob;
  beforeEach(() => {
    blob = transformObjectToFormData(object);
  });

  it('transformObjectToFormData: should transform Object to Blob', () => {
    expect(blob).toBeTruthy();
  });

  it('transformObjectToFormData: should transform Object to Blob type application/json', () => {
    expect(blob.type)
      .withContext('Blob type does not match')
      .toBe('application/json');
  });

  it('transformObjectToFormData: should not change the object', (done) => {
    blob.text().then((jsonText) => {
      expect(JSON.parse(jsonText))
        .withContext('Blob text and object had to be equal')
        .toEqual(object);
      done();
    });
  });
});
