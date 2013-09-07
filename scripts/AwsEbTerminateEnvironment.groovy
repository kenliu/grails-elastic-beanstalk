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

import com.amazonaws.services.elasticbeanstalk.*
import com.amazonaws.services.elasticbeanstalk.model.*

/**
* @author Kenneth Liu
*/

includeTargets << new File(awsElasticBeanstalkPluginDir, "scripts/_AwsEbCommon.groovy")

target(awsEbTerminateEnvironment: "Terminate a running AWS Elastic Beanstalk environment") {
	depends(initElasticBeanstalkClient)

    def targetEnvironment = this.environmentName

    //TODO first check that environment exists

	def result = elasticBeanstalk.terminateEnvironment(new TerminateEnvironmentRequest().withEnvironmentName(targetEnvironment))
	println "Environment terminating: ${environmentName}"
	println result
}

setDefaultTarget(awsEbTerminateEnvironment)