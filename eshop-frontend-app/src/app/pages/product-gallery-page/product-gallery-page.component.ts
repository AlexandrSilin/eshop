import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../services/product.service";
import {Product} from "../../model/product";
import {ProductFilterDto} from "../../model/product-filter-dto";
import {Page} from "../../model/page";

export const PRODUCT_GALLERY_URL = 'product';

@Component({
  selector: 'app-product-gallery-page',
  templateUrl: './product-gallery-page.component.html',
  styleUrls: ['./product-gallery-page.component.scss']
})
export class ProductGalleryPageComponent implements OnInit {

  products: Product[] = [];
  isError: boolean = false;
  productFilter?: ProductFilterDto;
  page?: Page;
  pageNumber: number = 1;

  constructor(public productService: ProductService) {
  }

  ngOnInit(): void {
    this.productService.findAll()
      .subscribe(res => {
        console.log("Loading products");
        this.page = res;
        this.products = res.content;
      }, err => {
        console.log(`Can't load products ${err}`);
      });
  }

  filterApplied($event: ProductFilterDto) {
    console.log($event);
    this.productFilter = $event;
    this.productService.findAll($event, 1).subscribe(res => {
      this.page = res;
      this.products = res.content;
      this.pageNumber = 1;
    }, error => console.error(`Can't load products ${error}`));
  }

  goToPage($event: number) {
    console.log($event);
    this.productService.findAll(this.productFilter, $event).subscribe(res => {
      this.page = res;
      this.products = res.content;
      this.pageNumber = res.number + 1;
    }, error => console.error(`Can't load products ${error}`));
  }
}
