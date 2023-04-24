import { Component, OnInit } from '@angular/core';
import { PokemonService } from '../services/pokemon.service';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { MessageService } from 'primeng/api';
import { Subscription } from 'rxjs';
import { NavigationEnd, Router } from '@angular/router';

@Component({
  selector: 'app-pokemon',
  templateUrl: './pokemon.component.html',
  styleUrls: ['./pokemon.component.css']
})
export class PokemonComponent implements OnInit{

  result! : any
  authSubscription!: Subscription;

  constructor(private pokemonSvc: PokemonService, public afAuth: AngularFireAuth, private router: Router, 
            private messageService: MessageService) {}

  ngOnInit(): void {
    this.pokemonSvc.getPokemonList(this.first).then(result => {
      console.log(">>> Results: ", this.result = result)
      })

    window.scrollTo({ 
      top: 0, 
      left: 0, 
      behavior: 'smooth' 
    })
  
  }

  first: number = 0;

  rows: number = 10;

  onPageChange(event: any) {
      this.first = event.first;
      this.rows = event.rows;
      this.ngOnInit();
      console.log(">>> RESULT: ", this.first)
  }

  bookmark(name: string, imageUrl: string , types: string[]){
    this.authSubscription = this.afAuth.authState.subscribe(user => {    
      if(user){ 
      this.pokemonSvc.logInPokemonTrainer(String(user?.email)).then(result => {
        const pokemonAdded = {
          username: result.username,
          pokemonName: name,
          pokemonUrl: imageUrl,
          pokemonTypes: types.toString()
          }
        console.log(pokemonAdded)
        
        this.pokemonSvc.postPokemon(pokemonAdded).then(result => {
          if(!result.inserted){
            console.log(result)
            console.log("Result returned pokemon in team: ", result.inserted)
            this.messageService.add({ severity: 'info', summary: 'Pokemon Is Already In Team', detail: name + ' is already in your team!', life: 1500})
          }else{
            console.log("Result returned pokemon add : ", result.inserted)
            this.messageService.add({ severity: 'success', summary: 'Successfully Added', detail: name + ' is added to your team!', life: 1500})
          }
          })
        })
        
      }else{
        this.messageService.add({ severity: 'warn', summary: 'Not Allowed', detail: 'Please login or registered an account to use our function!', life: 2000})
      }
      this.authSubscription.unsubscribe();
    })
  }
}
