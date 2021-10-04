import {Component, OnInit} from '@angular/core';
import {Order} from "../../model/order";
import {OrderService} from "../../services/order.service";

export const ORDERS_URL = 'order';

@Component({
  selector: 'app-order-page',
  templateUrl: './order-page.component.html',
  styleUrls: ['./order-page.component.scss']
})
export class OrderPageComponent implements OnInit {

  orders: Order[] = [];

  constructor(private orderService: OrderService) {
  }

  ngOnInit(): void {
    this.orderService.findOrdersByUser()
      .subscribe(orders => this.orders = orders, error => console.error(error))
  }

}
