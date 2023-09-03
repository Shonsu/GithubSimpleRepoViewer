## Simple reading of Github repositories for the user
#### (Recruitment task)

### General info
This project lists all user github repositories, which are not forks and have the following information:
* Repository Name
* Owner Login
* For each branch it’s name and last commit sha

### Technologies
Project is created with:
* Java
* Spring Boot
* maven

### Setup
To run this project run console and use listed commands:
```shell
git clone git@github.com:Shonsu/GithubReader.git
cd GithubReader
mvn spring-boot:run
```
## REST API
### Get simple information of existing user repositories
#### Request

`GET /simplerepository/{username}`

    curl -i -H 'Accept: application/json' http://localhost:8080/simplerepository/username
#### Response

    HTTP/1.1 200
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Sun, 03 Sep 2023 18:06:42 GMT

    [{"repositoryName":"aisd","owner":"Shonsu","branches":[{"name":"master","sha":"7432d23717a2be3db66c1f7cd36c3ba18595b3e1"}]},...]

### Get simple information of NOT existing user

`GET /simplerepository/{username}`

    curl -i -H 'Accept: application/json' http://localhost:8080/simplerepository/notExistingUser

    {"status":"404 NOT_FOUND","message":"User not found."}

### Get simple information of existing user with header 'Accept: application/xml'

`GET /simplerepository/{username}`

    curl -i -H 'Accept: application/xml' http://localhost:8080/simplerepository/username

    {"status":"406 NOT_ACCEPTABLE","message":"Media type XML is not acceptable representation."}




