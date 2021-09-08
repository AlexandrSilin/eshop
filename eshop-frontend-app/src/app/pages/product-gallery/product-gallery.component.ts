import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../services/product.service";
import {Product} from "../../model/product";

export const PRODUCT_GALLERY_URL = 'product';

@Component({
  selector: 'app-product-gallery',
  templateUrl: './product-gallery.component.html',
  styleUrls: ['./product-gallery.component.scss']
})
export class ProductGalleryComponent implements OnInit {

  products: Product[] = [];
  isError: boolean = false;

  constructor(public productService: ProductService) {
  }

  ngOnInit(): void {
    this.retrieveProduct()
  }

  private retrieveProduct() {
    this.productService.findAll().then(response => {
      this.products = response.content;
      this.products.map(product => {
        this.productService.getMainPicture(product.pictures[0])
          .then(response => {
            product.picturesObj = response;
          });
      })
    }).catch(err => {
      this.isError = true;
      console.error(err)
    });
  }

}
