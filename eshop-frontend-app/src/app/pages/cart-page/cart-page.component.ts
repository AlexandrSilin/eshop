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
    this.cartService.findAll().subscribe(response => this.content = response);
  }

}
