package online.ilyin.status.check.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import online.ilyin.status.check.model.NetworkStatusChangeAction
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Service
class ActionsRunner {
    suspend fun runAction(networkStatusChangeAction: NetworkStatusChangeAction) {
        when (networkStatusChangeAction) {
            NetworkStatusChangeAction.REBOOT -> {
                withContext(Dispatchers.IO) {
                    Files.write(
                        Paths.get("/proc/sys/kernel/sysrq"),
                        "1".toByteArray(),
                        StandardOpenOption.APPEND,
                        StandardOpenOption.WRITE
                    )
                    Files.write(
                        Paths.get("/proc/sysrq-trigger"),
                        "b".toByteArray(),
                        StandardOpenOption.APPEND,
                        StandardOpenOption.WRITE
                    )
                }
            }
        }
    }
}