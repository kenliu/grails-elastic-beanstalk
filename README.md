# Grails Plugin - AWS Elastic Beanstalk Integration

This plugin provides Grails integration with the AWS Elastic Beanstalk service.

Features Grails scripts for managing AWS Elastic Beanstalk deployments:
- aws-eb-deploy: Deploy a WAR file to Elastic Beanstalk
- aws-eb-stop: Terminate (shut down) a running Elastic Beanstalk environment

## Installation and Configuration

### Setting up an Elastic Beanstalk environment for the first time

Before you can deploy a Grails application, you'll need to create an Elastic Beanstalk "Application" and start up an "Environment" to be your target deployment container.

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
All API calls to AWS require an API key consisting of an "Access Key" and a "Secret Key" pair.

1. Find/create an Access Key for your AWS account (Access Key/Secret Key pair) from the "Your Security Credentials" page: https://console.aws.amazon.com/iam/home?#security_credential
1. Create a credentials file containing your access key and secret key

WARNING: Be sure to properly secure this key information! Anyone who has this key can use your AWS account and potentially cost you a lot of money.

The plugin uses the same credentials property file as the official AWS CLI tools.

see: http://docs.aws.amazon.com/elasticbeanstalk/latest/dg/usingCLI.html

- Set an environment variable AWS_CREDENTIAL_FILE with the path to the credentials file

### Plugin Installation

In BuildConfig.groovy add the following plugin dependency to the "plugins" section:

    build ':aws-elastic-beanstalk:0.1.1'

### Plugin Configuration

If you previously created your own Elastic Beanstalk application/environment (prior to these steps), you can configure them in Config.groovy (see "Additional configuration" below). Otherwise no other configuration is required (defaults will be used).

## Deploying your WAR to Elastic Beanstalk

The plugin provides a Grails command for deploying your WAR to Elastic Beanstalk.

run Grails command:

    grails aws-eb-deploy

<!--## Configuring a Grails app to run on Elastic Beanstalk

Grails 
-->

## Shutting down the application

Run the grails command:

    grails aws-eb-stop

to shut down the running environment. Note this will not only shut down the application container, it will also shut down and the running EC2 instance(s) for the target environment and delete all other associated AWS resources for the environment (e.g. Elastic IP, ELB).

The +aws-eb-stop+ command is an alias for +aws-eb-terminate-environment+

## Additional configuration

Config.groovy:

    grails.plugin.awsElasticBeanstalk.applicationName = 'myApplication'
    grails.plugin.awsElasticBeanstalk.environmentName = 'myEnvironment'
    grails.plugin.awsElasticBeanstalk.systemProperties = ['property.name.1':'property-value-1', 'property.name.2':'property-value-2']

## Roadmap



## Contributing

Pull requests are appreciated!

## Acknowledgements
Maikel Alderhout's Blog http://malderhout.wordpress.com/2011/10/10/automate-deployment-from-cloudbees-jenkins-to-amazon-beanstalk/

## Version History
* 0.2 - add support for alternate regions, add aws-eb-terminate-environment and aws-eb-stop commands
* 0.1.1 - fix incompatibility issue with Grails 2.2
* 0.1 - Initial release - deploy WAR to existing EB environment
