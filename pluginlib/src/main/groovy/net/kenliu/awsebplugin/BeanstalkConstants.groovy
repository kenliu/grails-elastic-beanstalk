package net.kenliu.awsebplugin

class BeanstalkConstants {

	/**
	*/
	enum SolutionStacks {
		TOMCAT7_32('32bit Amazon Linux running Tomcat 7'),
		TOMCAT7_64('64bit Amazon Linux running Tomcat 7'),
		TOMCAT6_32('32bit Amazon Linux running Tomcat 6'),
		TOMCAT6_64('64bit Amazon Linux running Tomcat 6')

		SolutionStacks(String value) { this.value = value }
    	final String value
	}

	//see http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/regions/Regions.html
//	enum Endpoints {
        /*
US East (Northern Virginia) Region
elasticbeanstalk.us-east-1.amazonaws.com
US West (Northern California) Region	elasticbeanstalk.us-west-1.amazonaws.com
US West (Oregon) Region	elasticbeanstalk.us-west-2.amazonaws.com
EU (Ireland) Region	elasticbeanstalk.eu-west-1.amazonaws.com
Asia Pacific (Singapore) Region	elasticbeanstalk.ap-southeast-1.amazonaws.com
Asia Pacific (Sydney) Region	elasticbeanstalk.ap-southeast-2.amazonaws.com
Asia Pacific (Tokyo) Region	elasticbeanstalk.ap-northeast-1.amazonaws.com
South America (Sao Paulo) Region	elasticbeanstalk.sa-east-1.amazonaws.com
          */
/*		Endpoints(String value) { this.value = value }
    	final String value
	}
*/
}