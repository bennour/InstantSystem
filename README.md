# Instant Sytem Test

Dans le cadre d’un test technique, il m’a été demandé de fournir une API REST permettant à toute application mobile cliente d’afficher selon différents critères les parkings à proximités sur une map.


## Services REST
Pour répondre à la problématique posée, j’ai développé 2 webservices REST.
-   un premier qui retourne une liste des parkings à proximité, avec possibilité de filtre et trie selon certains critères
-   un second qui retourne le détails d’un parking.
    

  
  
### GET - /parkings
Service permettant de retourner la liste des parkings à proximité.
Il retourne une liste de ParkingVM (ie. VM = ViewModel), qui contient les informations nécessaires permettant d’identifier et placer un parking sur la map.

  

Il est par ailleurs possible de filtrer et/ou trier la liste selon certains critères :

-   **/parkings?sort=X&sortby=Y**  
Permet de trier la liste de manière X (=ASC ou DSC) selon l’attribut Y

-   **/parkings?onlyOpened=true**  
N’affiche que les parkings actuellement ouverts
    
-   **/parkings?minFree=X**  
    N’affiche que les parkings ayant au minimum X places de disponible
    
-   **/parkings?maxPrice=X&duration=Y**  
    N’affiche que les parkings offrant un prix maximum X pour une durée Y
    
### GET - /parkings/{id}

Service permettant de retourner le détails d’un parking. Il retourne un ParkingDetailsVM, qui hérite de ParkingVM, et qui contient de ce fait plus de détails. Ainsi en plus des informations de ParkingVM, on retrouve tous les tarifs et les horaires.

## AOP

Mise en place d’aspects au niveau des méthodes du parking controller, permettant de logger :
-   les services appelés
-   les arguments passés
-   le temps d'exécution
-   les données retournées
-   les messages d’erreurs en cas d’exceptions

## Architecture

Pour la réalisation de ce projet, j’ai opté pour une architecture n-tiers avec : une couche service permettant ici d’avoir une API Rest, mais il est envisageable d’avoir d’autres flux à ce niveau là en branchant différents adaptateurs (Webservice SOAP, JSF, Thymeleaf)

 Une couche business qui contient toutes les règles de gestions - dans le cas présent, le projet sert essentiellement de passe-plat et cette partie là n’est donc pas très étoffée. On peut néanmoins imaginer qu’il pourrait y avoir des évolutions qui nécessiterait d’avoir une telle couche.

 Une couche DAO - interface d’accès aux objets qui permet d’avoir autant de source de données que l’on souhaite. Ici nous avons une implémentation via un Rest Client mais il pourrait être envisageable de switcher directement sur une base de donnée.

## Améliorations

1.  L’utilisateur devrait renseigner sa positions GPS (longitude; latitude) afin qu’il puisse consulter les parkings autour de lui. Il pourrait par ailleurs ajouter un rayon de recherche maximal.
    
    **Example** : Un utilisateur situé à la position GPS (4.1215; 1.2135) et qui souhaite afficher les parkings autour de lui dans un rayon de 1km.  
**/parking/list?long=4.1215&lat=1.2135&radius=1000**

2.  L’application est monolithique pour des raisons de productivité mais on pourrait envisager de rendre le projet multimodulaire avec, par exemple, le package client Rest à part. Cela permettrait également de pouvoir switcher d’implémentation facilement comme précisé précédemment.
    
3.  Écrire les logs dans des fichiers journaliers avec logback


## Tests

J’ai réalisé quelques tests unitaires au niveau du parking controller et mapper afin de montrer leur importance. Ce n’est en aucun cas exhaustif, idéalement il faudrait tous les cas nominaux et aux limites.

Concernant les tests du controller, j’ai mocké les résultats de l’API afin de ne pas dépendre d’un service externe (disponibilité, données variantes)

## Installation

> Requirement : java 8 et maven

`mvn clean install`
  

## Exécution

Lancer la classe main avec la commande : `mvn spring-boot:run`
 
Utiliser swagger à l’adresse suivante :  [http:localhost:8080/swagger-ui.html](http:localhost:8080/swagger-ui.html)

## Outils

* [MapStruct](https://mapstruct.org/) - Mapping plugin
* [Swagger](https://swagger.io/) - API design tool
* [Lombok](https://projectlombok.org/) - Compiler plugin to generate getter/setter & more
* [Log4j ](https://logging.apache.org/log4j/2.x/) - Logging plugin
* [Maven](https://maven.apache.org/) - Dependency Management
* AspectJ - Aspect Oriented Programming

## Temps

Durée total réalisation (projet + document) : 6h

## Auteur

* **Salah BENNOUR** 
