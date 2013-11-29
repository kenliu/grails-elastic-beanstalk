grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        //NOTE: this version number needs to be kept in sync with the 
        //version in pluginlib/build.gradle
        build ('com.amazonaws:aws-java-sdk:1.6.7') { //this depends on HttpClient 4.1, which seems to clash with HttpClient 4.0 from Grails
            //export = false
        }
        compile ('com.amazonaws:aws-java-sdk:1.6.7') { //this depends on HttpClient 4.1, which seems to clash with HttpClient 4.0 from Grails
            //export = false
        }
        runtime 'org.yaml:snakeyaml:1.13'
    }

    plugins {
        build(":release:3.0.1", ':rest-client-builder:1.0.3') {
            export = false
        }
    }
}
