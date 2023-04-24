import { Component, OnInit } from '@angular/core';
import { PokemonService } from '../services/pokemon.service';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { MessageService } from 'primeng/api';
import { AngularFireAuth } from '@angular/fire/compat/auth';

@Component({
  selector: 'app-pokemon-types',
  templateUrl: './pokemon-types.component.html',
  styleUrls: ['./pokemon-types.component.css']
})
export class PokemonTypesComponent implements OnInit{

  params$!: Subscription
  result!: any
  authSubscription!: Subscription;

  constructor(private activatedRoute: ActivatedRoute, private pokemonSvc: PokemonService,
              public afAuth: AngularFireAuth, private messageService: MessageService){ }
  
  ngOnInit(): void {
    this.params$ = this.activatedRoute.params.subscribe(
      (params) => {
        const type = params['type']
        this.pokemonSvc.getPokemonListByType(type, this.first)
          .then(result => {
            console.info('>>> Result: ', this.result = result)
          })
          .catch(error => {
            console.error('>> error: ', error)
          })
      }
    )
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
        };
        console.log(pokemonAdded)
        this.pokemonSvc.postPokemon(pokemonAdded).then(result => {
          if(!result.inserted){
            this.messageService.add({ severity: 'info', summary: 'Pokemon Is Already In Team', detail: name + ' is already in your team!' , life: 1500})
          }else{
            this.messageService.add({ severity: 'success', summary: 'Successfully Added', detail: name + ' is added to your team!' , life: 1500})
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
