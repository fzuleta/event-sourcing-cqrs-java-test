# event-source-test
**Server**: RabbitMQ + Java micro services + Orientdb.

**Client**: Aurelia.

This is an experiment to:
- try out communicating between services using a queue message (instead of api-points|socket).
- store state as event source and cqrs.

## How it works
`sh buildandrun.sh` will build (in-order) the jar's for the microservices then it will start docker containers
`http://localhost:8888` will load the example client.

#### IMPORTANT NOTE

**Embedded Orientdb in Docker is not working: to run the project:**
- run `docker-compose up` to have rabbitmq
- run `sh build.sh`
- run each server/{name}/target/{name}.jar with: `java -jar {name}.jar`

# Services
RabbitMQ handles communication between services

### apigateway
- entry point from Client to Server.
- dispatches messages to rabbitmq queues and listens back to chain actions.

### eventstore
- This is the database (orientdb embedded) and also listens for **eventstore queue** and stores a record in disk.
- when **users** or **money** services start, they request all events to put the memorydb up to date

### users
- when loads requests all events since begining of time from **eventstore**
- listens to queue **user.user**

### money
- when loads requests all events since begining of time from **eventstore**
- listens to queue **user.money**

![Image](resources/server-setup.png)

### Todo
- Snapshots of state
- Fix stuff :)