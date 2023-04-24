import { Component, OnInit } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { Router } from '@angular/router';
import { render } from 'creditcardpayments/creditCardPayments'
import { PokemonService } from '../services/pokemon.service';

@Component({
  selector: 'app-donation',
  templateUrl: './donation.component.html',
  styleUrls: ['./donation.component.css']
})
export class DonationComponent implements OnInit {

  
    constructor() { }

  ngOnInit(): void {
    document.body.className = "selector"
    render({
          id: "#myPaypalButtons",
          currency: "SGD",
          value: "5.00",
          onApprove: (details) => {
            alert("Transaction Successful")
          }
        }) 
  }

  ngOnDestroy(){
    document.body.className=""
  }

}
