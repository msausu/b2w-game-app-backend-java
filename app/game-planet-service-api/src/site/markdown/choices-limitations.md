
## Organization

There are 3 modules for structuring the back-end:

- persistence
- swapi-client
- rest api (this one)

## Choices

- Java8 (JEE CDI)
- Maven
- MongoDB

## Bundling

- payara-micro

## Libraries (easy to change)

- JAX-RS Jersey & Jackson (JAXB)
- JPA Eclipselink (NoSQL)

## Testing

- JUnit and TestNG
- embedmongo-maven-plugin for database IT
- microshed-testing-core for rest IT

(tested in Fedora 32)

## Decisions

1. Services are not async or reactive
2. No request throttling (use load-balancer)
3. No user authentication (no specs)
4. The swapi-client caches requests and
   because of this is fragile (external dependency).
   The specs don't say if "number of films" could be
   part of the DB and kept in sync by another process.

## Configuration

Addresses, ports and database names are maven properties. Defaults:

- localhost: http/8080, mongodb/27017
- database name: test

## Building

- build: mvn clean install -Dmaven.test.skip
- test: mvn clean compile test-compile package integration-test
- docs: mvn site:site

## Artifacts

- game-swapi-client-x.x.x (jar)
- game-planet-model-x.x.x (jar)
- game-planet-service-api-x.x.x (war)
- game-planet-service-api-x.x.x-microbundle (jar)

(target dir of module)

## Deployment

- as a war in a container
- this basedir (needs payara-micro): java -jar payara-micro-5.201.jar --deploy target/game-planet-service-api-1.0.0.war
- standalone anywhere: java -jar game-planet-service-api-1.0.0-microbundle.jar 

(v. 1.0.0)

## Limitations

- swapi-client cold cache and rate limiting
  (queuing would just make this complex)
- not web-scale, just a micro-service, replicate x-times 
 
## Warnings

- embedmongo-maven-plugin downloads MongoDB
- time/hardware/network/os may break things

## Bugs

- microshed-testing-core doesn't shutdown the java process if there is more than one java process
- microshed-testing-payara-micro is not working with podman
- embedmongo-maven-plugin doesn't support versions greater than 3.4
  sometimes socket does not close
- some others for sure (help me test more)
