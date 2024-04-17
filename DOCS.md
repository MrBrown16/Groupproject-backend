
# TartalomJegyzék

- [Áttekintés](#áttekintés)
    - [Requirements](#requirements)
- [API](#api)
    - [Authorization server](#auth)
    - [Resource server](#resource)
    - [Visszatérési értékek](#visszatérési-értékek--paraméterek)
- [Projekt futtatása](#projekt-futtatása)
- [Adatbázis](#adatbázis)
    - [auth-server](#authdump)
    - [res-server](#resdump)


# Áttekintés: 


## REQUIREMENTS:
- JAVA 21
- Maven
- English (Basic)

## [Frontend](https://github.com/Attila732/Onkormanyzat_)

Url: http://localhost:8081/client/

## [Backend](https://github.com/MrBrown16/Groupproject-backend)

### resource-server: 

Url: http://localhost:8082  ==  http://localhost:8081/resource/

Feladat: felhasználók, szervezetek adatainak tárolása módosítása törlése

### authorization-server:

Url: http://localhost:8083

Feladat: Felhasználók azonosítása, Jwt-k készítése, alap felhasználó adatok tárolása módosítása törlése, kommunikáció a resource serverrel jwt-k validálása érdekében

## Api(használatban lévő végpontok):

__Jelmagyarázat:__

{valami} = url részlet amit mint paramétert/változót használunk és amit @PathVariable annotációval megjelölve a Spring framework nyer ki és tesz elérhetővé mint valami

__Általános paraméterek:__

Authorization auth: A Spring Framework / Spring Security a jelenleg bejelentkezett felhasználó adatait tartalmazó objektum 

### Auth:

- [/login](http://localhost:8083/login) bejelentkező oldal 
- [/.well-known/openid-configuration](http://localhost:8083/.well-known/openid-configuration) authorization server végpontok info
- [/h2-console](http://localhost:8083/h2-console) authorization server adatbázis belépés ()

### Resource: 


- [/resource/](http://localhost:8081/resource/)

    - [/user](http://localhost:8081/resource/user) felhasználó kezelés 

        - [GET /id/ex/{id}](http://localhost:8081/resource/user/id/ex/{id}) felhasználó adatainak lekérése id alapján ex: extended 
            - Visszatérési érték: [UserDtoPrivatePartial](#userdtoprivatepartial)
        - [GET /admin/search](http://localhost:8081/resource/user/admin/search) admin jogosultsággal felhasználók keresése 
            - Paraméterek:
                - value(szöveg): a keresési érték/szöveg
                - pageNum(szám): az oldalszám
                - category(UserFields ENUM): enum ami a felhasználó objektum field-jeit reprezentálja
            - Visszatérési érték: [UserDtoPrivatePartial](#userdtoprivatepartial)
        - [GET /myUserInfo](http://localhost:8081/resource/user/myUserInfo) a bejelentkezett felhasználó adatai lekérése Role-okkal és Org(szervezet)-ekkel kiegészítve
            - Visszatérési érték: [UserInfoDto](#userinfodto)
        - [GET /{userId}/orgs](http://localhost:8081/resource/user/{userId}/orgs) felhasználó org(szervezet)-einek lekérése
            - Paraméterek:
                - pageNum(szám): az oldalszám 
            - Visszatérési érték: Set(halmaz) [OrgDtoPublicPartial](#orgdtopublicpartial)
        - [POST ](http://localhost:8081/resource/user) új felhasználó létrehozása regisztráció
            - Paraméterek: 
                - [UserDtoNewWithPW](#userdtonewwithpw)
        - [PUT /{id}](http://localhost:8081/resource/user/{id}) felhasználó adatainak módosítása
            - Paraméterek: 
                - [UserDtoNew](#userdtonew)
        - [DELETE /{id}](http://localhost:8081/resource/user/{id}) felhasználó törlése

    - [/org](http://localhost:8081/resource/org) szervezet kezelés

        - [GET /search/name](http://localhost:8081/resource/search/name) org(szervezet)-ek lekérése név(részlet) alapján 
            - Paraméterek:
                - name(szöveg): a keresési érték/szöveg
                - pageNum(szám): az oldalszám
            - Visszatérési érték: Set(halmaz) [OrgDtoPublicPartial](#orgdtopublicpartial)

    - [/items](http://localhost:8081/resource/items) termék kezelés

        - [GET /search/price](http://localhost:8081/resource/items/search/price) termékek lekérése ár alapján szűrve
            - Paraméterek:
                - price(szám): a keresési érték
                - pageNum(szám): az oldalszám
                - category(szöveg): a price- nél nagyobb("higher") vagy kisebb("lower") értékű termékeket kér
            - Visszatérési érték: Set(halmaz) [ItemDtoPublicWithImages](#itemdtopublicwithimages)
        - [GET /sajat/{userId}](http://localhost:8081/resource/items/sajat/{userId}) termékek lekérése felhasználó id alapján
            - Visszatérési érték: Set(halmaz) [ItemDtoPublicWithImages](#itemdtopublicwithimages)
        - [POST /new](http://localhost:8081/resource/items/new) új termék létrehozása
            - Paraméterek: 
                - [ItemDto](#itemdto)
        - [PUT /{itemId}](http://localhost:8081/resource/items/new) termék módosítása
            - Paraméterek: 
                - [ItemDto](#itemdto)
        - [DELETE /del/{itemId}](http://localhost:8081/resource/items/new) termék törlése 

    - [/news](http://localhost:8081/resource/news) hír kezelés

        - [GET /search](http://localhost:8081/resource/news/search) keresés a hírek között
            - Paraméterek:
                - value(szöveg): a keresési érték/szöveg
                - pageNum(szám): az oldalszám
                - category(szöveg): a news objektum melyik field-jében keres ("title"/"content"/"type")
            - Visszatérési érték: Page(Oldal) [NewsDtoPublic](#newsdtopublic)
        - [POST /new](http://localhost:8081/resource/news/new) hír létrehozása
            - Visszatérési érték: [NewsDtoPublic](#newsdtopublic)
        - [PUT /{newsId}](http://localhost:8081/resource/news/{newsId}) hír módosítása
            - Visszatérési érték: [NewsDtoPublic](#newsdtopublic)
        - [DELETE /del/{newsId}](http://localhost:8081/resource/news/del/{newsId}) hír törlése

    - [/event](http://localhost:8081/resource/event)

        - [GET /search](http://localhost:8081/resource/event/search) keresés az események között
            - Paraméterek:
                - value(szöveg): a keresési érték/szöveg
                - pageNum(szám): az oldalszám
                - category(szöveg): az event objektum melyik field-jében keres ("name"/"description"/"location")
            - Visszatérési érték: Page(Oldal) [EventDtoPublic](#eventdtopublic)
        - [GET /sajat/{orgId}](http://localhost:8081/resource/event/sajat/{orgId}) egy org(szervezet) által szervezett események lekérése 
            - Visszatérési érték: Set(halmaz) [EventDtoPublic](#eventdtopublic)
        - [POST /new](http://localhost:8081/resource/event/new) új esemény létrehozása
            - Paraméterek: 
                - [EventDto](#eventdto)
        - [PUT /{eventId}](http://localhost:8081/resource/event/{eventId}) esemény módosítása
            - Paraméterek: 
                - [EventDto](#eventdto)
        - [DELETE /del/{eventId}](http://localhost:8081/resource/event/del/{eventId}) esemény törlése
    
    - [/notice](http://localhost:8081/resource/notice) bejelentések kezelése
        - [GET /search](http://localhost:8081/resource/notice/search) bejelentések keresése a bejelentés típúsa alapján 
            - Paraméterek:
                - pageNum(szám): az oldalszám
                - category(NoticeTypes ENUM): a bejelentés típúsai
            - Visszatérési érték: Page(Oldal) [NoticeDtoPublic](#noticedtopublic)
        - [GET /sajat/{userId}](http://localhost:8081/resource/notice/sajat/{userId}) bejelentések lekérése felhasználónként
            - Visszatérési érték: Set(halmaz) [NoticeDtoPublic](#noticedtopublic)
        - [GET /org/{orgId}](http://localhost:8081/resource/notice/org/{orgId}) bejelentések lekérése org(szervezet) responsibility(ügykör) alapján
            - Paraméterek:
                - pageNum(szám): az oldalszám
            - Visszatérési érték: Set(halmaz) [NoticeDtoPublic](#noticedtopublic)
        - [POST /new](http://localhost:8081/resource/notice/new) új bejelentés létrehozása/megtétele
            - Paraméterek: 
                - [NoticeDto](#noticedto)
        - [DELETE /del/{noticeId}](http://localhost:8081/resource/notice/del/{noticeId}) bejelentés törlése


### Visszatérési értékek / Paraméterek:

- <a name="userdtoprivatepartial"> __UserDtoPrivatePartial__ </a>
    - id: szöveg
    - email: 
    - userName: szöveg
    - firstName: szöveg
    - lastName: szöveg
    - phone: szám

- <a name="userinfodto"> __UserInfoDto__ </a>
    - userId: szöveg
    - email: szöveg
    - name: szöveg
    - phone: szám
    - roles: szöveg tömb a felhasználó jogosultságai
    - location: szöveg 
    - orgs: szöveg tömb a felhasználó org(szervezet)-einek az id-i

- <a name="orgdtopublicpartial"> __OrgDtoPublicPartial__ </a> 
    - id: szöveg
    - name: szöveg

- <a name="userdtonewwithpw"> __UserDtoNewWithPW__ </a> 
    - userName: szöveg
    - firstName: szöveg
    - lastName: szöveg
    - phone: szám
    - email : szöveg
    - password1: szöveg
    - password2: szöveg

- <a name="userdtonew"> __UserDtoNew__ </a> 
    - userName: szöveg
    - firstName: szöveg
    - lastName: szöveg
    - phone: szám
    - email: szöveg

- <a name="itemdtopublicwithimages"> __ItemDtoPublicWithImages__ </a> 
    - itemId: szöveg
    - userId: szöveg
    - name: szöveg
    - description: szöveg
    - condition: szöveg
    - location: szöveg
    - email: szöveg
    - phone: szám
    - price: szám
    - images: szöveg tömb a képek url-je

- <a name="itemdto"> __ItemDto__ </a> 
    - itemId: szöveg
    - userId: szöveg
    - name: szöveg
    - description: szöveg
    - condition: szöveg
    - location: szöveg
    - email: szöveg
    - phone: szám
    - price: szám

- <a name="newsdtopublic"> __NewsDtoPublic__ </a> 
    - id: szöveg
    - userId: szöveg
    - userName: szöveg
    - orgId: szöveg
    - orgName: szöveg
    - title: szöveg
    - content: szöveg
    - type: [NewsTypes](#newstypes)

- <a name="newstypes"> __NewsTypes__ </a> (ENUM)
    - LOCAL
    - NATIONAL
    - INTERNATIONAL

- <a name="eventdtopublic"> __EventDtoPublic__ </a> 
    - eventId: szöveg
    - name: szöveg
    - description: szöveg
    - location: szöveg
    - userId: szöveg
    - orgId: szöveg
    - publicPhones: szám lista
    - publicEmails: szöveg lista
    - startDate: Timestamp
    - endDate: Timestamp

- <a name="eventdto"> __EventDto__ </a>
    - name: szöveg
    - description: szöveg
    - location: szöveg
    - userId: szöveg
    - orgId: szöveg
    - publicPhones: szám lista
    - publicEmails: szöveg lista
    - startDate: Timestamp 
    - endDate: Timestamp 



- <a name="noticedtopublic"> NoticeDtoPublic </a> 
    - noticeId: szöveg
    - userId: szöveg
    - type: [NoticeTypes](#noticetypes)   
    - urgency: szöveg
    - description: szöveg
    - location: szöveg
    - phone: szám
    - date: Timestamp 

- <a name="noticetypes"> __NoticeTypes__ </a> (ENUM)
    - KOZTERULET
    - UTHIBA
    - VIZGAZ
    - LOMTALANITAS
    - SZEMETSZALLITAS

- <a name="noticedto"> NoticeDto </a> 
    - userId: szöveg
    - type: [NoticeTypes](#noticetypes)  
    - urgency: szöveg
    - description: szöveg
    - location: szöveg
    - phone: szám
    - date: Timestamp 


# Projekt Futtatása

- 







# Adatbázis

__Jelenlegi / development:__ embedded H2 in-memory adatbázis mariadb driverrel
__Jövőbeni:__ mariadb mariadb driverrel "drop in replacement"

__Teszt adatok:__ src\main\resources\data.sql -ből van feltöltve a resource server indításakor

__Schema:__ Jakarta Persistence Api / Hibernate ORM - által generált az entities classok alapján

__Dump File:__ 

- <a name="authdump" > __authorization-server\dump.sql__ </a>

    - USERS:

        - USERNAME VARCHAR_IGNORECASE(50) __Primary Key__
        - PASSWORD VARCHAR_IGNORECASE(150) 
        - ENABLED BOOLEAN 

    - AUTHORITIES:

        - USERNAME VARCHAR_IGNORECASE(50) __Foreign Key__
        - AUTHORITY VARCHAR_IGNORECASE(50)

- <a name="resdump" > __resource-server\dump.sql__ </a> Soft-delete audit funkcionalitás érdekében a törlés nem törli csak töröltként jelöli meg a rekordokat (DELETED=true)

    - EVENTS:

        - EVENT_ID CHARACTER VARYING(255) __Primary Key__
        - DESCRIPTION CHARACTER VARYING(255)
        - ENDDATE TIMESTAMP(6)
        - LOCATION CHARACTER VARYING(255)
        - NAME CHARACTER VARYING(255)
        - PUBLICEMAILS CHARACTER VARYING(255) ARRAY
        - PUBLICPHONES BIGINT ARRAY
        - STARTDATE TIMESTAMP(6)
        - ORG_ID CHARACTER VARYING(255) __Foreign Key__
        - USER_ID CHARACTER VARYING(255) __Foreign Key__
        - DELETED BOOLEAN COMMENT 'Soft-delete indicator'

    - ITEMS:
        
        - ITEM_ID CHARACTER VARYING(255) __Primary Key__
        - CONDITION CHARACTER VARYING(255)
        - CREATIONTIME TIMESTAMP(6)
        - DESCRIPTION CHARACTER VARYING(255)
        - EMAIL CHARACTER VARYING(255)
        - LOCATION CHARACTER VARYING(255)
        - NAME CHARACTER VARYING(255)
        - PHONE BIGINT
        - PRICE BIGINT
        - UPDATETIME TIMESTAMP(6)
        - USER_ID CHARACTER VARYING(255) __Foreign Key__
        - DELETED BOOLEAN COMMENT 'Soft-delete indicator' 

    - MYORG_CATEGORIES:

        - MYORG_ORG_ID CHARACTER VARYING(255) __Foreign Key__
        - CATEGORIES CHARACTER VARYING(255) __Foreign Key__
        - DELETED BOOLEAN COMMENT 'Soft-delete indicator' 

    - MYORG_RESPONSIBILITIES:

        - MYORG_ORG_ID CHARACTER VARYING(255) __Foreign Key__
        - RESPONSIBILITIES CHARACTER VARYING(255)
        - DELETED BOOLEAN COMMENT 'Soft-delete indicator' 

    - NEWS

        - NEWS_ID CHARACTER VARYING(255) __Primary Key__
        - CONTENT CHARACTER LARGE OBJECT
        - CREATIONTIME TIMESTAMP(6)
        - TITLE CHARACTER LARGE OBJECT
        - TYPE CHARACTER VARYING(255)
        - UPDATETIME TIMESTAMP(6)
        - ORG_ID CHARACTER VARYING(255) __Foreign Key__
        - USER_ID CHARACTER VARYING(255) __Foreign Key__
        - DELETED BOOLEAN COMMENT 'Soft-delete indicator' 

    - NOTICES

        - NOTICE_ID CHARACTER VARYING(255) __Primary Key__
        - DATE TIMESTAMP(6)
        - DESCRIPTION CHARACTER VARYING(255)
        - LOCATION CHARACTER VARYING(255)
        - PHONE BIGINT
        - TYPE CHARACTER VARYING(255)
        - URGENCY CHARACTER VARYING(255)
        - USER_ID CHARACTER VARYING(255) __Foreign Key__
        - DELETED BOOLEAN COMMENT 'Soft-delete indicator' 

    - ORGANISATIONS

        - ORG_ID CHARACTER VARYING(255) __Primary Key__
        - NAME CHARACTER VARYING(255)
        - DELETED BOOLEAN COMMENT 'Soft-delete indicator' 

    - ORGS_ADMINS

        - USER_ID CHARACTER VARYING(255) __Foreign Key__
        - ORG_ID CHARACTER VARYING(255) __Foreign Key__
        - DELETED BOOLEAN COMMENT 'Soft-delete indicator' 

    - POSTS

        - POST_ID CHARACTER VARYING(255) __Primary Key__
        - CONTENT CHARACTER VARYING(255)
        - CREATIONTIME TIMESTAMP(6)
        - DISLIKES BIGINT DEFAULT 0
        - LIKES BIGINT DEFAULT 0
        - UPDATETIME TIMESTAMP(6)
        - ORG_ID CHARACTER VARYING(255) __Foreign Key__
        - USER_ID CHARACTER VARYING(255) __Foreign Key__
        - VOTE_ID CHARACTER VARYING(255) __Foreign Key__
        - DELETED BOOLEAN COMMENT 'Soft-delete indicator' 

    - RESERVATIONS

        - RESERVATION_ID CHARACTER VARYING(255) __Primary Key__
        - EMAIL CHARACTER VARYING(255)
        - ENDDATE TIMESTAMP(6)
        - PHONE BIGINT
        - PREFERREDNAME CHARACTER VARYING(255)
        - STARTDATE TIMESTAMP(6)
        - ORG_ID CHARACTER VARYING(255) __Foreign Key__
        - USER_ID CHARACTER VARYING(255)  __Foreign Key__
        - DELETED BOOLEAN COMMENT 'Soft-delete indicator' 

    - USERS

        - USER_ID CHARACTER VARYING(255) __Primary Key__
        - EMAIL CHARACTER VARYING(255) __UNIQUE__
        - FIRSTNAME CHARACTER VARYING(255)
        - LASTNAME CHARACTER VARYING(255)
        - PHONE BIGINT
        - USERNAME CHARACTER VARYING(255) __Foreign Key__ (authorization-server USERS) 
        - DELETED BOOLEAN COMMENT 'Soft-delete indicator' 

    - VOTE_OPTIONS

        - VOTE_OPTION_ID CHARACTER VARYING(255) __Primary Key__
        - OPTIONTEXT CHARACTER VARYING(255)
        - VOTESNUM BIGINT DEFAULT 0
        - VOTE_ID CHARACTER VARYING(255) __Foreign Key__
        - DELETED BOOLEAN COMMENT 'Soft-delete indicator' 

    - VOTES

        - VOTE_ID CHARACTER VARYING(255) __Primary Key__
        - DESCRIPTION CHARACTER VARYING(255)
        - POST_ID CHARACTER VARYING(255) __Foreign Key__
        - DELETED BOOLEAN COMMENT 'Soft-delete indicator' 