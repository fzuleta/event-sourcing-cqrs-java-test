# event-source-test
**Server**: RabbitMQ + Java micro services + Orientdb.

**Client**: Aurelia.

This is an experiment to:
- try out communicating between services using a queue message (instead of api-points|socket).
- store state as event source and cqrs.

## How it works
`sh buildandrun.sh` will build (in-order) the jar's for the microservices then it will start docker containers
`http://localhost:8888` will load the example client.

# Services
RabbitMQ handles communication between services

### apigateway
    - entry point from Client to Server.
    - dispatches messages to rabbitmq queues.

### eventstore
    - Is the database (orientdb embedded) and also listens for __storeevent__ and keeps a record.
    - when **users** or **money** services start, they request all events to put the memorydb up to date

### users
    - when loads requests all events since begining of time to **eventstore**
    - listens to queue __user.user__

### money
    - when loads requests all events since begining of time to **eventstore**
    - listens to queue __user.money__

![Image](resources/server-setup.png)

### Todo
    - Snapshots of state
    - Fix stuff :)