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
package io.megam.common.uid

import scalaz._
import Scalaz._
import scalaz.Validation
import scalaz.Validation.FlatMap._
import scalaz.NonEmptyList._
import io.jvm.uuid._

/**
 * @author ram
 *
 */
class UID(agent: String) {


  def get: ValidationNel[Throwable, UniqueID] = {
    (Validation.fromTryCatchThrowable[Long,Throwable] {
      io.jvm.uuid.UUID.random.leastSigBits.abs
    } leftMap { t: Throwable =>
      new Throwable(
        """Unique id random gen failure for 'agent:' '%s'""".format(agent).stripMargin + "\n ", t)
    }).toValidationNel.flatMap { i: Long => Validation.success[Throwable, UniqueID](UniqueID(agent, i)).toValidationNel }
  }
}

object UID {

  def apply(agent: String) = new UID(agent)

}
