start:
make up
or
make r

docks:
local - http://localhost:8081/swagger-ui/index.html
container - http://localhost:8080/swagger-ui/index.html

EXCHANGE_ACCESS_KEY=4a05d2fa091f37e5cd4e70ef589ad8ae (limited calls for get rate (fixer.io) for free)

properties:
exchange.cron_expression=0 0 * * * * (every 1 hour)
exchange.cron_expression=0 * * * * * (every 1 minute)