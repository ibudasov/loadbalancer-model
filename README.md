# Load Balancer

It's a programmatic model of a load balancer, which simulates real life load balancer which:

- holds up to 10 providers
- has 2 algorithms of getting these providers: `InvocationAlgorithmRandom` and `InvocationAlgorithmRoundRobin`
- supports manual inclusion and exclusion of providers
- checks heart bit of providers, to make sure they are able to serve the requests
- providers also support recovery when possible
- the load balancer will not process more requests then it can process

## Todo

- Abstract away randomness out of `InvocationAlgorithmRandom` to improve testability (currently one test is falling with
  some probability)
- Create a simple CLI application which would nicely demonstrate how it works 
