import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Page} from "../model/page";
import {Picture} from "../model/picture";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) {
  }

  public findAll() {
    return this.http.get<Page>('/api/v1/products/all').toPromise();
  }

  public getMainPicture(id: number) {
    return this.http.get<Picture>("http://127.0.0.1:9090/api/v1/pictures/" + id).toPromise();
  }
}
