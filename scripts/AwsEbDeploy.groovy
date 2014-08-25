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
import com.amazonaws.services.s3.*
import com.amazonaws.services.s3.model.*
import net.kenliu.awsebplugin.BeanstalkDeployer

/**
* @author Kenneth Liu
*/

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsWar")
//includeTargets << grailsScript("_GrailsPackage") //needed to access application settings
includeTargets << new File(awsElasticBeanstalkPluginDir, "scripts/_AwsEbCommon.groovy")

//TODO check what happens on a new installation before plugin is downloaded (breaks with new Grails version?)

deployer = new BeanstalkDeployer()

USAGE = """
grails aws-eb-deploy
"""
target(main: "Deploy Grails WAR file to AWS Elastic Beanstalk") {
    depends(compile, createConfig, configureWarName, initElasticBeanstalkClient, initTargetApplicationAndEnvironmentConfig) 

    String warFileName

    //configureWarName target doesn't set the warName property since Grails 2.2 (http://bit.ly/17uHfFm)
    if (binding.variables.containsKey("warName")) {
        warFileName = warName
    } else {
        warFileName = warCreator.warName
    }

    //output global variables coming from Grails scripts
    //println "script metadata: ${metadata}"
    //println "Grails settings warname ${grailsSettings.projectWarFile}"
    println "WAR file name: ${warFileName}"

    println 'Starting AWS Elastic Beanstalk deployment'

    //TODO check number of deployed applications to watch for limit
    //TODO optionally purge old application versions

    File appWarFile = getAppWarFile(warFileName)
    if(!appWarFile.exists()) {
        println "Could not find WAR file to upload. Expected: $appWarFile.absolutePath"
        exit 1
    }

    println "Finding S3 bucket to upload WAR"
    //TODO log bucket creation
    String bucketName = elasticBeanstalk.createStorageLocation().getS3Bucket()

    def s3key = uniqueTempWarFileName(appWarFile)
    uploadToS3(awsCredentials, appWarFile, bucketName, s3key)

    //TODO handle case where application does not yet exist - check application? (don't want to autocreate - disable autocreate flag?)

    //create a new application version
    println "Create application version with uploaded application"
    println "applicationName: ${applicationName}"
    println "environmentName: ${environmentName}"
    String versionLabel = getVersionLabel(appWarFile)
    println "versionLabel: ${versionLabel}"
    def createApplicationRequest = new CreateApplicationVersionRequest(
        applicationName: applicationName,
        versionLabel: versionLabel,
        description: description,
        autoCreateApplication: true, 
        sourceBundle: new S3Location(bucketName, s3key)
    )
    
    def createApplicationVersionResult = elasticBeanstalk.createApplicationVersion(createApplicationRequest)
    println "Created application version $createApplicationVersionResult"

    //check if the target beanstalk environment exists (if application is missing it will get created)
    def environmentExists = elasticBeanstalk.describeEnvironments().getEnvironments().find({
        it.environmentName == environmentName
    })

    if (environmentExists) {
        //deploy the deployed version to an existing environment
        println "Updating environment ${environmentName} with uploaded application version"
        def updateEnviromentRequest = new UpdateEnvironmentRequest(environmentName:environmentName, versionLabel:versionLabel)
        def updateEnviromentResult = elasticBeanstalk.updateEnvironment(updateEnviromentRequest)
        println "Updated environment $updateEnviromentResult"
    } else {
        //TODO check for existence of template before creating environment, or catch/handle exception
        println "Target environment does not exist. Creating environment ${environmentName}, configuration template: ${templateName}"

        //TODO somehow check first that the version label template is valid
        def createEnvironmentRequest = new CreateEnvironmentRequest(
                applicationName: applicationName,
                environmentName: environmentName,
                versionLabel: versionLabel,
                templateName: templateName
        )
        //println createEnvironmentRequest
        def createEnvironmentResult = elasticBeanstalk.createEnvironment(createEnvironmentRequest)
        println "Created environment: ${createEnvironmentResult}"
    }
}

setDefaultTarget(main)

private String getDescription() {
    def versionDescriptionTemplate = config.grails?.plugin?.awsElasticBeanstalk?.versionDescriptionTemplate
    deployer.generateVersionDescription(versionDescriptionTemplate)
}

private String getVersionLabel(warFile) {
    def versionLabelTemplate = config.grails?.plugin?.awsElasticBeanstalk?.versionLabelTemplate
    deployer.generateVersionLabel(versionLabelTemplate, warFile, metadata.applicationVersion)
}

private File getAppWarFile(warFilename) {
    //TODO check to make sure that the WAR actually exist
    //println "war file name: ${warFilename}"
    new File(warFilename)
}

private uploadToS3(credentials, file, bucketName, key) {
    final console = grailsConsole // seems to be necessary so the ProgressListener can access the grailsConsole object
    console.addStatus "[${new Date()}] Uploading local WAR file ${file.name} to remote WAR file ${key} in bucket ${bucketName}..."
    String s3key = URLEncoder.encode(key, 'UTF-8')

    AmazonS3 s3 = new AmazonS3Client(credentials)
    if (System.getenv('CI')) {
        // Do no show progress in console when running CI (otherwise it generates huge log, e.g. in TravisCI it generates an error)
        s3.putObject(new PutObjectRequest(bucketName, s3key, file))
    } else {
        // Show progress in console
        def totalBytesTransferred = 0
        final fileSize = file.size()
        console.updateStatus "[${new Date()}] Uploaded 0/${fileSize} bytes..."
        //fully qualifying here to avoid conflict with deprecated classes
        s3.putObject(new PutObjectRequest(bucketName, s3key, file)
                .withGeneralProgressListener(new com.amazonaws.event.ProgressListener() {
            void progressChanged(com.amazonaws.event.ProgressEvent event) {
                totalBytesTransferred += event.bytesTransferred
                console.updateStatus "[${new Date()}] Uploaded ${totalBytesTransferred}/${fileSize} bytes..."
            }}))
    }
    console.addStatus "[${new Date()}] Uploaded WAR to S3."
}

/**
* Generates a unique file name for the uploaded WAR file in S3, based on the file's timestamp.
* //TODO what happens if the same file is uploaded twice?
*/
private String uniqueTempWarFileName(warFile) {
    def uuid = Long.toHexString(warFile.lastModified())
    "${uuid}-${warFile.name}"
}

