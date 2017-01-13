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

package businessrates.authorisation.connectors

import com.google.inject.Inject
import play.api.libs.json.JsValue
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.play.http.{HeaderCarrier, HttpGet, NotFoundException}

import scala.concurrent.{ExecutionContext, Future}

class IndividualAccounts @Inject()(http: HttpGet)(implicit ec: ExecutionContext) extends ServicesConfig {

  type PersonId = Int

  lazy val url = baseUrl("data-platform")

  def getPersonId(externalId: String)(implicit hc: HeaderCarrier): Future[Option[PersonId]] = {
    http.GET[JsValue](s"$url/person?governmentGatewayExternalId=$externalId") map { js =>
      (js \ "id").asOpt[PersonId]
    } recover {
      case _: NotFoundException => None
    }
  }
}
