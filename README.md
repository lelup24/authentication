# Authentication Example using Spring Boot 3, Angular 16 and JWT

Authentication using Spring Boot 3 and Angular 16

* Clone

```bash
   git clone https://github.com/lelup24/authentication.git
 ```

* run a postgresql database with docker or your own

```bash
   docker compose up -d
```

* start backend or run in your IDE

```bash
   ./gradlew bootRun
```

* go to backend folder and run

```bash
   npm i
```

* go to frontend folder and start frontend

```bash
   ng serve
```

## RSA

```bash
  openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:4096
```

```bash
  openssl rsa -pubout -in private_key.pem -out public_key.pem
```
