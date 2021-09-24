import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AddLineItemDto} from "../model/add-line-item-dto";
import {Observable} from "rxjs";
import {AllCartDto} from "../model/all-cart-dto";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(private http: HttpClient) {
  }

  public findAll(): Observable<AllCartDto> {
    return this.http.get<AllCartDto>('/api/v1/cart/all');
  }

  public addToCart(dto: AddLineItemDto) {
    return this.http.post('/api/v1/cart', dto);
  }

  clearCart() {
    return this.http.delete('/api/v1/cart');
  }

  deleteItem(productId: number) {
    this.http.delete(`/api/v1/cart/${productId}`)
  }

  recalculationOfCost(productId: number, qty: number | undefined) {
    return this.http.post(`/api/v1/cart/${productId}`, qty);
  }
}
