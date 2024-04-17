## GROUPPROJECT BACKEND

contents:
- resource-server (spring oauth2 resource server, only the bones)
- authorization-server(spring oauth2 authorization server, only the bones, some configuration to work with the [frontend](https://github.com/Attila732/Onkormanyzat_))
- filter_readability_thingy "prettifyer" for the spring securityFilterchain logs


REQUIREMENTS:
- JAVA 21
- Maven


to run:
- start authorization server VSCode run / terminal: ./mvnw spring-boot:run
- start resource server VSCode run / terminal: ./mvnw spring-boot:run
- start [frontend](https://github.com/Attila732/Onkormanyzat_) see the README 

PORTS:
- authorization server: 8083
- resource server: 8082
- frontend server: 8081


## Temporary But Important!!

To use the image upload option change the root Path in the resource server 
src\main\java\hu\project\groupproject\resourceserver\myabstractclasses\LoadableImages.java
to a path on your computer to avoid the images being created/saved in random places

Docs:

authorization-server:
átmeneti authentikáció és authorizációs szerver, production rendszerben helyettesítendő pl:Google, Ügyfélkapu vagy más authentikációval.


Jelenleg Oauth2 Jwt-vel működik


resource-server:

adatbázis: H2-development adatbázis mariadb Driverrel, production rendszerben kicserélhető a dependency kicserélésével, és a datasource átírásával


config:
- DataConfig: adatbázis kapcsolat beállítása
- SecurityConfig: auth beállítások

controllers: végpontokat tartalmazó java filek, témák/tárgyak/entity-k alapján felbontva

CustomAuthThings: authentikációhoz és authorizációhoz szükséges saját classok

dtos: (data transfer objects) kimenő és beérkező illetve metódusok között

entities: 
- nonsoftdeletable: jelenleg nem használt, adatbázisban adattárolás optimalizációjához lettek volna
- softdeletable: azok az entity-k (egyedek) amiket az adatbázisban tárolni akarunk, hibernate ORM/jakarta JPA mappings. törlés esetén nem törlődnek, hanem törölt jelzést kapnak, hogy audit funkció is biztosítható legyen


myabstractclasses: képfeltöltéshez szükséges abstract class amit a képeket tartalmazó entity-k extendelnek


repositories: Spring/Jakarta Persistence Api repositories az entity-khez adatbázis kezelést valósítanak meg JPQL nyelven íródott querykkel

services: "business logic" code a controllerek és repositoryk illetve egyéb rendszerek közötti kapcsolatot teremtik meg.

resources:
- data.sql a H2 in memory adatbázis induláskor való feltöltéséhez írt insert sql statements
-application.properties: az alkalmazás konfigurálásához szükséges tulajdonságok 