# Áttekintés:

## [Frontend](https://github.com/Attila732/Onkormanyzat_)

Url:http://localhost:8081/client/

## [Backend](https://github.com/MrBrown16/Groupproject-backend)

### resource-server: 

Url:http://localhost:8082  ==  http://localhost:8081/resource/

Feladat: felhasználók, szervezetek adatainak tárolása módosítása törlése

### authorization-server:

Url:http://localhost:8083

Feladat: Felhasználók azonosítása, Jwt-k készítése, alap felhasználó adatok tárolása módosítása törlése, kommunikáció a resource serverrel jwt-k validálása érdekében

## Api(használatban lévő végpontok):
Jelmagyarázat:
{valami} = url részlet amit mint paramétert/változót használunk és amit @PathVariable annotációval megjelölve a Spring framework nyer ki és tesz elérhetővé mint valami

### Auth:

- [/login](http://localhost:8083/login) bejelentkező oldal 
- [/.well-known/openid-configuration](http://localhost:8083/.well-known/openid-configuration) authorization server végpontok info
- [/h2-console](http://localhost:8083/h2-console) authorization server adatbázis belépés ()

### Resource: 

- [/resource/](http://localhost:8081/resource/)

    - [/user](http://localhost:8081/resource/user) felhasználó kezelés 

        - [GET /id/ex/{id}](http://localhost:8081/resource/user/id/ex/{id}) felhasználó adatainak lekérése id alapján ex:extended 
            - Visszatérési érték: [UserDtoPrivatePartial](#userdtoprivatepartial)
        - [GET /admin/search](http://localhost:8081/resource/user/admin/search) admin jogosultsággal felhasználók keresése 
            - Paraméterek:
                - value(szöveg): a keresési érték/szöveg
                - pageNum(szám): az oldalszám
                - category(UserFields ENUM): enum ami a felhasználó objektum field-jeit reprezentálja
            - Visszatérési érték: [UserDtoPrivatePartial](#userdtoprivatepartial)
        - [GET /myUserInfo](http://localhost:8081/resource/user/myUserInfo) a bejelentkezett felhasználó adatai lekérése Role-okkal és Org(szervezet)-ekkel kiegészítve
        - [GET /{userId}/orgs](http://localhost:8081/resource/user/{userId}/orgs) felhasználó org(szervezet)-einek lekérése
            - Paraméterek:
                - pageNum(szám): az oldalszám 
        - [POST ](http://localhost:8081/resource/user) új felhasználó létrehozása regisztráció
        - [PUT /{id}](http://localhost:8081/resource/user/{id}) felhasználó adatainak módosítása
        - [DELETE /{id}](http://localhost:8081/resource/user/{id}) felhasználó törlése

    - [/org](http://localhost:8081/resource/org) szervezet kezelés

        - [GET /search/name](http://localhost:8081/resource/search/name) org(szervezet)-ek lekérése név(részlet) alapján 
            - Paraméterek:
                - name(szöveg): a keresési érték/szöveg
                - pageNum(szám): az oldalszám

    - [/items](http://localhost:8081/resource/items) termék kezelés

        - [GET /search/price](http://localhost:8081/resource/items/search/price) termékek lekérése ár alapján szűrve
            - Paraméterek:
                - price(szám): a keresési érték
                - pageNum(szám): az oldalszám
                - category(szöveg): a price- nél nagyobb("higher") vagy kisebb("lower") értékű termékeket kér
        - [GET /sajat/{userId}](http://localhost:8081/resource/items/sajat/{userId}) termékek lekérése felhasználó id alapján
        - [POST /new](http://localhost:8081/resource/items/new) új termék létrehozása
        - [PUT /{itemId}](http://localhost:8081/resource/items/new) termék módosítása
        - [DELETE /del/{itemId}](http://localhost:8081/resource/items/new) termék törlése 

    - [/news](http://localhost:8081/resource/news) hír kezelés

        - [GET /search](http://localhost:8081/resource/news/search) keresés a hírek között
            - Paraméterek:
                - value(szöveg): a keresési érték/szöveg
                - pageNum(szám): az oldalszám
                - category(szöveg): a news objektum melyik field-jében keres ("title"/"content"/"type")
        - [POST /new](http://localhost:8081/resource/news/new) hír létrehozása
        - [PUT /{newsId}](http://localhost:8081/resource/news/{newsId}) hír módosítása
        - [DELETE /del/{newsId}](http://localhost:8081/resource/news/del/{newsId}) hír törlése

    - [/event](http://localhost:8081/resource/event)

        - [GET /search](http://localhost:8081/resource/event/search) keresés az események között
            - Paraméterek:
                - value(szöveg): a keresési érték/szöveg
                - pageNum(szám): az oldalszám
                - category(szöveg): az event objektum melyik field-jében keres ("name"/"description"/"location")
        - [GET /sajat/{orgId}](http://localhost:8081/resource/event/sajat/{orgId}) egy org(szervezet) által szervezett események lekérése 
        - [POST /new](http://localhost:8081/resource/event/new) új esemény létrehozása
        - [PUT /{eventId}](http://localhost:8081/resource/event/{eventId}) esemény módosítása
        - [DELETE /del/{eventId}](http://localhost:8081/resource/event/del/{eventId}) esemény törlése
    
    - [/notice](http://localhost:8081/resource/notice) bejelentések kezelése
        - [GET /search](http://localhost:8081/resource/notice/search) bejelentések keresése a bejelentés típúsa alapján 
            - Paraméterek:
                - pageNum(szám): az oldalszám
                - category(NoticeTypes ENUM): a bejelentés típúsai
        - [GET /sajat/{userId}](http://localhost:8081/resource/notice/sajat/{userId}) bejelentések lekérése felhasználónként
        - [GET /org/{orgId}](http://localhost:8081/resource/notice/org/{orgId}) bejelentések lekérése org(szervezet) responsibility(ügykör) alapján
            - Paraméterek:
                - pageNum(szám): az oldalszám
        - [POST /new](http://localhost:8081/resource/notice/new) új bejelentés létrehozása/megtétele
        - [DELETE /del/{noticeId}](http://localhost:8081/resource/notice/del/{noticeId}) bejelentés törlése


### Visszatérési értékek:

- <a name="userdtoprivatepartial"> __UserDtoPrivatePartial__ <a>
    - id: szöveg
    - email: 
    - userName: szöveg
    - firstName: szöveg
    - lastName: szöveg
    - phone: szám

- <a name="userinfodto"> __UserInfoDto__ <a>
    - userId: szöveg
    - email: szöveg
    - name: szöveg
    - phone: szám
    - roles: szöveg tömb a felhasználó jogosultságai
    - location: szöveg 
    - orgs: szöveg tömb a felhasználó org(szervezet)-einek az id-i


        


             