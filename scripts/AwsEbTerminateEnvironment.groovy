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

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsWar")
includeTargets << grailsScript("_GrailsPackage") //needed to access application settings
includeTargets << new File(awsElasticBeanstalkPluginDir, "scripts/_AwsAuthentication.groovy")

//TODO check what happens on a new installation before plugin is downloaded (breaks with new Grails version?)
target(awsEbTerminateEnvironment: "Terminate running AWS Elastic Beanstalk environment") {
	depends(loadAwsCredentials, compile, createConfig)

    AWSElasticBeanstalk elasticBeanstalk = new AWSElasticBeanstalkClient(awsCredentials)

    //TODO set endpoint here

    def targetEnvironment = this.environmentName
	def result = elasticBeanstalk.terminateEnvironment(new TerminateEnvironmentRequest().withEnvironmentName(targetEnvironment))
	println "Environment terminating: ${environmentName}"
	println result
}

setDefaultTarget(awsEbTerminateEnvironment)

//TODO refactor this between this script and the Deploy script
private String getEnvironmentName() {
    def name = config.grails?.plugin?.awsElasticBeanstalk?.environmentName
    if (!name) name = System.getProperty('awsElasticBeanstalk.environmentName')
    name ?: metadata.'app.name' + '-default' //the name of the default environment used in the AWS Console
    //FIXME this should be unique to account - needs to be truncated? - appname must be between 4 and 23 chars long
}
