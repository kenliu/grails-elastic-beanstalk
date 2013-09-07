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
        build ('com.amazonaws:aws-java-sdk:1.5.5') { //this depends on HttpClient 4.1, which seems to clash with HttpClient 4.0 from Grails
            //export = false
        }
        compile ('com.amazonaws:aws-java-sdk:1.5.5') { //this depends on HttpClient 4.1, which seems to clash with HttpClient 4.0 from Grails
            //export = false
        }
    }

    plugins {
        build(":release:3.0.0", ':rest-client-builder:1.0.3') {
            export = false
        }
    }
}
