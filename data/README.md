# txt database format
Duration -> minutes\
imdb rating -> double\
allowed genre values ->  
ACTION |  ADULT |  ADVENTURE |  ANIMATION |  BIOGRAPHY |  COMEDY |  CRIME |  DOCUMENTARY |  DRAMA |  FAMILY 
|  FANTASY |  FILM_NOIR |  GAME_SHOW |  HISTORY | HORROR | MUSICAL | MUSIC | MYSTERY | ROMANCE | SCI_FI | 
SPORT | THRILLER | WAR | WESTERN \
Allowed gender values (male | female) -> M | F \
Allowed race values -> CAUCASOID | NEGROID | MONGOLOID | ASIAN | PACIFIC_ISLAND_AND_AUSTRALIAN | AMERINDIANS_AND_ESKIMOS

## Actors.txt
1,Marlon,Brando,1924-03-04,M,CAUCASOID
> id,firstname,lastname,birthdate,gender,race
## Directors.txt
13,Jon,Watts,1981-06-28,M,Spider-Man: No Way Home|Spider-Man: Homecoming|Spider-Man: Far From Home 
> id,firstname,lastname,birthdate,gender,knownForMedia|knownForMedia22
## Movies.txt
17,Edge of Tomorrow,2014,SCI_FI,113,Doug Liman,7.9,Tom Cruise,1:8|2:8|3:8|4:8|5:8|7:7
> id,title,release year,genre,duration,Director,IMDb rating,Protagonist,userId:userRating|userId:userRating
## Series.txt
8,Alice in the Borderland,THRILLER,1:7|2:8|3:9|5:8|7:10
> id,title,genre,userId:userRating|userId:userRating
## Seasons.txt
11,4,1,2022
> seasonId,seriesId,seasonNumber,release year
## Episodes.txt
228,26,51,Matt Duffer,7.8,Winona Ryder
> episodeId,seasonId,duration,Director,IMDb rating,protagonist
