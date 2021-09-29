import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NavBarComponent} from './components/nav-bar/nav-bar.component';
import {FooterComponent} from './components/footer/footer.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {ProductFilterComponent} from './components/product-filter/product-filter.component';
import {PaginationComponent} from './components/pagination/pagination.component';
import {ProductGalleryComponent} from './components/product-gallery/product-gallery.component';
import {ProductGalleryPageComponent} from './pages/product-gallery-page/product-gallery-page.component';
import {OrderPageComponent} from './pages/order-page/order-page.component';
import {CartPageComponent} from './pages/cart-page/cart-page.component';
import {LoginPageComponent} from './pages/login-page/login-page.component';
import {RegisterPageComponent} from './pages/register-page/register-page.component';
import {ProductInfoPageComponent} from './pages/product-info-page/product-info-page.component';

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    FooterComponent,
    ProductFilterComponent,
    PaginationComponent,
    ProductGalleryComponent,
    ProductGalleryPageComponent,
    OrderPageComponent,
    CartPageComponent,
    LoginPageComponent,
    RegisterPageComponent,
    ProductInfoPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
