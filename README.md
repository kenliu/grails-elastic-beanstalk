# Grails Plugin - AWS Elastic Beanstalk Integration

This plugin provides Grails integration with the AWS Elastic Beanstalk service.

Features:
- Build WAR file and deploy to Elastic Beanstalk using Grails command

## Installation and Configuration

### Setting up the Elastic Beanstalk environment for the first time

Before you can deploy a Grails application, you'll need to create an Elastic Beanstalk "Application" and "Environment" to be your target deployment container (Tomcat).

1. Sign up for an AWS account (credit card needed).
1. In the AWS Elastic Beanstalk console (web interface), click "Create New Application".
1. For "Application Name" enter the name of your Grails application (the app.name specified in application.properties)
1. On the "Environment Type" screen, for "Predefined Configuration:", select "Tomcat".
1. For "Environment type:", you can select either "Single instance" or "Load balancing, autoscaling", and click "Continue"
1. On the "Application Version" screen, for "Source", select "Sample application", and click "Continue".
1. On the "Environment Information" screen, for "Environment Name", enter "Default-Environment".
1. For "Environment URL", enter a subdomain name for your application. This domain must be globally unique across AWS and will be accessible to the public. Click "Check availability" to check that the subdomain is not already used. Click "Continue".
1. On the "Additional Resources" screen, click "Continue".
1. On the "Configuration Details" screen, select the "Instance type". "t1.micro" should be sufficient for a small Grails application but you may need "m1.small" if your application has a large memory footprint. Note that "t1.micro" falls under the "Free Tier" (free usage for the first year) but other instance sizes are not free.
1. Click "Continue", then click "Finish". It will take a few minutes for AWS to allocate services and spin up your new instance.

### AWS Credentials File Setup

1. Find/create an Access Key for your AWS account (Access Key/Secret Key pair) from the "Your Security Credentials" page: https://console.aws.amazon.com/iam/home?#security_credential
1. Create a credentials file containing your access key and secret key

Warning: Be sure to properly secure this key information! Anyone who has this key can use your AWS account and potentially cost you a lot of money.

The plugin uses the same credentials property file as the official AWS CLI tools.

see: http://docs.aws.amazon.com/elasticbeanstalk/latest/dg/usingCLI.html

- Set an environment variable AWS_CREDENTIAL_FILE with the path to the credentials file

### Plugin Installation

In BuildConfig.groovy add the following plugin dependency to the "plugins" section:

    build ':aws-elastic-beanstalk:0.1.1'

### Plugin Configuration

If you previously created your own Elastic Beanstalk application/environment (prior to these steps), you can configure them in Config.groovy (see "Additional configuration" below). Otherwise no other configuraiton is required.

## Deploying your WAR to Elastic Beanstalk

The plugin provides a Grails command for deploying your WAR to Elastic Beanstalk.

run Grails command:

    grails aws-eb-deploy

## Additional configuration

Config.groovy:

<!-- TODO describe default values - default to Grails application name?, restrictions -->

    grails.plugin.awsElasticBeanstalk.applicationName = 'myApplication'
    grails.plugin.awsElasticBeanstalk.environmentName = 'myEnvironment'



## Acknowledgements
Maikel Alderhout's Blog http://malderhout.wordpress.com/2011/10/10/automate-deployment-from-cloudbees-jenkins-to-amazon-beanstalk/

## Version History
* 0.1.1 - fix incompatibility issue with Grails 2.2
* 0.1 - Initial release - deploy WAR to existing EB environment