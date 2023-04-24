import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  userLoggedIn: boolean

  constructor(private router: Router, private afAuth: AngularFireAuth, private messageService: MessageService) {
    this.userLoggedIn = false

    this.afAuth.onAuthStateChanged((user) => {
      if(user){
        this.userLoggedIn = true
      }
      else{
        this.userLoggedIn = false;
      } 
    })
   }

   signUpUser(user: any): Promise<any> {
    return this.afAuth.createUserWithEmailAndPassword(user.email, user.password)
    .then((result) => {
      let emailLower = user.email.toLowerCase()
      result.user?.sendEmailVerification()
    })
    .catch(error => {
      console.log('Auth Service: signup error')
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'This Email is already registered with an account. Please login!', life: 2000})
    })

   }

   logInUser(email: string, password: string): Promise<any> {
    return this.afAuth.signInWithEmailAndPassword(email, password)
    .then(() => {
      console.log('Auth Service: loginUser: success')
    })
    .catch(error => {
      console.log('Auth Service: login error...')
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Invalid Email or Password.', life: 2000})
    })
   }

}
