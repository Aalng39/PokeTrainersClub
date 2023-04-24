import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { PokemonService } from '../services/pokemon.service';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-pokemon-details',
  templateUrl: './pokemon-details.component.html',
  styleUrls: ['./pokemon-details.component.css']
})
export class PokemonDetailsComponent implements OnInit{
  
  params$!: Subscription
  result!: any
  authSubscription!: Subscription;

  constructor(private activatedRoute: ActivatedRoute, private pokemonSvc: PokemonService, 
                      private router: Router, public afAuth: AngularFireAuth, 
                      private messageService: MessageService){ }

  ngOnInit(): void {
    this.params$ = this.activatedRoute.params.subscribe(
      (params) => {
        const nameOrId = params['nameOrId']
        this.pokemonSvc.getPokemonDetails(nameOrId)
          .then(result => {
            console.info('>>> Pokemon Details: ', this.result = result)
          })
          .catch(error => {
            console.error('>> error: ', error)
            this.router.navigate(['/error', nameOrId]);
          })
      }
    )
    
    this.router.events.subscribe((event) => {
          if (!(event instanceof NavigationEnd)) {
              return;
          }
          window.scrollTo({ 
            top: 0, 
            left: 0, 
            behavior: 'smooth' 
          })
      });
  
  }
  
  visible!: boolean;

  showDialog() {
      this.visible = true;
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
            this.messageService.add({ severity: 'info', summary: 'Pokemon Is Already In Team', detail: name + ' is already in your team!', life: 1500})
          }else{
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

