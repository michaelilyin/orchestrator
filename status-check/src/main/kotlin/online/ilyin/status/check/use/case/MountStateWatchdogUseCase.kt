package online.ilyin.status.check.use.case

import kotlinx.coroutines.runBlocking
import online.ilyin.status.check.model.NetworkType
import online.ilyin.status.check.support.logging.error
import online.ilyin.status.check.support.logging.info
import online.ilyin.status.check.support.logging.logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.ApplicationListener
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

private val log = logger { }

//@ConfigurationProperties()
//data class MountsCheckProperties(
//
//)

@Component
class MountStateWatchdogUseCase(
  private val executor: TaskScheduler,
) : ApplicationListener<ApplicationReadyEvent> {
  override fun onApplicationEvent(event: ApplicationReadyEvent) {
    checkMounts()
  }

  private fun checkMounts() {
    log.info { "Start mounts state check" }
    try {
      val process = ProcessBuilder("nsenter", "-t", "1", "-m", "mount")
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

      process.waitFor()

      val mounts = process.inputStream.bufferedReader().lines()
        .filter {
          it.contains("/video") || it.contains("/music")
        }.toList()

      log.info("Mounts $mounts, size=${mounts.size}")
      if (mounts.size < 2) {
        log.info("Try to mount")
        val mountProcess = ProcessBuilder("nsenter", "-t", "1", "-m", "mount", "-a")
          .redirectOutput(ProcessBuilder.Redirect.INHERIT)
          .redirectError(ProcessBuilder.Redirect.INHERIT)
          .start()

        mountProcess.waitFor()
      }

      runBlocking {
      }

      val nextCheckAt = Instant.now().plus(Duration.ofMinutes(5))
      log.info { "Schedule next checking at $nextCheckAt" }
      executor.schedule({ checkMounts() }, nextCheckAt)

    } catch (e: Exception) {
      log.error(e) { "Failed to complete mounts state check" }
    }
  }
}
