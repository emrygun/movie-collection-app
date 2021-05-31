[![Docker Compose CI](https://github.com/emrygun/movie-collection-app/actions/workflows/main.yml/badge.svg)](https://github.com/emrygun/movie-collection-app/actions/workflows/main.yml)
# Movie Collection Application
Movie Collection App powered by Java, Spring, JPA, Thymeleaf, MySQL.

## Project Summary

It is a simple movie collection application which lets user sign up and add, inspect and edit movie entries. 
Movie entries includes name, languages, genre, media (.png), year, description, N actors with their roles.
There are 3 roles (USER, ADMIN, FOUNDER) in application with different authorities. Default role is USER. 

**Roles:**
- Founder:
  - Can access "Users" tab.
  - Can change users role between USER and ADMIN.
  - Can remove users.
  - Can see other users movie collections with "Get User" button
  - Can do what users can.
  
- Admin
  - Can access "Users" tab.
  - Can see other users movie collections. 
  - Cannot change the users role and can't remove user like FOUNDER.
  - Can do what users can.
  
- User
  - Can create movies and edit their own movies.
  - Can get their movie entries details.
  - Default role. Users sign up registers into database with this role.
  
**PS:** On startup, you can acces to "testfounder" account with in-memory authentication.
This account not belongs to database so it can not create movie entries but can remove users, 
change users roles and get other users collections. Default username and password of this account is "testfounder".
You can edit the username and password from <em>docker-compose.yml</em> file by adjusting enviroment variables of app entry.

    line 12 and 13:
    - TESTFOUNDER_USERNAME=testfounder
    - TESTFOUNDER_password=testfounder

---
## Build and Run
In project directory:

    docker-compose --file docker-compose.yml up --build
Make sure your 8080 and 3306 ports are free!
