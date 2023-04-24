import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { PokemonTrainer } from '../models';
import { PokemonService } from '../services/pokemon.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})
export class LogInComponent implements OnInit{

  @ViewChild('file') imageFile!: ElementRef;
  
  signUpForm!: FormGroup
  loginForm!: FormGroup
  firebaseErrorMessage: string;
  pokeTrainer!: PokemonTrainer

  constructor(private fb: FormBuilder, private pokemonSvc: PokemonService, private authService: AuthService, 
              private router: Router, private messageService: MessageService) {
                this.firebaseErrorMessage = ""
              }

  ngOnInit(): void {
      document.body.className = "selector"
      this.signUpForm = this.createSignUpForm()
      this.loginForm = this.createLoginForm()
    }
    ngOnDestroy(){
      document.body.className=""
    }

  processSignUpForm() {
    const formData = new FormData()
    if(this.signUpForm.invalid){
      return;
    }
    this.authService.signUpUser(this.signUpForm.value).then((result: any) => {
      if(result == null){
      formData.set('username', this.signUpForm.value.username)
      formData.set('email', this.signUpForm.value.email)
      formData.set('password', this.signUpForm.value.password)
      formData.set('image', this.imageFile.nativeElement.files[0])
      
      this.router.navigate(['/'])
      console.log('>>> Form Data: ', formData)
      this.pokemonSvc.postPokemonTrainer(formData)
        .then(results => {
          
          this.messageService.add({ severity: 'success', summary: 'Successfully Registered', detail: 'Hello ' + this.signUpForm.value.username + ', your acccount is registered', life: 2000})
        }).catch(error => {
          console.log("Same primary key used...")
          this.signUpForm.reset()
        })

      
      }else if(result.isValid == false){
      this.firebaseErrorMessage = result.message
    }
    }).catch(() => {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Your email is already registered with an account. Please login!', life: 2000})
    })
  }

  processLoginForm() {
    this.pokemonSvc.logInPokemonTrainer(this.loginForm.value.email)

    if(this.loginForm.invalid){
      return;
    }
    this.authService.logInUser(this.loginForm.value.email, this.loginForm.value.password).then((result: any) => {
      if(result == null){
        console.log('logging in...')
        this.pokemonSvc.logInPokemonTrainer(this.loginForm.value.email).then(result => {
        this.router.navigate(['/profile'])
        this.messageService.add({ severity: 'success', summary: 'Successfully Logged in', detail: 'Welcome Back ' + result.username, life: 2000})
        }).catch(() => {
          this.messageService.add({ severity: 'error', summary: 'Login Failed', detail: 'Invalid Username or Password! Please check!', life: 2000})
          console.log("user not found... ")
          this.loginForm.reset()
        })
      }
      else if(result.isValid == false){

      this.firebaseErrorMessage = result.message
      }
    }).catch(() => {
      console.log("login failed")
      
    })
    console.info('>> form: ', this.loginForm.value);
  }

  private createSignUpForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control<string>('', [ Validators.required, Validators.minLength(3), Validators.maxLength(20)]),
      email: this.fb.control<string>('', [ Validators.required, Validators.email]),
      password: this.fb.control<string>('', [ Validators.required, Validators.minLength(8), Validators.maxLength(20)]),
      image: this.fb.control('', [Validators.required])
    })
  } 
  
  private createLoginForm(): FormGroup {
    return this.fb.group({
      // username: this.fb.control<string>('', [ Validators.required, Validators.minLength(3), Validators.maxLength(20)]),
      email: this.fb.control<string>('', [ Validators.required, Validators.email]),
      password: this.fb.control<string>('', [ Validators.required, Validators.minLength(8), Validators.maxLength(20)]),
    })
  } 
}
