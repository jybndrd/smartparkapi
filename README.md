# Smart Park Api
a smart parking api


## Libraries/Tools Used
- Java 21
- [Spring Boot](https://spring.io/projects/spring-boot) with the following components:
    * Spring Web
    * Spring JPA
    * Spring Security
- [Json Web Token](https://jwt.io/)
- [Lombok](https://projectlombok.org/)
- [MapStruct](https://mapstruct.org/)
- [H2](https://www.h2database.com/html/main.html) - File based data based configuration

### Getting started
Please clone this project into your local machine and let's make sure that everything is set up before going into details.
Here are also some of the features that i've included in this app:
- This application is configured with spring security. JWT Auth is the first filter used and then the Usernamd and password auth.
- The user authentication is also dynamic. Meaning that the user is able to register an account which is saved in our database. The password used if encoded using BCryptPasswordEncoder with a strength of 10. User is able to register in the url: `http://localhost:8090/register` via POST method. This is included in the test request templates below.
- The secretKey and token generation is found in the [JWTService](src/main/java/com/demo/smartpark/service/JWTService.java)
- Various custom exception classes are also included to manage some of the errors encountered.
- For the fee calculation, the vehicles are not given a "grace period" meaning that a minimum cost is always charged whenever a client park.
- The scheduler that handles vehicles parked in more than X mins are handle through the class [VehicleRemovalScheduler](src/main/java/com/demo/smartpark/scheduler/VehicleRemovalScheduler.java)

#### A. We will be using H2 as out database. This makes things easier for testing. Make sure that you have the db file for H2.
1. Check this file if existing: /db/data/smartparkdb.mv.db
2. Check the `application.yml` under the **src/main/resources** if configured correctly, here are the expected configurations:
   ```
   spring:
    application:
      name: Smart Park Application
  
    datasource:
      url: jdbc:h2:file:./db/data/smartparkdb
      driver-class-name: org.h2.Driver
      username: sa
      password: admin
  
    h2:
      console:
        enabled: true #Enable H2 web console
        path: /h2-console #Path http://localhost:8090/h2-console
   ```
3. Check if you are able to connect and see some pre loaded data in the database. Use the above credentials. We are expected to see `user`, `parking_lot`, `vehicle`, and `parking_lot_vehicle` tables. These tables should also have rows inside of it.
**NOTE**: note that an enum was not made for the type of car. We can introduce this if necessary.
   
#### B. Run the Spring Boot application
1. Open your IDE (**e.g.,** IntelliJ)
2. Go to [SmartparkApplication](src/main/java/com/demo/smartpark/SmartparkApplication.java)
3. Right-click then select **Run/Debug**.
4. This will run the Spring Boot application - below is the expected logs at startup.
**NOTE** : You won't be able to start the application if you are currently connected to the database, it is one of the limitation of H2. Kindly check if you database is connected.

```
  _________                      __ __________               __
 /   _____/ _____ _____ ________/  |\______   \_____ _______|  | __
 \_____  \ /     \\__  \\_  __ \   __\     ___/\__  \\_  __ \  |/ /
 /        \  Y Y  \/ __ \|  | \/|  | |    |     / __ \|  | \/    <
/_______  /__|_|  (____  /__|   |__| |____|    (____  /__|  |__|_ \
        \/      \/     \/                           \/           \/
Smart Park Application
Powered by Spring Boot 3.4.3
...
2025-03-03T18:55:53.281+08:00  INFO 19453 --- [Smart Park Application] [  restartedMain] o.s.b.a.h2.H2ConsoleAutoConfiguration    : H2 console available at '/h2-console'. Database available at 'jdbc:h2:file:./db/data/smartparkdb'
2025-03-03T18:55:53.342+08:00  INFO 19453 --- [Smart Park Application] [  restartedMain] o.s.b.d.a.OptionalLiveReloadServer       : LiveReload server is running on port 35729
2025-03-03T18:55:53.364+08:00  INFO 19453 --- [Smart Park Application] [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8090 (http) with context path '/'
2025-03-03T18:55:53.376+08:00  INFO 19453 --- [Smart Park Application] [   scheduling-1] c.d.s.scheduler.VehicleRemovalScheduler  : Running scheduled task to remove long-parked vehicles.
2025-03-03T18:55:53.377+08:00  INFO 19453 --- [Smart Park Application] [  restartedMain] c.demo.smartpark.SmartparkApplication    : Started SmartparkApplication in 3.529 seconds (process running for 4.038)
```

#### C. Test all the API! We can use Postman or "Generate Request in HTTP Client" via your IntelliJ IDE. Here are some of the sample requests that I've made and its details:
### Register new user
POST http://localhost:8090/register
Content-Type: application/json

{
  "userName": "newhasheduser",
  "password": "hashedpassword"
}

### Trial of invalid credentials login
POST http://localhost:8090/login
Content-Type: application/json

{
  "userName": "randomusername",
  "password": "randompassword"
}

### Another trial with known username
POST http://localhost:8090/login
Content-Type: application/json

{
"userName": "newhasheduser",
"password": "randompassword"
}

### Login your newly created user, take note of the jwt key given. This token will be used for next transactions
POST http://localhost:8090/login
Content-Type: application/json

{
  "userName": "newhasheduser",
  "password": "hashedpassword"
}

### Place the token here to use for future requests. This token will expire after 1 hour
@token = eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXdoYXNoZWR1c2VyIiwiaWF0IjoxNzQxMDAwMTIzLCJleHAiOjE3NDEwMDM3MjN9.u_UUqrwwvGN9KR6E8dlvLhZ_FBxz2qMH3FRXC7aQ9-M

### Sanity check for access. Dont forget to LOGIN and change the token value above
GET http://localhost:8090/api/smartparking/sanityCheck
Authorization: Bearer {{token}}

### Get all parking lot records
GET http://localhost:8090/api/smartparking/getAllParkingLots
Authorization: Bearer {{token}}

### Register a vehicle
POST http://localhost:8090/api/smartparking/registerVehicle
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "licensePlate": "ABC123",
  "type": "Car",
  "ownerName": "Juan Dela Cruz"
}

### Register a parking lot
POST http://localhost:8090/api/smartparking/registerParkingLot
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "location": "Quezon City",
  "capacity": 3,
  "occupiedSpaces": 0,
  "costPerMinute": 1
}

### Place the token here to use for future requests. This token will expire after 1 hour
@parkingLotId = 1
@licensePlate = ABC123

### Get a specific parking lot using a parking lot id
GET http://localhost:8090/api/smartparking/getParkingLot/{{parkingLotId}}
Authorization: Bearer {{token}}

### Get a specific a vehicle using a license plate
GET http://localhost:8090/api/smartparking/getVehicle/{{licensePlate}}
Authorization: Bearer {{token}}

### Get the occupancy of a parking lot using a parking lot id
GET http://localhost:8090/api/smartparking/parkingLotOccupancy/{{parkingLotId}}
Authorization: Bearer {{token}}

### Get the vehicles checked in a parking lot using a parking lot id
GET http://localhost:8090/api/smartparking/parkingLotVehicles/{{parkingLotId}}
Authorization: Bearer {{token}}

### Check in a vehicle on a given parking lot
POST http://localhost:8090/api/smartparking/checkInVehicle/{{parkingLotId}}/{{licensePlate}}
Authorization: Bearer {{token}}

### Check out a vehicle
POST http://localhost:8090/api/smartparking/checkOutVehicle/{{licensePlate}}
Authorization: Bearer {{token}}

###
GET http://localhost:8090/api/smartparking/getParkRecords
Authorization: Bearer {{token}}
