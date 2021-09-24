import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {
  PRODUCT_GALLERY_URL,
  ProductGalleryPageComponent
} from "./pages/product-gallery-page/product-gallery-page.component";
import {CART_URL, CartPageComponent} from "./pages/cart-page/cart-page.component";
import {OrderPageComponent, ORDERS_URL} from "./pages/order-page/order-page.component";

const routes: Routes = [
  {path: "", pathMatch: "full", redirectTo: PRODUCT_GALLERY_URL},
  {path: PRODUCT_GALLERY_URL, component: ProductGalleryPageComponent},
  {path: CART_URL, component: CartPageComponent},
  {path: ORDERS_URL, component: OrderPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
