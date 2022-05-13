export interface PaginatedList<T> {
  content: Array<T>;
  number: number;
  numberOfElements: number;
  size: number;
  totalElements: number;
  totalPages: number;
}
