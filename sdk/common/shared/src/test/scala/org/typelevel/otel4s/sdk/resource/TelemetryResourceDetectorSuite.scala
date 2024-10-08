/*
 * Copyright 2023 Typelevel
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

package org.typelevel.otel4s.sdk.resource

import cats.effect.IO
import munit.CatsEffectSuite
import org.typelevel.otel4s.semconv.SchemaUrls

class TelemetryResourceDetectorSuite extends CatsEffectSuite {

  test("HostDetector - detect name and arch") {
    val keys = Set("host.name", "host.arch")

    for {
      resource <- HostDetector[IO].detect
    } yield {
      assertEquals(resource.map(_.attributes.map(_.key.name).toSet), Some(keys))
      assertEquals(resource.flatMap(_.schemaUrl), Some(SchemaUrls.Current))
    }
  }

  test("default - contain host detector") {
    val detectors = TelemetryResourceDetector.default[IO].map(_.name)
    val expected = Set("host")

    assertEquals(detectors.map(_.name), expected)
  }

}
