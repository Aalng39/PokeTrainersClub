import { ChangeDetectorRef, Component, ElementRef, OnChanges, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import { Router } from '@angular/router';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { MenuItem } from 'primeng/api';
import { PokemonService } from './services/pokemon.service';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnChanges {
  title = 'client';

  nameOrId! : string
  userLoggedIn: boolean = false
  username!: string
  email: string = ""
  items: MenuItem[] = [];
  authSubscription!: Subscription;
  handler: any
  
  constructor(private router: Router, public afAuth: AngularFireAuth,
            private cdr: ChangeDetectorRef, private pokemonSvc: PokemonService){ }
    

    ngOnInit(): void {
        this.authSubscription = this.afAuth.authState.subscribe(user => {
            console.log("current user logged in >")
            console.log(user?.email)   
            this.userLoggedIn = !!user;
            this.email = String(user?.email);
            if(user){
            this.pokemonSvc.logInPokemonTrainer(this.email).then(result => {
                console.log("username form sql > " + result.username)
                this.username = result.username; 
                
            this.items.forEach(item => {
                if (item.label === 'Welcome...') {
                  item.label = 'Welcome ' + this.username;

                }})
            }).catch(error => {
                console.log(error)
                this.ngOnInit()
            });
            
            }
  
            this.items = [
                    {
                        label: 'Home',
                        icon: 'pi pi-fw pi-home',
                        command: () => {
                        this.router.navigate(["/"])
                        }
                    },
                    {
                        label: 'Types',
                        icon: 'pi pi-fw pi-bars',
                        items: [ 'Normal', 'Fire', 'Water', 'Grass', 'Electric',
                            'Ice', 'Fighting', 'Poison', 'Ground', 'Flying', 'Psychic',
                            'Bug', 'Rock', 'Ghost', 'Dark', 'Dragon', 'Steel', 'Fairy'
                            ].map(type => ({
                                label: type,
                                command: () => {
                                this.router.navigate(['/types', type]);
                                }
                            }))
                    },
                    {
                        label: 'My Team',
                        icon: 'pi pi-fw pi-bookmark',
                        command: () => {
                            this.router.navigate(["/myteam"])
                            }
                    },
                    {
                        label: 'Forum',
                        icon: 'pi pi-fw pi-book',
                        command: () => {
                            this.router.navigate(["/forum"])
                            }
                    },
                    { 
                        label: 'Login',
                        icon: 'pi pi-fw pi-sign-in',
                        visible: !this.userLoggedIn,
                        command: () => {
                            this.router.navigate(["/login"])
                            }
                        
                    },
                    
                    {
                        label: 'Welcome...',
                        icon: 'pi pi-fw pi-id-card',
                        visible: this.userLoggedIn && !!this.email,
                        items: [
                            {
                                label: "Trainer's Profile",
                                icon: 'pi pi-fw pi-user',
                                visible: this.userLoggedIn,
                                command: () => {
                                    this.router.navigate(["/profile"])
                                    }
                            },
                            {
                                label: 'Donate',
                                icon: 'pi pi-fw pi-credit-card',
                                visible: this.userLoggedIn && !!this.email,
                                command: () => {
                                    this.router.navigate(["/donation"])
                                    }
                            },
                            {
                            label: 'Logout',
                            icon: 'pi pi-fw pi-sign-out',
                            visible: this.userLoggedIn,
                            command: () => {
                                this.afAuth.signOut()
                                this.router.navigate(["/login"])
                                }
                            }
                        ],
                    }
                ]
            })
            this.cdr.detectChanges(); 
  
    }

    ngOnChanges(changes: SimpleChanges): void {
        throw new Error('Method not implemented.');
    }

  

    onKeyDown(event: any){
        this.router.navigate(['/pokemon/' , this.nameOrId])
        this.nameOrId = ''
    }

}
