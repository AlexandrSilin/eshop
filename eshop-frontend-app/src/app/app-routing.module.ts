import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {
  PRODUCT_GALLERY_URL,
  ProductGalleryPageComponent
} from "./pages/product-gallery-page/product-gallery-page.component";
import {CART_URL, CartPageComponent} from "./pages/cart-page/cart-page.component";
import {OrderPageComponent, ORDERS_URL} from "./pages/order-page/order-page.component";
import {AuthGuard} from "./helpers/auth-guard";
import {LOGIN_URL, LoginPageComponent} from "./pages/login-page/login-page.component";
import {REGISTER_URL, RegisterPageComponent} from "./pages/register-page/register-page.component";
import {PRODUCT_INFO_URL, ProductInfoPageComponent} from "./pages/product-info-page/product-info-page.component";

const routes: Routes = [
  {path: "", pathMatch: "full", redirectTo: PRODUCT_GALLERY_URL},
  {path: PRODUCT_GALLERY_URL, component: ProductGalleryPageComponent},
  {path: PRODUCT_INFO_URL, component: ProductInfoPageComponent},
  {path: CART_URL, component: CartPageComponent},
  {path: ORDERS_URL, component: OrderPageComponent, canActivate: [AuthGuard]},
  {path: LOGIN_URL, component: LoginPageComponent},
  {path: REGISTER_URL, component: RegisterPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
