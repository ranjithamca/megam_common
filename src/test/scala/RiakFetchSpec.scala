/*
** Copyright [2012] [Megam Systems]
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
 * @author rajthilak
 *
 */
import io.megam.common.amqp._
import io.megam.common.riak._
import org.specs2._
import scalaz._
import Scalaz._
import org.specs2.mutable._
import org.specs2.Specification
import org.specs2.matcher.MatchResult

import com.stackmob.scaliak._
import com.basho.riak.client.core.query.indexes.{ RiakIndexes, StringBinIndex, LongIntIndex }
import com.basho.riak.client.core.util.{ Constants => RiakConstants }

class RiakFetchSpec extends Specification {

  def is =
    "RiakFetchSpec".title ^ end ^
      """
  Riak Fetch client which interfaces with Riak datasource
    """ ^ end ^
      "The Riak fetch spec Should" ^
      "Correctly print fetch result for account " ! AccountFetch.succeeds ^
      end

  private lazy val ScaliakTestPool = Scaliak.clientPool(List("localhost"))

  private lazy val riak: GSRiak = new GSRiak("localhost", "samplenodes")(ScaliakTestPool)

  case object AccountFetch {

    val metadataKey = "Field"
    val metadataVal = "1002"
    val bindex = "email"
    val bvalue = Set("")

    def succeeds = {
      val t: ValidationNel[Throwable, List[String]] = riak.fetchIndexByValue(new GunnySack("email", "megam@mypaas.io", RiakConstants.CTYPE_TEXT_UTF8, None, Map(metadataKey -> metadataVal), Map((bindex, bvalue))))
      println("-->" + t)
      t.toOption must beSome.which { _.contains("key14")}

    }
  }

}
