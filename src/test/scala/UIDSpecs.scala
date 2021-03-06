/*
** Copyright [2012-2013] [Megam Systems]
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
** http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
*/
/**
 * @author subash
 *
 */
import io.megam.common.uid.UID
import io.megam.common.uid._
import org.specs2._
import scalaz._
import Scalaz._

import org.specs2.mutable._
import org.specs2.Specification
import org.specs2.matcher.MatchResult


class UIDSpecs extends Specification {

  def is =
    "UIDSpecs".title ^ end ^
      """
  UID is an implementation of TwitterSnowflakeId service
  """ ^ end ^
      "The UID Client Should" ^
      "Correctly return a Unique ID for agent act" ! UIDActNoneService().succeeds ^
      "Correctly return a Unique ID for agent ACT" ! UIDActService().succeeds ^
      end

  def execute[T](t: ValidationNel[Throwable, UniqueID], expectedPrefix: String)(fn: UniqueID => MatchResult[T]) = {
    val res = t match {
      case Success(uid) => uid
      case Failure(errThrown) => {
        UniqueID.empty
      }
    }
    res.get._1.toString must startWith(expectedPrefix) and fn(res)
  }

  protected def ensureUIDOk(h: UniqueID) = h.get._2 must beGreaterThan(0L)

  case class UIDActNoneService() {
    def succeeds = execute(UID("act").get, "ACT")(ensureUIDOk(_))
  }

  case class UIDActService() {
    def succeeds = execute(UID("ACT").get, "ACT")(ensureUIDOk(_))
  }

}
