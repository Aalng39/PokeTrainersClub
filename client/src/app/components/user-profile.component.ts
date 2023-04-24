import { Component, OnInit } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { PokemonService } from '../services/pokemon.service';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { ConfirmEventType, ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit{

username!: string
email!: string
image!: string
date!: string

  constructor(private activatedRoute: ActivatedRoute, private pokemonSvc: PokemonService, 
     public afAuth: AngularFireAuth, private confirmationService: ConfirmationService, 
     private messageService: MessageService) { } 
    
  ngOnInit(): void {
    document.body.className = "selector"
    this.afAuth.authState.subscribe(user => {    
    if(user){ 
      this.pokemonSvc.logInPokemonTrainer(String(user.email)).then(result => {
        console.log(result)
        this.username = result.username
        this.email = result.email
        this.image = result.imageUrl
        this.date = result.date
      })
     }
    })
  }

  ngOnDestroy(){
    document.body.className=""
  }

  deleteAccount(){
    this.confirmationService.confirm({
      message: 'Do you want to delete this account?',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => {
          this.afAuth.currentUser.then(user => {
            console.log(user?.email)
            user?.delete().then(result => {
              console.log(result)
              const pokemonTrainer = {
                username: this.username,
                email: this.email,
                password: ''
              }
              this.pokemonSvc.deleteUser(pokemonTrainer).then(result => {
                this.messageService.add({ severity: 'success', summary: 'Successfully Deleted', detail: 'Account ' + this.username +' deleted.', life: 2000 });
                console.log(result)
              }).catch(error => {
              this.messageService.add({ severity: 'warn', summary: 'Unsuccessful', detail: 'Something went wrong... Account ' + this.username +' cannot be deleted.', life: 2000 });
            })
            }).catch(() => {
              this.afAuth.signOut()
              this.messageService.add({ severity: 'warn', summary: 'Session Ended', detail: 'Please login again to delete your account.', life: 2000 });
            })
            })
      } 
    });
  }
}

