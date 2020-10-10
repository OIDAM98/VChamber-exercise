# Vestwell Coding Excercise

> A collection of particles is contained in a linear chamber. They all have the same speed, but some are headed toward the right and others are headed toward the left. These particles can pass through each other without disturbing the motion of the particles, so all the particles will leave the chamber relatively quickly.

## Approach
Using the `Chamber`'s `init` string, all the particles are obtained along with their `index` inside it. This yields a `List[(Char, Int)]` where the `Char` is the particle. This list of particles will be the one to iterated over for the animation's steps. 

Inside the `Chamber` class and its `animate` method, `require` was used to determine if both their inputs meet the requirements specified. Instead of throwing `IllegalArgumentException`s, `Either[String, A]` would have been a more idiomatic way of handling these exceptions.

### Chamber

A `Class` was created for the modeling of the problem.
It contains a `constructor` that receives an `init` `String`. Both the `constructor` and `init` are `private` to encapsulte the inner state of the class and to prevent external tinkering.

The class contains a public `method`, called `animate`, that will return an `Array` of the computed animations
Along with it, different `private methods` were created as helper methods for reaching the solution of the problem.

#### Companion Object

A `Companion Object` was created in order to facilitate a factory method to create instances of `Chamber`.

One `private attribute` was used to model the beginning of a step inside the animation process. This attribute is named `EMPTY_ANIMATION` and is generated with `"." * init.length` as its value.

### Animate

First all the particles of the `init` string are obtained, along with their index. Down the line, a particle is represented as a `(Char, Int)` tuple. Also, the initial representation for the `Chamber` is generated, where a character `X` is the position of a particle or `.` if it is an empty space, creating the base string for the animation.

The function `loop` is called with both of these values as its initial parameters.

#### Loop

`def loop(stepParticles: List[(Char, Int)], animations: List[String]): List[String]`

This is the function that creates the Strings that represent the animation process.

It is defined as an `inner function` of `animate`, and uses recursion. It has two parameters: the list of particles to evaluate for the current animation step `stepParticles`, and a list of all the computed animations before this step `animations`.

The `animations` parameter is used as an accumulator and the call to this function is the last action inside it. This means that `tail recursion` was able to be used for optimization.

To create the animation, the following steps are performed:
1. If there are no more particles for the step, then return the computed animations
2. Else
   1. Start with an empty list of particles, named `validParticles` and a string, named `animation`, starting as `EMPTY_ANIMATION`.
   2. For each particle inside this list
      1. Check if the particle moves right or left
      2. Calculate its new position depending on the speed of the animation
      3. Check if the particle has left the chamber
         1. True
            1. If there's not an `X` already in the new position, then add it to `animation`.
            2. Add this particle and its new position to `validParticles` for the next animation step.
         2. False
            1. Do not include it for the next animation step.
   3. Repeat the process with the filled `validParticles` list and prepend `animation` to the list of calculated animations. (Call again `loop`)


This function returns the calculated steps for the animation. Its elements are backwards, the last element is the initial instant of the animation. It needs to be `reverse`d before returning it as an `Array`.

## Tests
Different tests were created to ensure the correct functionality of both the creation of a `Chamber` instance and the `animate` method.

For `animate`, the cases that were given as examples inside the `description` file were used as test cases.

## Scaladocs

Documentation of the `methods` created and the `Chamber` class was written using `Scala Docs`.

## Running the program

The program is meant to be run as a `Docker` container, that executes the testing suite inside the `Main` of the application.

The `Dockerfile` is created using the `sbt` plugin `sbt-native-packager`, with `openjdk:8u201-jre-alpine3.9` as its base image to reduce the size. This plugin works by using the `Main` method of the application and using it as the entrypoint for the container. 

Just for this demonstration, the test suite was moved from `src/test` to `src/main/chamber/test` as it was required to execute the tests from the main method. Normally these tests would be executed from `sbt test`.

At least in `Windows`, this plugin requires a `Docker Hub` account to work. 

The command `sbt docker:stage` outputs the `Dockerfile` to the `target/docker` folder. This command is not required for the execution of the application but can be used to analyze the generated file.

### Script

A `bash` script was created to run the application as a Docker container. It is found in the root directory of the application and can be run with `bash ./runDocker.sh` to test the library.

It performs the following steps:

- Using `sbt docker:publishLocal` creates the container image and publishes it to the local repository of `Docker`.
- Runs the generated image, with name `chamber-tests`.
  - The container executes the test suite and outputs the results to the `Standard Output`.

- Using the container's ID:
  - removes the stopped container, with `docker rm $ID`
  - cleans the image using `sbt docker:clean`.