/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package businessrates.authorisation

import businessrates.authorisation.models.{Organisation, Person, PersonDetails}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

trait ArbitraryDataGeneration {

  implicit def getArbitrary[T](t: Gen[T]): T = t.sample.get

  def randomShortString: Gen[String] = Gen.listOfN(20, Gen.alphaNumChar).map(_.mkString)
  def randomNumericString: Gen[String] = Gen.listOfN(20, Gen.numChar).map(_.mkString)
  def randomPositiveLong: Gen[Long] = Gen.choose(0L, Long.MaxValue)

  def randomEmail: Gen[String] = for {
    mailbox <- randomShortString
    domain <- randomShortString
    tld <- randomShortString
  } yield s"$mailbox@$domain.$tld"

  def randomOrganisation: Gen[Organisation] = for {
    id <- arbitrary[Int]
    groupId <- randomShortString
    companyName <- randomShortString
    addressId <- arbitrary[Int]
    email <- randomEmail
    phone <- randomNumericString
    isSmallBusiness <- arbitrary[Boolean]
    isAgent <- arbitrary[Boolean]
    agentCode <- randomPositiveLong
  } yield Organisation(id, groupId, companyName, addressId, email, phone, isSmallBusiness, isAgent, agentCode)

  private implicit val arbitraryOrganisation: Arbitrary[Organisation] = Arbitrary(randomOrganisation)

  def randomPersonDetails: Gen[PersonDetails] = for {
    firstName <- randomShortString
    lastName <- randomShortString
    email <- randomEmail
    phone1 <- randomNumericString
    phone2 <- Gen.option(randomNumericString)
    addressId <- arbitrary[Int]
  } yield PersonDetails(firstName, lastName, email, phone1, phone2, addressId)

  private implicit val arbitraryPersonDetails: Arbitrary[PersonDetails] = Arbitrary(randomPersonDetails)

  def randomPerson: Gen[Person] = for {
    externalId <- randomShortString
    trustId <- randomShortString
    organisationId <- randomPositiveLong
    individualId <- randomPositiveLong
    details <- arbitrary[PersonDetails]
  } yield Person(externalId, trustId, organisationId, individualId, details)

  private implicit val arbitraryPersonGenerator: Arbitrary[Person] = Arbitrary(randomPerson)
}