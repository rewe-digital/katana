Speed comparison of the three DI libraries Katana, [Koin](https://github.com/InsertKoinIO/koin) and [Kodein](https://github.com/Kodein-Framework/Kodein-DI).

Also see this independent [comparison](https://github.com/Sloy/android-dependency-injection-performance).

## Test setup

Please have a look at the source code. The test is divided into the two blocks *setup* and *execution*. *Setup* tests 
how fast the test subject is in setting up the dependency injection (creating modules, bindings, etc.) and *execution*
tests the actual injection. The test collects the average and median time in nanoseconds of 1,000,000 iterations of
each block.

### Library versions

| Library | Version |
| ------- | ------- |
| Katana  | 1.7.1   |
| Koin    | 2.0.1   |
| Kodein  | 6.3.3   |

For fairness towards Kodein the [erased](http://kodein.org/Kodein-DI/?6.0.1/getting-started#_flavour) version of Kodein
was used for this comparison. It should be faster since it doesn't use reflection.

## Test environment

The test has been performed on a 2017 MacBook Pro with a 3,1 GHz Intel Core i7 CPU, 16 GB RAM, macOS Mojave 10.14.6 and
Java 1.8.0_222.

## Results

All times in nanoseconds.

| Library | Setup (average) | Setup (median) | Execution (average) | Execution (median) |
| ------- | ---------------:| --------------:| -------------------:| ------------------:|
| Katana  |      793.377251 |          527.0 |          259.320590 |              181.0 |
| Koin    |      969.294944 |          509.0 |          370.726363 |              254.0 |
| Kodein  |     1629.850542 |          951.0 |          809.621470 |              566.0 |

## How to build and run comparison

From the root folder of the project run:

```
./gradlew :speed-comparison:build
java -jar ./speed-comparison/build/libs/speed-comparison-fat.jar
```
