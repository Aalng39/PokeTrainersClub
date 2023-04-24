DROP DATABASE IF EXISTS pokemon;

CREATE DATABASE pokemon;

USE pokemon;

create table pokemontrainers (
    username varchar(32) not null,
    email varchar(128) not null,
    password varchar(32) not null,
    image varchar(512) not null,
    date date not null,
    
    primary key(email)

);

create table pokemonteam (

    entry int not null AUTO_INCREMENT,
    pokemon_name varchar(128) not null,
    pokemon_url varchar(512) not null,
    pokemon_type varchar(128) not null,
    username varchar(32) not null,
    
    primary key(entry),
    unique index unique_pokemon (pokemon_name, username)
);

create table pokemonforums (

    entry int not null AUTO_INCREMENT,
    title varchar(512) not null,
    content text not null,
    username varchar(32) not null,
    image varchar(512) not null,
    date datetime not null,
    
    primary key(entry),
    unique index unique_post (date, username)

);
