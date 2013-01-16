import com.amazonaws.auth.*

/**
* @author Kenneth Liu
*/

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

target(loadAwsCredentials: 'Load AWS credentials from a file or from env') {
	awsCredentials = getAwsCredentialsFromPropertiesFile()
}