
Adatbázis: H2 in-memory adatbázis<br>
adatbázis console (php-myadminhoz hasonló)  /h2-console<br>
jdbc driver: mariadb driver<br>
<br>
konfiguráció: src\main\resources\application.properties<br>

Requirement(s):
    -Java 21



Figyelem!
Post/Hír létrehozásához ezekre a field/mezőkre van szügség: 
    -userId Long/Szám (a login után tárolni kell és itt behelyezni) (kötelező)
    -orgId Long/Szám (szintén a login után tárolva és behelyezve) (csak egy szervezet megbízottja/tagja hozhat létre ilyet így kötelező)
    -content String/szöveg (mondandó) (kötelező)
    -voteDescription String/szöveg A szavazás magyarázata/kérdés (ha van szavazás kötelező egyébként null)
    -optionTexts String[]/String-szöveg tömb  ["opció 1", "opció kettő", "opció III"] (ha van szavazás kötelező nem üres tömb legalább kettő nem üres Stringgel)
    see: PostController
    /post/create-with-vote
    /post/create-no-vote

Post/Hír Módosításához ezekre a field/mezőkre van szügség: 
    -id Long/Szám (a post id-je)
    -post Object (ugyanaz mint a létrehozásnál)

    példa:
    {"id":4,"postDTOCreate":{"userId":2,"orgId":2,"content":"szöveg a város egyik legjobb parkjában való fejlesztésről, és közvéleménykutatés hogy hány fa legyen","voteDescription":"LALALALALALALA_____válassz az opciók közül/ melyik opció tetszik jobban?","optionTexts":["Sok fa legyen Vagy sem","maximum kettő Tölgyfa legyen","mi az a fa?"]}}

    path:/post/:id (:id behejettesítve a példánál pl 4)