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

import com.amazonaws.auth.*
import com.amazonaws.services.elasticbeanstalk.*
import com.amazonaws.services.elasticbeanstalk.model.*

/**
* @author Kenneth Liu
*/

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsPackage") //needed to access application settings

//global script variables
//def awsCredentials
//def elasticBeanstalk
//def applicationName
//def environmentName



target(initPlugin: '') {

}

target(initElasticBeanstalkClient: 'Create an instance of the Elastic Beanstalk client - AWS credentials required') {
	depends(initPlugin, loadAwsCredentials, initTargetApplicationAndEnvironmentConfig)
	println "initializing ElasticBeanstalk client"
    elasticBeanstalk = new AWSElasticBeanstalkClient(awsCredentials)

    //setup non-default endpoint URL, if configured
    def endpointUrl = getEndpointUrl()
    if (endpointUrl) {
		elasticBeanstalk.endpoint = endpointUrl
    }
}

target(initTargetApplicationAndEnvironmentConfig: 'Loads application and environment properties') {
	depends(compile, createConfig)
	applicationName = getApplicationName() //set global
	environmentName = getEnvironmentName() //set global
}

/**
* @author Kenneth Liu
*/

target(loadAwsCredentials: 'Load AWS credentials from a file or from env') {
	println "loading AWS credentials"
	def credentials = getAwsCredentialsFromPropertiesFile()
	if (!credentials) credentials = getAwsCredentialsFromSystemProperties()
	awsCredentials = credentials //set global property
}

/**
* @return null if properties not found
*/
private AWSCredentials getAwsCredentialsFromSystemProperties() {
	def accessKey = System.getProperty('AWSAccessKeyId')
	def secretKey = System.getProperty('AWSSecretKey')
	println 'Loading credentials from System properties'

	if (!accessKey || !secretKey) return null //TODO log helpful error message

	new BasicAWSCredentials(accessKey, secretKey)
}

/**
* see: http://aws.amazon.com/articles/3586
* see: http://docs.aws.amazon.com/elasticbeanstalk/latest/dg/usingCLI.html
*/
private AWSCredentials getAwsCredentialsFromPropertiesFile() {
	def credentialFile = System.getenv('AWS_CREDENTIAL_FILE')
	println 'Loading credential file from: ' + credentialFile

	if (!credentialFile) return null //TODO log helpful error message

	//TODO handle case where env var is set, but file is not there

	//new PropertiesCredentials(new File(credentialFile)) //TODO handle possible exceptions

	def props = new Properties()
	new File(credentialFile).withInputStream { props.load(it) }
	new BasicAWSCredentials(props.AWSAccessKeyId, props.AWSSecretKey)
}

private AWSCredentials getAwsCredentialsFromGrailsConfig() {

}


private String getApplicationName() {
    def name = config.grails?.plugin?.awsElasticBeanstalk?.applicationName 
    if (!name) name = System.getProperty('awsElasticBeanstalk.applicationName')
    name ?: metadata.'app.name'
}

private String getEnvironmentName() {
    def name = config.grails?.plugin?.awsElasticBeanstalk?.environmentName
    if (!name) name = System.getProperty('awsElasticBeanstalk.environmentName')
    name ?: metadata.'app.name' + '-default' //the name of the default environment used in the AWS Console
    //FIXME this should be unique to account - needs to be truncated? - appname must be between 4 and 23 chars long
}

private String getEndpointUrl() {
    def url = config.grails?.plugin?.awsElasticBeanstalk?.serviceEndpointUrl
    if (!url) url = System.getProperty('awsElasticBeanstalk.serviceEndpointUrl')
    if (!url) return getServiceEndpointFromEnv()
}

private String getServiceEndpointFromEnv() {
	//see http://docs.aws.amazon.com/elasticbeanstalk/latest/dg/usingCLI.html
	def endpointEnvVar = System.getenv('ELASTICBEANSTALK_URL') //for compatibility with EB API CLI
}

