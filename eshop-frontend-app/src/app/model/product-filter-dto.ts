export class ProductFilterDto {
  constructor(public namePattern: string,
              public minPrice: number,
              public maxPrice: number) {
  }
}
