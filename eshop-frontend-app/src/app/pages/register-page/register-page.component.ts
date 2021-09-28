import { Component, OnInit } from '@angular/core';

export const REGISTER_URL = 'register'

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.scss']
})
export class RegisterPageComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
