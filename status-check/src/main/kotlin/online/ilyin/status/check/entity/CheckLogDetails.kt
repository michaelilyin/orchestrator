package online.ilyin.status.check.entity

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.annotation.Id
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.relational.core.mapping.Table
import java.net.InetAddress
import java.time.Duration

@Table("check_log_details")
data class CheckLogDetails(
  val logId: Long,
  val data: CheckLogData
) {
  @Id
  var id: Long = 0
}

@JsonTypeInfo(
  use = JsonTypeInfo.Id.MINIMAL_CLASS,
  include = JsonTypeInfo.As.PROPERTY,
)
@JsonSubTypes(
  JsonSubTypes.Type(NetworkPingData::class)
)
interface CheckLogData


data class NetworkPingData(
  val host: InetAddress,
  val duration: Duration
) : CheckLogData

@ReadingConverter
class CheckLogDataReadConverter(
  private val objectMapper: ObjectMapper
) : Converter<String, CheckLogData> {
  override fun convert(source: String): CheckLogData = objectMapper.readValue(source)
}

@WritingConverter
class CheckLogDataWriteConverter(
  private val objectMapper: ObjectMapper
) : Converter<CheckLogData, ByteArray> {
  override fun convert(source: CheckLogData): ByteArray = objectMapper.writeValueAsBytes(source)
}

@Configuration
class CheckLogDataConfig(
  private val connectionFactory: ConnectionFactory,
  private val objectMapper: ObjectMapper
) : AbstractR2dbcConfiguration() {
  override fun connectionFactory(): ConnectionFactory {
    return connectionFactory
  }

  override fun getCustomConverters(): MutableList<Any> {
    return mutableListOf(CheckLogDataWriteConverter(objectMapper), CheckLogDataReadConverter(objectMapper))
  }
}
