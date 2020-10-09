package chamber

import scala.collection.mutable.ListBuffer
import scala.annotation.tailrec

/**
  * A lineal Chamber containing a collection of particles determined by the initial condition.
  * The particles have the same speed, but some are headed toward the right and others
  * are headed toward the left.
  *
  * @param init Initial conditions of the particles inside the Chamber
  */
class Chamber private (private val init: String) {
  require(init.nonEmpty && init.length <= 50, "init must be between 1 and 50 characters inclusive.")

  // Inital string value for a starting animation step
  private val EMPTY_ANIMATION = "." * init.length

  /**
    * Returns true if the given letter can be animated ('R' or 'L').
    *
    * @param letter Character to validate
    * @return if the given letter is a particle
    */
  private def isValidParticle(letter: Char): Boolean = letter == 'R' || letter == 'L'

  /**
    * Returns the animation operation depending on the given particle.
    *
    * @param particle Current particle of the animation
    * @param curr Current index of the particle
    * @param speed Speed of the animation
    * @return A function that correctly calculates the new position of the given particle.
    */
  private def animateOperation(particle: Char): (Int, Int) => Int =
    if (particle == 'L')
      (curr: Int, speed: Int) => curr - speed
    else
      (curr: Int, speed: Int) => curr + speed

  /**
    * Returns true if the position given is inside the Chamber's space.
    *
    * @param pos Position to evaluate
    * @return if the position is inside the Init string
    */
  private def isValidPosition(pos: Int): Boolean = pos >= 0 && pos < init.length

  /**
    * Return all the valid particles for the animation and their postion inside
    * the initial condition.
    *
    * @return A List of valid particles in the inital condition
    */
  private def getParticles(): List[(Char, Int)] =
    init.zipWithIndex.filter { case (letter, _) => isValidParticle(letter) }.toList

  /**
    * Returns the initial conditions given to the Chamber as an
    * animation step.
    *
    * @return A String representing the initial conditions as an animation
    */
  private def generateInitialStep(): String = init.map(char => if (isValidParticle(char)) 'X' else '.')

  /**
    * Returns the animation process starting from the class' intial
    * condition, determined by the given speed.
    *
    * @param speed the Speed for each step of the animation
    * @return An Array containing each step of the animation as a String, where
    *   each 'X' determines a particle.
    */
  def animate(speed: Int): Array[String] = {
    require(speed > 0 && speed <= 10, "speed must be between 1 and 10.")

    // Filter out only the particles and their initial position from init
    val particles = getParticles()

    val initialState = generateInitialStep() :: Nil

    @tailrec
    def loop(stepParticles: List[(Char, Int)], animations: List[String]): List[String] = {
      // Base case: All particles have been animated succesfully
      if (stepParticles.isEmpty) animations // Return calculated animations
      else {
        // Generate step for the animation

        /*
          Start with empty list of particles and empty animation step.
          Fill the list with valid particles, along with their new position, that
          will be used for the next step and animate this step.
            - A valid particle is one that is still inside the chamber's space.
         */
        val (validParticles, animation) =
          stepParticles.foldRight((List.empty[(Char, Int)], EMPTY_ANIMATION)) {
            case ((particle, currentPosition), (validParticles, animation)) =>
              // Get the correct operation for the current particle
              val operation = animateOperation(particle)
              // Calculate new position
              val newPosition = operation(currentPosition, speed)

              if (isValidPosition(newPosition)) { // New position is inbounds of initial space
                // Do nothing with the step if there's already a particle
                val stepAnimation =
                  if (animation(newPosition) != 'X')
                    animation.updated(newPosition, 'X')
                  else animation

                // Add the particle and its new possition as a valid particle for next iteration
                val newValidParticles = (particle, newPosition) :: validParticles

                (newValidParticles, stepAnimation)
              } else (validParticles, animation) // Don't add particle for next iteration
          }

        // Call recursion with valid particles and adding the animated step to the animations
        loop(validParticles, animation :: animations)
      }
    }

    // Convert animations into array and return them
    loop(particles, initialState).reverse.toArray
  }
}

/**
  * Factory for [[chamber.Chamber]] intances.
  */
object Chamber {

  /**
    * Creates a Chamber with the given starting state of particles.
    *
    * @param init
    * @return a new Chamber instance with the given starting string
    */
  def apply(init: String) = new Chamber(init)
}
