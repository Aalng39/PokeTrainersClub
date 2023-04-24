export interface PokemonTrainer{
    username: string
    email: string
    password: string
}

export interface PokemonAdded{
    username: string
    pokemonName: string
    pokemonUrl: string
    pokemonTypes: string
}

export interface Post {
    title: string;
    content: string;
    username: string;
    imageUrl: string
    date: string;
}

export interface UserPost{
    username: string
    date: string
}
