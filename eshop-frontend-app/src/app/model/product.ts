import {Category} from "./category";
import {Brand} from "./brand";
import {Picture} from "./picture";
import {HttpClient} from "@angular/common/http";

export class Product {

  constructor(public id: number,
              public title: string,
              public price: number,
              public description: string,
              public category: Category,
              public brand: Brand,
              public pictures: number[],
              public http: HttpClient,
              public picturesObj: Picture) {
    this.http.get<Picture>("http://127.0.0.1:9090/api/v1/pictures/" + pictures[0]).toPromise()
      .then(response => {
        this.picturesObj = response;
      });
  }
}
