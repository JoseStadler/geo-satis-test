export function transformObjectToFormData(jsonObject: {
  [key: string]: any;
}): Blob {
  return new Blob([JSON.stringify(jsonObject)], { type: 'application/json' });
}
