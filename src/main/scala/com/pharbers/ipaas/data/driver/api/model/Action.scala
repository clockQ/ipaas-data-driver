/*
 * This file is part of com.pharbers.ipaas-data-driver.
 *
 * com.pharbers.ipaas-data-driver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * com.pharbers.ipaas-data-driver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.pharbers.ipaas.data.driver.api.model

/** iPaas Driver Action 运行实体
  *
  * @author clock
  * @version 0.1
  * @since 2019/06/14 10:00
  * @note
  */
case class Action() extends Model {
    /** Action 包含的 Operators
      *
      * @author clock
      * @version 0.1
      * @since 2019/06/14 11:30
      * @note
      */
    var opers: java.util.List[Operator] = _

    def getOpers: java.util.List[Operator] = opers

    def setOpers(opers: java.util.List[Operator]): Unit = this.opers = opers
}