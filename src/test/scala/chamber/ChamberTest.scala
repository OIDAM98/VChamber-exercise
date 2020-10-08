package chamber

import org.scalatest._
import funspec._
import matchers.should._

class ChamberTest extends AnyFunSpec with Matchers {
  describe("A Chamber") {

    describe("the animate method") {
      describe("should work correctly:") {
        it("Case 0") {
          val output = Array("..X....", "....X..", "......X", ".......")
          Chamber("..R....").animate(2) should equal(output)
        }
        it("Case 1") {
          val output = Array("XX..XXX", ".X.XX..", "X.....X", ".......")
          Chamber("RR..LRL").animate(3) should equal(output)
        }
        it("Case 2") {
          val output = Array(
            "XXXX.XXXX",
            "X..X.X..X",
            ".X.X.X.X.",
            ".X.....X.",
            "........."
          )
          Chamber("LRLR.LRLR").animate(2) should equal(output)
        }
        it("Case 3") {
          val output = Array("XXXXXXXXXX", "..........")
          Chamber("RLRLRLRLRL").animate(10) should equal(output)
        }
        it("Case 4") {
          val output = Array("...")
          Chamber("...").animate(1) should equal(output)
        }
        it("Case 5") {
          val output = Array("..X....", "....X..", "......X", ".......")
          Chamber("LRRL.LR.LRR.R.LRRL.").animate(1) should equal(output)
        }
      }
      it(
        "should throw IllegalArgumentException if speed is not between 1 and 10"
      ) {
        assertThrows[IllegalArgumentException] {
          Chamber("...").animate(0)
        }
      }
    }

    it(
      "should throw IllegalArgumentException if init is not between 1 and 50 characters"
    ) {
      assertThrows[IllegalArgumentException] {
        Chamber("")
      }
    }
  }
}
