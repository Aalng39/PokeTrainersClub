import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { PokemonComponent } from './components/pokemon.component';
import { PokemonService } from './services/pokemon.service';
import { PokemonDetailsComponent } from './components/pokemon-details.component';
import { PrimeNgModule } from './primeng.module';
import { PokemonTypesComponent } from './components/pokemon-types.component';
import { PokemonMyteamComponent } from './components/pokemon-myteam.component';
import { PokemonForumComponent } from './components/pokemon-forum.component';
import { LogInComponent } from './components/log-in.component';
import { AngularFireModule } from '@angular/fire/compat';
import { environment } from '../environments/environment';
import { AuthGuard } from './services/auth.guard';
import { ConfirmationService, MessageService } from 'primeng/api';
import { SearchErrorComponent } from './components/search-error.component';
import { UserProfileComponent } from './components/user-profile.component';
import { ForumPostComponent } from './components/forum-post.component';
import { DonationComponent } from './components/donation.component';

const appRoutes: Routes = [
  { path: '', component: PokemonComponent },
  { path: 'login', component: LogInComponent },
  { path: 'pokemon/:nameOrId', component: PokemonDetailsComponent },
  { path: 'types/:type', component: PokemonTypesComponent},
  { path: 'myteam', component: PokemonMyteamComponent, canActivate: [AuthGuard]},
  { path: 'forum', component: PokemonForumComponent},
  { path: 'error/:nameOrId', component: SearchErrorComponent},
  { path: 'forum/posts', component: ForumPostComponent, canActivate: [AuthGuard]},
  { path: 'profile', component: UserProfileComponent,  canActivate: [AuthGuard]},
  { path: 'donation', component: DonationComponent,  canActivate: [AuthGuard]},
  { path: '**', redirectTo: '/', pathMatch: 'full' }
]

@NgModule({
  declarations: [
    AppComponent,
    PokemonComponent,
    PokemonDetailsComponent,
    PokemonTypesComponent,
    PokemonMyteamComponent,
    PokemonForumComponent,
    LogInComponent,
    SearchErrorComponent,
    UserProfileComponent,
    ForumPostComponent,
    DonationComponent,
    
  ],
  imports: [
    
    BrowserModule, 
    BrowserAnimationsModule,
    PrimeNgModule, 
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes, { useHash : true }),
    AngularFireModule.initializeApp(environment.firebase)

  ],
  providers: [PokemonService, MessageService, ConfirmationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
