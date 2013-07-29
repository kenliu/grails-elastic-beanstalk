grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        build ('com.amazonaws:aws-java-sdk:1.5.1') { //this depends on HttpClient 4.1, which seems to clash with HttpClient 4.0 from Grails
            //export = false
        }
        compile ('com.amazonaws:aws-java-sdk:1.5.1') { //this depends on HttpClient 4.1, which seems to clash with HttpClient 4.0 from Grails
            //export = false
        }
    }

    plugins {
        build(":release:3.0.0", ':rest-client-builder:1.0.3') {
            export = false
        }
    }
}
