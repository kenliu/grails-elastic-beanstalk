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

import groovy.text.SimpleTemplateEngine

class BeanstalkDeployer {

	String generateVersionDescription(descriptionTemplate, Date deploymentDate = new Date()) {
		//TODO use ISO date format here?
		def defaultDescriptionTemplate = 'Deployed on ${deploymentDate.toGMTString()} from Grails AWS Elastic Beanstalk Plugin'

		def binding = [deploymentDate: deploymentDate]
		def template = descriptionTemplate ?: defaultDescriptionTemplate
		new SimpleTemplateEngine().createTemplate(template).make(binding).toString()
	}

	String generateVersionLabel(versionLabelTemplate, warFile, applicationVersion) {
		def defaultVersionTemplate = '${warTimestampDate}'

		def template = versionLabelTemplate ?: defaultVersionTemplate
		def binding = [
			warTimestampDate: getWarTimestamp(warFile),
			applicationVersion: applicationVersion
		]

		String generated = new SimpleTemplateEngine().createTemplate(template).make(binding).toString()
	}

	private String getWarTimestamp(warFile) {
		def warDate = new Date(warFile.lastModified())
		warDate.format('yyyy-MM-dd_HH-mm-ss') //same as Jenkins BUILD_ID format
	}

}