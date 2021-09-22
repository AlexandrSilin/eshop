import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NavBarComponent} from './components/nav-bar/nav-bar.component';
import {FooterComponent} from './components/footer/footer.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import { ProductFilterComponent } from './components/product-filter/product-filter.component';
import { PaginationComponent } from './components/pagination/pagination.component';
import { ProductGalleryComponent } from './components/product-gallery/product-gallery.component';
import { ProductGalleryPageComponent } from './pages/product-gallery-page/product-gallery-page.component';

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    FooterComponent,
    ProductFilterComponent,
    PaginationComponent,
    ProductGalleryComponent,
    ProductGalleryPageComponent
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
