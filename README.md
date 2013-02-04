# Grails Plugin - AWS Elastic Beanstalk Integration

This plugin provides Grails integration with the Amazon Web Services Elastic Beanstalk service.


## Installation and Configuration

### Setting up the Elastic Beanstalk environment
- Sign up for an AWS account
- In the AWS Elastic Beanstalk console (web interface), click "Create New Application".
- For "Application Name" enter the name of your Grails application (the app.name specified in application.properties)
- For "Container Type", select any of the Tomcat containers (64-bit Tomcat 7 recommended)
- For "Application Source", select "Use the Sample Application", and click "Continue"
- For "Environment Name", enter "Default-Environment"
- For "Environment URL", enter a subdomain name for your application. This domain must be globally unique across AWS and will be accessible to the public
- Click "Continue"
- Select the "Instance Type". "t1.micro" should be sufficient for a small Grails application but you may need "m1.small" if your application has a large memory footprint. Note that "t1.micro" falls under the "Free Tier" (free usage for the first year) but other instance sizes are not free
- Click "Continue", then click "Finish". It will take a few minutes for AWS to allocate services and spin up your new instance.

### AWS credentials file setup

- Find/create an Access Key for your AWS account (Access Key/Secret Key pair) from here: https://aws-portal.amazon.com/gp/aws/securityCredentials
- Create a credentials file containing your access key and secret key

The plugin uses the same credentials property file as the official AWS CLI tools.

http://docs.aws.amazon.com/elasticbeanstalk/latest/dg/usingCLI.html

- Set an environment variable AWS_CREDENTIAL_FILE with the path to the credentials file

### Plugin Installation

In BuildConfig.groovy add the following plugin dependency to the "plugins" section

build ":aws-elastic-beanstalk:0.1"

### Plugin Configuration

If you previously created your own Elastic Beanstalk application/environment (prior to these steps), you can configure them in Config.groovy (see "Additional configuration" below). Otherwise no other configuraiton is required.

## Deploying your WAR to Elastic Beanstalk

The plugin provides a Grails target for deploying your WAR to Elastic Beanstalk.

run Grails script:
grails aws-eb-deploy

## Additional configuration

Config.groovy:
grails.plugin.awsElasticBeanstalk.applicationName = 'testApplication' //TODO describe default, restrictions
grails.plugin.awsElasticBeanstalk.environmentName = 'testApplication' //TODO describe default

## Acknowledgements

## Version History
* 0.1 - Initial release - deploy WAR to existing EB environment