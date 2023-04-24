import { Component, OnInit } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { PokemonService } from '../services/pokemon.service';
import { MessageService } from 'primeng/api';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-pokemon-myteam',
  templateUrl: './pokemon-myteam.component.html',
  styleUrls: ['./pokemon-myteam.component.css']
})
export class PokemonMyteamComponent implements OnInit {
  
  result: any
  authSubscription!: Subscription;

  constructor(private pokemonSvc: PokemonService, public afAuth: AngularFireAuth, 
              private messageService: MessageService) {}

  ngOnInit(): void {
    this.afAuth.authState.subscribe(user => {    
      if(user){ 
      this.pokemonSvc.logInPokemonTrainer(String(user.email)).then(result => {
        this.pokemonSvc.getPokemonTeam(result.username).then(result => {
          this.result = result;
          console.log(this.result)
        })
        })
      }else{
        console.log("no user login")
      }
    })
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
        
        this.pokemonSvc.deletePokemon(pokemonAdded).then(result => {
          if(result.deleted){
            this.messageService.add({ severity: 'success', summary: 'Successfully Removed', detail: name + ' is removed from your team!', life: 1500})
          }else{
            this.messageService.add({ severity: 'warn', summary: 'Unsuccessful', detail: 'Something went wrong...', life: 1500})
          }
          
          this.ngOnInit()
          })
          this.authSubscription.unsubscribe()         
        })
        
      }else{
        console.log(name, imageUrl, types)
      }
    })
  }
}

function delay(ms: number) {
  return new Promise( resolve => setTimeout(resolve, ms) );
}
