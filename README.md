# Grails Plugin - AWS Elastic Beanstalk Integration

[![Build Status](https://travis-ci.org/kenliu/grails-elastic-beanstalk.png)](https://travis-ci.org/kenliu/grails-elastic-beanstalk)

This plugin provides Grails integration with the AWS Elastic Beanstalk service.

Features Grails scripts for managing AWS Elastic Beanstalk deployments:
- aws-eb-deploy: Deploy a WAR file to Elastic Beanstalk
- aws-eb-stop: Terminate (shut down) a running Elastic Beanstalk environment

## Quick Start

See the online Quick Start guide: http://kenliu.github.io/grails-elastic-beanstalk/quick-start.html

## User Manual

A detailed user manual is available online:

http://kenliu.github.io/grails-elastic-beanstalk/manual.html

## Additional configuration

### Config.groovy:

    grails.plugin.awsElasticBeanstalk.applicationName = 'myApplication'
    grails.plugin.awsElasticBeanstalk.environmentName = 'myEnvironment'
    grails.plugin.awsElasticBeanstalk.savedConfigurationName = 'default' //TODO is this the same terminology as in the web interface
    grails.plugin.awsElasticBeanstalk.systemProperties = ['property.name.1':'property-value-1', 'property.name.2':'property-value-2']

If the environment 'myEnvironment' does not exist it will be created automatically, the savedConfigurationName property allows to use a saved configuration for the newly created environment.
As an alternative to the AWS Credential File described in a previous section, you can configure the authentication directly in your project configuration file. The credentials can be set directly (not recommended) or passed through system properties (recommended).

### Config.groovy:

    grails.plugin.awsElasticBeanstalk.accessKey = System.getProperty('BEANSTALK_ACCESS_KEY')
    grails.plugin.awsElasticBeanstalk.secretKey = System.getProperty('BEANSTALK_SECRET_KEY')

### src/ebextensions/\*.config

Any config files added to the `src/ebextensions` are added to the `.ebextensions` directory at the top-level of the generated WAR file.
These are either YAML or JSON formatted files, as detailed at http://docs.aws.amazon.com/elasticbeanstalk/latest/dg/customize-containers.html.

## Roadmap

TBD

## Contributing and bug reports

Pull requests are appreciated! Please report any bugs or suggestions in the Github issue tracker.

## Contributors

Thanks to:
* https://github.com/jvdrean
* https://github.com/alxndrsn
* https://github.com/burtbeckwith

## Acknowledgements
Maikel Alderhout's Blog http://malderhout.wordpress.com/2011/10/10/automate-deployment-from-cloudbees-jenkins-to-amazon-beanstalk/

## Version History
* 0.3 - new user manual, default saved configuration, ebextensions folder, jvm and environment properties, custom version label and description
* 0.2 - add support for alternate regions, add aws-eb-terminate-environment and aws-eb-stop commands
* 0.1.1 - fix incompatibility issue with Grails 2.2
* 0.1 - Initial release - deploy WAR to existing EB environment
