package chamber

class Chamber private (private val init: String) {
  require(init.nonEmpty && init.length <= 50, "init must be between 1 and 50 characters inclusive.")

  def animate(speed: Int): Array[String] = ???
}

object Chamber {
  def apply(init: String) = new Chamber(init)
}
