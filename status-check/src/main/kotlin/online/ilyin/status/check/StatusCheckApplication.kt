package online.ilyin.status.check

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
class StatusCheckApplication

fun main(args: Array<String>) {
    runApplication<StatusCheckApplication>(*args)
}
