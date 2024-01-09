
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