## Local run
Run app in IntelliJ directly for easy debug and development...

0) change mongodb to localhost! in 
   - [application.properties](catalog-service%2Fsrc%2Fmain%2Fresources%2Fapplication.properties)
   - [application.properties](payment-service%2Fsrc%2Fmain%2Fresources%2Fapplication.properties)
1) Run the script below to start databases
2) Run catalog-service from IntelliJ

**START**
```shell
    docker-compose -f mongo-postgres-compose.yml up -d
```
* `-f` Docker-compose file path
* `-d` Detached mode (run in the background)

**STOP**
```shell
    docker-compose -f mongo-postgres-compose.yml down -v
```
* `-v` Remove volumes declared in the volumes section of the Compose file


## Docker approach (all apps inside containers) + debug on 5007 and 5008 ports
Showtime 

```shell
   mvn clean install
```

**START**
```shell
    docker-compose -f docker-compose.yml up --build --force-recreate -d
```
* `--build` Build images before starting containers.
* `-d` Detached mode (run in the background)
* `--force-recreate` Recreate containers even if their configuration and image haven't changed. 

**STOP**
```shell
    docker-compose -f docker-compose.yml down --remove-orphans -v
```
* `-v` Remove volumes declared in the volumes section of the Compose file
* `--remove-orphans` Remove containers


**LOGS**
```shell
   docker logs -f payment-service
```