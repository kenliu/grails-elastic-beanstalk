/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.kenliu.awsebplugin

import spock.lang.Specification
import spock.lang.Ignore

class BeanstalkDeployerSpec extends Specification {

	def cut = new BeanstalkDeployer()

	def "should generate description using default template"() {
		given:
		def now = Date.parse('yyyy-MM-dd_HH-mm-ss', '2013-11-27_18-43-33')

		when:
		def desc = cut.generateVersionDescription(null, now)

		then:
		desc == 'Deployed on 27 Nov 2013 23:43:33 GMT from Grails AWS Elastic Beanstalk Plugin'
	}

	def "should generate description using default template, passing empty map"() {
		given:
		def now = Date.parse('yyyy-MM-dd_HH-mm-ss', '2013-11-27_18-43-33')

		when:
		def desc = cut.generateVersionDescription([:], now)

		then:
		desc == 'Deployed on 27 Nov 2013 23:43:33 GMT from Grails AWS Elastic Beanstalk Plugin'
	}


	def "should generate description using custom template"() {
		given:
		def now = Date.parse('yyyy-MM-dd_HH-mm-ss', '2013-11-27_18-43-33')
		def template = 'Deployed: ${deploymentDate.toGMTString()}'

		when:
		def desc = cut.generateVersionDescription(template, now)

		then:
		desc == 'Deployed: 27 Nov 2013 23:43:33 GMT'
	}

	def "should generate version label using default template"() {
		given:
		def mockWarFile = new Expando()
		mockWarFile.lastModified = { Date.parse('yyyy-MM-dd_HH-mm-ss', '2013-11-27_18-43-33').time }

		when:
		def label = cut.generateVersionLabel(null, mockWarFile, '1.2')

		then:
		label == '2013-11-27_18-43-33'
	}

	def "should generate version label using default template, passing empty map"() {
		given:
		def mockWarFile = new Expando()
		mockWarFile.lastModified = { Date.parse('yyyy-MM-dd_HH-mm-ss', '2013-11-27_18-43-33').time }

		when:
		def label = cut.generateVersionLabel([:], mockWarFile, '1.2')

		then:
		label == '2013-11-27_18-43-33'
	}


	def "should generate version label using custom template"() {
		given:
		def mockWarFile = new Expando()
		mockWarFile.lastModified = { Date.parse('yyyy-MM-dd_HH-mm-ss', '2013-11-27_18-43-33').time }
		def template = '''${applicationVersion.endsWith('SNAPSHOT') ? applicationVersion + '-' + warTimestampDate : applicationVersion}'''

		when:
		def label = cut.generateVersionLabel(template, mockWarFile, '1.2-SNAPSHOT')

		then:
		label == '1.2-SNAPSHOT-2013-11-27_18-43-33'
	}


}