import { Component, OnInit } from '@angular/core';

export const PRODUCT_INFO_URL = "product/:id"

@Component({
  selector: 'app-product-info-page',
  templateUrl: './product-info-page.component.html',
  styleUrls: ['./product-info-page.component.scss']
})
export class ProductInfoPageComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
