import { Component } from '@angular/core';
import { Post } from '../models';
import { Subscription } from 'rxjs';
import { PokemonService } from '../services/pokemon.service';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-forum-post',
  templateUrl: './forum-post.component.html',
  styleUrls: ['./forum-post.component.css']
})
export class ForumPostComponent {

  posts! : Post[]

  authSubscription!: Subscription;
  email!: string

  constructor(private pokemonSvc: PokemonService, public afAuth: AngularFireAuth,
    private messageService: MessageService, private confirmationService: ConfirmationService,) {}
    
  ngOnInit(): void {
    this.authSubscription = this.afAuth.authState.subscribe(user => {
      if(user){ 
        this.pokemonSvc.logInPokemonTrainer(String(user.email)).then(result => {
          this.pokemonSvc.getForumEntriesByUser(result.username).then(result => {
            this.posts = result;
            console.log(this.posts)
          })
          })
        }
    }) 
  }

  deletePost(username: string, date: string){
    this.confirmationService.confirm({
      message: 'Do you want to delete this post?',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => {      
                const userPost = {
                  username: username,
                  date: date
                }
                this.pokemonSvc.deletePost(userPost).then(result => {
                  console.log(result)
                  if(result.deleted){
                  this.messageService.add({ severity: 'success', summary: 'Successfully Deleted', detail: 'Post deleted.', life: 2000 });
                  }else{
                    this.messageService.add({ severity: 'warn', summary: 'Unsuccessful', detail: 'Something went wrong... this post cannot be deleted.', life: 2000 });
                  }
                  this.ngOnInit()
                }).catch(error => {
                this.messageService.add({ severity: 'warn', summary: 'Unsuccessful', detail: 'Something went wrong... this post cannot be deleted.', life: 2000 });
              })
              this.authSubscription.unsubscribe()

            } 
    });
  }

}
