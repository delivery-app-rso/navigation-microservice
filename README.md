# RSO: Image metadata microservice

## How to run

1. Create a copy of .env.example and name it .env

2. Fill the values in .env

3. Run `mvn clean package`, `docker-compose build` and `docker-compose up`

4. Navigation microservice is now accessible on http://127.0.0.1:8080/v1/navigation

5. You can access individual addresses by adding their id after the above URL (http://127.0.0.1:8080/v1/navigation/{addressID}).
