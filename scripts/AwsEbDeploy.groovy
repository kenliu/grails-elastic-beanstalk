import com.amazonaws.auth.*
import com.amazonaws.services.elasticbeanstalk.*
import com.amazonaws.services.elasticbeanstalk.model.*
import com.amazonaws.services.s3.*

/**
* @author Kenneth Liu
*/

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsWar")
includeTargets << grailsScript("_GrailsPackage") //needed to access application settings
includeTargets << new File("${awsElasticBeanstalkPluginDir}/scripts/_AwsAuthentication.groovy")


target(main: "Deploy Grails WAR file to AWS Elastic Beanstalk") {
	depends(loadAwsCredentials, compile, createConfig, configureWarName)
    println "script metadata: ${metadata}"
    println "grails settings warname ${grailsSettings.projectWarFile}"
    println "war file name: ${warName}"

    println 'Starting AWS Elastic Beanstalk deployment'

    def credentials = awsCredentials
    AWSElasticBeanstalk elasticBeanstalk = new AWSElasticBeanstalkClient(credentials)

    //TODO optionally set region here

    uploadWarToS3()

    //TODO handle case where application does not yet exist - check application? (don't want to autocreate - disable autocreate flag?)
    //TODO handle case where target environment does not yet exist - check environment?

    //create a new application version
    println "Create application version with uploaded application"
    println "applicationName: ${applicationName}"
    println "environmentName: ${environmentName}"
    def createApplicationRequest = new CreateApplicationVersionRequest(
        applicationName: applicationName, versionLabel: versionLabel,
        description: description,
        autoCreateApplication: true, sourceBundle: new S3Location(bucketName, key)
    )
    def createApplicationVersionResult = elasticBeanstalk.createApplicationVersion(createApplicationRequest)
    println "Created application version $createApplicationVersionResult"

    //deploy the deployed version to an existing environment
    println "Updating environment with uploaded application version"
    def updateEnviromentRequest = new UpdateEnvironmentRequest(environmentName:environmentName, versionLabel:versionLabel)
    def updateEnviromentResult = elasticBeanstalk.updateEnvironment(updateEnviromentRequest)
    println "Updated environment $updateEnviromentResult"

}

setDefaultTarget(main)


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

private String getDescription() {
    //TODO add customization of description using template
    "deployed on ${new Date()} from grails AWS Elastic Beanstalk Plugin"
}

private String getVersionLabel() {
    //TODO provide for alternate algorithms for generating version label
    //def applicationVersion = metadata.getApplicationVersion()
    def label = getWarTimestamp(appWarFile)
    println "version label: ${label}"
    label
}

private File getAppWarFile() {
    //println "loading WAR file from basedir: ${basedir}"
    //println "war file name: ${warName}"
    new File(warName)
}

private String getWarTimestamp(File warFile) {
    def warDate = new Date(warFile.lastModified())
    warDate.format('yyyy-MM-dd_HH-mm-ss') //same as Jenkins BUILD_ID format
}

private uploadWarToS3() {
    println "Finding S3 bucket to upload WAR"

    //TODO log bucket creation
    String bucketName = elasticBeanstalk.createStorageLocation().getS3Bucket()
    println "S3 bucket name: ${bucketName}"
    def uploadFilename = uniqueTempWarFileName(appWarFile)

    println "Uploading WAR file: ${uploadFilename}"
    String key = URLEncoder.encode(uploadFilename, 'UTF-8')

    println "Uploading WAR to S3"
    AmazonS3 s3 = new AmazonS3Client(credentials)
    def s3Result = s3.putObject(bucketName, key, appWarFile)
    println "Uploaded WAR ${s3Result.versionId}"
}

/**
* Generates a unique file name for the uploaded WAR file in S3, based on the file's timestamp.
*/
private String uniqueTempWarFileName(warFile) {
    def uuid = Long.toHexString(warFile.lastModified())
    "${uuid}-${warFile.name}"
}

