import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {PRODUCT_GALLERY_URL} from "../../pages/product-gallery-page/product-gallery-page.component";
import {ORDERS_URL} from "../../pages/order-page/order-page.component";
import {CART_URL} from "../../pages/cart-page/cart-page.component";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

  isProductGalleryPage: boolean = false;
  isOrderPage: boolean = false;
  isCartPage: boolean = false;

  constructor(private router: Router,
              public auth: AuthService) {
  }

  ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.isProductGalleryPage = event.url === '/' || event.url === '/' + PRODUCT_GALLERY_URL;
        this.isOrderPage = event.url === '/' + ORDERS_URL;
        this.isCartPage = event.url === '/' + CART_URL;
      }
    })
  }

  logout() {
    this.auth.logout();
    this.router.navigateByUrl("/");
  }

}
