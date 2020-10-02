package imperative.modular

import imperative.modular.movingStats.OptionalStat
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.Queue

class TestLineCountImperative extends AnyWordSpec {

  /** Creates a (mutable!) SUT instance. */
  def createSUT() = new movingStats with OutputToBuffer[movingStats.Stats] {

  }

  "An imperative LineCounter" when {
    "given an empty iterator" should {
      "produce an empty output" in {
        val args = new Array[String](2)
        // create SUT instance for this test case
        val sut = createSUT()
        // exercise SUT
        sut.run(Iterator.empty, args)
        // check effect on output observer
        assert(sut.getResults.isEmpty)
      }
    }

    "given a nonempty iterator" should {
      "produce the correct nonempty output" in {
        // input data for this test case
        val args = new Array[String](2)
        val data = Seq("3", "4", "5", "6")
        // create SUT instance for this test case
        val sut = createSUT()
        // exercise SUT
        sut.run(data.iterator, args)
        // check effect on output observer
        assert(sut.getResults === (1 to data.length).zip(data))
      }
    }
  }

  "given a nonempty iterator" should {
    "exhibit the correct interactive behavior" in {
      // input data for this test case
      val input = Iterator("3", "4", "5", "6")
      val args = Array("3")
      // create SUT instance for this test case
      val sut = new movingStats with Tracing[String, movingStats.Stats] {
        //override def run(input: Iterator[String], args: Array[String]): Unit = ???
      }
      // exercise SUT
      sut.run(input, args)

      // check correctness of resulting interactions
      import sut.{ InputEvent => i, OutputEvent => o }
      assert(sut.trace === Seq(
        i("3"), o((3.0, 1, Seq(Option("? ? ? ?"))),
        i("2"), o((2.0, 2, Seq(Option("? ? ? ?"))),
        i("5"), o((5.0, 3, Seq(Option(2, 3.3, 5, 0.81649658092773))),
        i("6"), o((6.0, 4, Seq(Option(2, 4, 6, 1.1180339887499))))
              )
            )
          )
        )
      )

    }
  }
}