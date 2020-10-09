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
    * @param letter
    * @return
    */
  private def isValidLetter(letter: Char): Boolean = letter == 'R' || letter == 'L'

  /**
    * Returns the animation operation depending on the given particle.
    *
    * @param letter Current letter of the animation
    * @param curr Current index of the letter
    * @param speed Speed of the animation
    * @return A function that correctly calculates the new position of the given particle.
    */
  private def animateOperation(letter: Char): (Int, Int) => Int =
    if (letter == 'L')
      (curr: Int, speed: Int) => curr - speed
    else
      (curr: Int, speed: Int) => curr + speed

  /**
    * Determines if the position can be inside the Init string.
    *
    * Returns true if the position given is inside the Init string
    * @param pos Position to evaluate
    * @return
    */
  private def isValidPosition(pos: Int): Boolean = pos >= 0 && pos < init.length

  /**
    * Return all the valid letters for the animation and their postion inside
    * the initial condition.
    *
    * @return A List of valid letters in the inital condition
    */
  private def getValidLetters(): List[(Char, Int)] =
    init.zipWithIndex.filter { case (letter, _) => isValidLetter(letter) }.toList

  /**
    * Returns the animation process starting from the class' intial
    * condition, determined by the given speed.
    *
    * @param speed
    * @return An Array containing each step of the animation as a String, where
    *   each 'X' determines a particle.
    */
  def animate(speed: Int): Array[String] = {
    require(speed > 0 && speed <= 10, "speed must be between 1 and 10.")

    // Filter out only the letters and their initial position from init
    val letters = getValidLetters()

    val initialState = init.map(char => if (isValidLetter(char)) 'X' else '.') :: Nil

    @tailrec
    def loop(stepLetters: List[(Char, Int)], animations: List[String]): List[String] = {
      // Base case: All letters have been animated succesfully
      if (stepLetters.isEmpty) animations // Return calculated animations
      else {
        // Intial step for the animation
        val step         = new StringBuilder(EMPTY_ANIMATION)
        val validLetters = ListBuffer.empty[(Char, Int)]

        for ((letter, currentPosition) <- stepLetters) {
          // Get the correct operation for the current letter
          val operation = animateOperation(letter)
          // Calculate new position
          val newPosition = operation(currentPosition, speed)

          if (isValidPosition(newPosition)) { // New position is inbounds of Init
            // Do nothing with the step if there's already a particle
            if (step(newPosition) != 'X')
              step(newPosition) = 'X'

            // Add the letter and its new possition as a valid letter for next iteration
            validLetters += (letter -> newPosition)
          }
        }

        // Call recursion with valid letters and adding the animated step to the animations
        loop(validLetters.toList, step.toString :: animations)
      }
    }

    // Convert animations into array and return them
    loop(letters, initialState).reverse.toArray
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
