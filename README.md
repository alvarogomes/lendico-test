# Lendico - Code challenge

### Tech stack
This project was created using Maven, Java 8, and Spring boot.

### How test the code? 

#### 1) Clone the github repository:
```sh
git clone https://github.com/alvarogomes/lendico-test.git
```

#### 2) Generate the build and execute the tests 
```sh
mvn clean install
```

#### 3) Running the app
```sh
java -jar target/lendico-test-0.0.1-SNAPSHOT.jar 
```

#### 4) Swagger + Test
To see the APIs that exist on this project just access in the browser:
```sh
http://localhost:8080/swagger-ui.html
```

Also for testing I recommend use *Postman* for this:

Create a POST request for the address: 
```sh
http://localhost:8080/generate-plan
```

And put this request body:

````json
{
  "loanAmount": "5000",
  "nominalRate": "5.0",
  "duration": 24,
  "startDate": "2018-01-01T00:00:00Z"
}
````

Thanks =)

