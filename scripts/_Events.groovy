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

import grails.util.Holders
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.DumperOptions

//refer to http://docs.aws.amazon.com/elasticbeanstalk/latest/dg/customize-containers-ec2.html
eventCreateWarStart = { warName, stagingDir ->

    //copy the src/ebextensions dir to build staging dir, if it exists
    def EBEXTENSIONS_SRC_DIR = "./src/ebextensions"
    if (new File(EBEXTENSIONS_SRC_DIR).exists()) {
        new AntBuilder().copy(todir: stagingDir.path + '/.ebextensions') {
            fileset(dir: "./src/ebextensions") {
                include(name: "*.config")
            }
        }
    }

    //TODO generate .ebextensions files from templates

    generateSettingsConfigYaml(stagingDir, Holders.config)
}

/**
* Generates an .ebextensions/eb-settings.config file that overrides/updates the
* Beanstalk application/container settings for environment variables and JVM options.
*
* refer to http://docs.aws.amazon.com/elasticbeanstalk/latest/dg/customize-containers-ec2.html#customize-containers-format-options
*/
def generateSettingsConfigYaml(File stagingDir, config) {
    def systemPropertiesMap = config.grails.plugin.awsElasticBeanstalk.environmentProperties
    def jvmPropertiesMap = config.grails.plugin.awsElasticBeanstalk.jvmOptions
    if(!systemPropertiesMap && !jvmPropertiesMap) {
        return
    }

    final File EXTENSIONS_DIR = new File(stagingDir, '.ebextensions')
    if(!EXTENSIONS_DIR.exists() && !EXTENSIONS_DIR.mkdir()) {
        throw new RuntimeException("Failed to create extensions directory at $EXTENSIONS_DIR.absolutePath")
    }

    final File ENV_FILE = new File(EXTENSIONS_DIR, 'eb-settings.config')

    def ebOptions = []
    systemPropertiesMap.each { key, value ->
        ebOptions << [namespace:'aws:elasticbeanstalk:application:environment',
        option_name:key, value:value]
    }
    jvmPropertiesMap.each { key, value ->
        ebOptions << [namespace:'aws:elasticbeanstalk:container:tomcat:jvmoptions',
        option_name:key, value:value]
    }

    DumperOptions yamlOptions = new DumperOptions()
    yamlOptions.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
    def yaml = new Yaml(yamlOptions)
    ENV_FILE.text = yaml.dump([option_settings:ebOptions])
}
