import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs'
import { PokemonAdded, PokemonTrainer, Post, UserPost } from '../models';

@Injectable({
  providedIn: 'root'
})
export class PokemonService {

  constructor(private http: HttpClient) { }

  public getPokemonList(offset: number): Promise<any>{

	const params = new HttpParams()
	.set('offset', offset);

		return firstValueFrom(
		  this.http.get<any>("pokemon", {params: params}))
	}

  public getPokemonDetails(nameOrId: string): Promise<any>{

		return firstValueFrom(
		  this.http.get<any>(`pokemon/${nameOrId}`))
	  }
	
  public getPokemonListByType(type: string, offset: number): Promise<any>{

	const params = new HttpParams()
	.set('offset', offset);

		return firstValueFrom(
		  this.http.get<any>(`pokemon/types/${type}`, {params: params}))
	  }
	
  public postPokemonTrainer(formData: FormData): Promise<any>{
	return firstValueFrom(
		this.http.post<any>("pokemon/user", formData))
		.then(result => {
			console.info('>>> after signup: ', result)
		  })
	}

  public logInPokemonTrainer(email: string): Promise<any>{
	return firstValueFrom(
		this.http.put<any>("pokemon/user", email))
	}

  public postPokemon(pokemonAdded: PokemonAdded): Promise<any>{
	return firstValueFrom(
		this.http.post<any>("pokemon/myteam/added", pokemonAdded))
	}

  public getPokemonTeam(username: string): Promise<any>{

	const params = new HttpParams()
	.set('username', username);

		return firstValueFrom(
			this.http.get<any>("pokemon/myteam", {params: params}))
	}

  public deletePokemon(pokemonAdded: PokemonAdded): Promise<any>{

	return firstValueFrom(
		this.http.put<any>("pokemon/myteam/removed", pokemonAdded))
	}

  public deleteUser(pokeTrainer: PokemonTrainer): Promise<any>{

	return firstValueFrom(
		this.http.put<any>("pokemon/deleteUser", pokeTrainer))
	}

  public getForumEntries(): Promise<any>{

		return firstValueFrom(
			this.http.get<any>("pokemon/forum"))
	}

  public addPost(post: Post): Promise<any>{

		return firstValueFrom(
			this.http.post<any>("pokemon/forum", post))
	}

  public getForumEntriesByUser(username: string): Promise<any>{

	const params = new HttpParams()
	.set('username', username);

		return firstValueFrom(
			this.http.get<any>("pokemon/forum/user", {params: params}))
	}

  public deletePost(userPost: UserPost): Promise<any>{

	return firstValueFrom(
		this.http.put<any>("pokemon/forum/user", userPost))
	}
}

