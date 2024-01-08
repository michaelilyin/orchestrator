package online.ilyin.controller.metal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
class MetalControllerApplication

fun main(args: Array<String>) {
    runApplication<MetalControllerApplication>(*args)
}
