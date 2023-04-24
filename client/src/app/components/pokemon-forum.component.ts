import { Component, OnInit } from '@angular/core';
import { Post } from '../models';
import { PokemonService } from '../services/pokemon.service';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { MessageService } from 'primeng/api';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription, delay } from 'rxjs';

interface Comment {
  author: string;
  text: string;
}

@Component({
  selector: 'app-pokemon-forum',
  templateUrl: './pokemon-forum.component.html',
  styleUrls: ['./pokemon-forum.component.css']
})
export class PokemonForumComponent implements OnInit {
  
posts! : Post[]

displayDialog: boolean = false;
postForm!: FormGroup
authSubscription!: Subscription;
email!: string

constructor(private fb: FormBuilder, private pokemonSvc: PokemonService, public afAuth: AngularFireAuth,
  private messageService: MessageService) {}
  
  ngOnInit(): void {
    // document.body.className = "selector"
    this.pokemonSvc.getForumEntries().then(result => {
      this.posts = result
    })
    this.postForm = this.createPostingForm()
    }

    ngOnDestroy(){
      document.body.className=""
    }

  showDialog() {
    this.authSubscription = this.afAuth.authState.subscribe(user => {    
      if(user){
        this.displayDialog = true;
        this.email = String(user?.email)
      }else{
        this.messageService.add({ severity: 'warn', summary: 'Not Allowed', detail: 'Please login or registered an account to use our function!', life: 2000})
        this.authSubscription.unsubscribe();
      }
    }) 
  }

  hideAddPostDialog() {
    this.displayDialog = false;
  }

  addPost() {
    console.log(this.email)
    this.pokemonSvc.logInPokemonTrainer(this.email).then(result => {
      console.log(result.imageUrl)
        const post = {
          title: this.postForm.value.title,
          content: this.postForm.value.content,
          username: result.username,
          imageUrl: result.imageUrl,
          date: ''
          }
        console.log(post)
        
        this.pokemonSvc.addPost(post).then(result => {
          if(result.inserted){
            console.log(result)

            this.hideAddPostDialog();
            this.messageService.add({ severity: 'success', summary: 'Successfully Post', detail: 'You just posted something!', life: 2000})
            
          }else{
            this.messageService.add({ severity: 'warn', summary: 'Unsuccessful', detail: 'Something went wrong...', life: 2000})
          }
          })
        })
        this.authSubscription.unsubscribe();
  }

  private createPostingForm(): FormGroup {
    return this.fb.group({
      title: this.fb.control<string>('', [ Validators.required ]),
      content: this.fb.control<string>('', [ Validators.required ]),
    })
  } 
}
