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

/**
* @author Kenneth Liu
*/


target(loadAwsCredentials: 'Load AWS credentials from a file or from env') {
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
