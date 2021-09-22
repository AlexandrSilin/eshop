import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PRODUCT_GALLERY_URL, ProductGalleryPageComponent} from "./pages/product-gallery-page/product-gallery-page.component";

const routes: Routes = [
  {path: "", pathMatch: "full", redirectTo: PRODUCT_GALLERY_URL},
  {path: PRODUCT_GALLERY_URL, component: ProductGalleryPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
