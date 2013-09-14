import grails.util.Holders
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.DumperOptions




eventCreateWarStart = { warName, stagingDir ->

    //create the .ebextensions folder and generate an environments.config file

    def config = Holders.config
    def propertiesMap = config.grails.plugin.awsElasticBeanstalk.systemProperties
    if(!propertiesMap) {
        return
    }

    //TODO determine how setting the JVM options here will affect the container settings
    //does it automatically override/reset the container options?
    //see http://docs.aws.amazon.com/elasticbeanstalk/latest/dg/customize-containers-ec2.html

    final File EXTENSIONS_DIR = new File(stagingDir, '.ebextensions')
    if(!EXTENSIONS_DIR.mkdir()) {
        throw new RuntimeException("Failed to create extensions directory at $EXTENSIONS_DIR.absolutePath")
    }

    final File ENV_FILE = new File(EXTENSIONS_DIR, 'environments.config')

    def ebOptions = []
    propertiesMap.each { key, value ->
        ebOptions << [option_name:key, value:value]
    }

    DumperOptions yamlOptions = new DumperOptions()
    yamlOptions.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
    def yaml = new Yaml(yamlOptions)
    ENV_FILE.text = yaml.dump([option_settings:ebOptions])
}

