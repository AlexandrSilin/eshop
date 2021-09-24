import {Component, OnInit} from '@angular/core';
import {CartService} from "../../services/cart.service";
import {AllCartDto} from "../../model/all-cart-dto";

export const CART_URL = 'cart'

@Component({
  selector: 'app-cart-page',
  templateUrl: './cart-page.component.html',
  styleUrls: ['./cart-page.component.scss']
})
export class CartPageComponent implements OnInit {
  content?: AllCartDto;

  constructor(private cartService: CartService) {
  }

  ngOnInit(): void {
    this.findAll()
  }

  clearCart() {
    this.cartService.clearCart().subscribe();
    this.content = undefined;
  }

  deleteItem(productId: number) {
    this.cartService.deleteItem(productId)
    this.findAll()
  }

  findAll() {
    this.cartService.findAll().subscribe(response => this.content = response);
  }

  recalculationOfCost(productId: number, newQty: string) {
    this.cartService.recalculationOfCost(productId, +newQty).subscribe();
    this.findAll()
  }
}
