package com.vymalo.keycloak.webhook.models

import com.rabbitmq.client.Address
import com.vymalo.keycloak.webhook.helper.*

data class AmqpConfig(
    val username: String,
    val password: String,
    val vHost: String,
    val ssl: Boolean,
    val exchange: String,
    val addresses: Array<Address>,
    val heartbeatSeconds: Int,
    val bufferCapacity: Int,
    val inFlightCapacity: Int,
    val confirmTimeoutMs: Long,
    val backoffInitialMs: Long,
    val backoffMaxMs: Long,
    val dlqEnabled: Boolean,
    val mainQueueName: String,
    val dlqExchangeName: String,
    val dlqQueueName: String
) {
    companion object {
        private const val DEFAULT_PORT = 5672
        private const val DEFAULT_VHOST = "/"
        private const val DEFAULT_HEARTBEAT = 30
        private const val DEFAULT_NETWORK_RECOVERY_MS = 5_000L
        private const val DEFAULT_BUFFER_CAPACITY = 1_000
        private const val DEFAULT_INFLIGHT_CAPACITY = 1_000
        private const val DEFAULT_CONFIRM_TIMEOUT_MS = 15_000L
        private const val DEFAULT_BACKOFF_INITIAL_MS = 250L
        private const val DEFAULT_BACKOFF_MAX_MS = 2000L
        private const val DEFAULT_DLQ_ENABLED = false
        private const val DEFAULT_MAIN_QUEUE_NAME = "keycloak.events"
        private const val DEFAULT_DLQ_EXCHANGE_NAME = "keycloak.dlx"
        private const val DEFAULT_DLQ_QUEUE_NAME = "keycloak.dlq"

        fun fromEnv(): AmqpConfig {
            val username = amqpUsernameKey.cff()
            val password = amqpPasswordKey.cff()
            val vHost = amqpVHostKey.cfe { DEFAULT_VHOST }
            val ssl = amqpSslKey.bf()
            val exchange = amqpExchangeKey.cff()

            val heartbeatSeconds = amqpHeartbeatSecondsKey
                .cfe { DEFAULT_HEARTBEAT.toString() }
                .toIntOrNull() ?: DEFAULT_HEARTBEAT

            val bufferCapacity = amqpWhHandlerBufferCapacityKey
                .cfe { DEFAULT_BUFFER_CAPACITY.toString() }
                .toIntOrNull() ?: DEFAULT_BUFFER_CAPACITY

            val inFlightCapacity = amqpWhHandlerInFlightCapacityKey
                .cfe { DEFAULT_INFLIGHT_CAPACITY.toString() }
                .toIntOrNull() ?: DEFAULT_INFLIGHT_CAPACITY

            val confirmTimeoutMs = amqpWhHandlerConfirmTimeoutMsKey
                .cfe { DEFAULT_CONFIRM_TIMEOUT_MS.toString() }
                .toLongOrNull() ?: DEFAULT_CONFIRM_TIMEOUT_MS

            val backoffInitialMs = amqpBackoffInitialMsKey
                .cfe { DEFAULT_BACKOFF_INITIAL_MS.toString() }
                .toLongOrNull() ?: DEFAULT_BACKOFF_INITIAL_MS

            val backoffMaxMs = amqpBackoffMaxMsKey
                .cfe { DEFAULT_BACKOFF_MAX_MS.toString() }
                .toLongOrNull() ?: DEFAULT_BACKOFF_MAX_MS

            val addresses: Array<Address> = when (val addrs = amqpAdressesKey.cf()) {
                null, "", " " -> {
                    val host = amqpHostKey.cff()
                    val port = amqpPortKey.cfe { DEFAULT_PORT.toString() }.toIntOrNull() ?: DEFAULT_PORT
                    arrayOf(Address(host, port))
                }
                else -> Address.parseAddresses(addrs) // already Address[]
            }

            return AmqpConfig(
                username = username,
                password = password,
                vHost = vHost,
                ssl = ssl,
                exchange = exchange,
                addresses = addresses,
                heartbeatSeconds = heartbeatSeconds,
                bufferCapacity = bufferCapacity,
                inFlightCapacity = inFlightCapacity,
                confirmTimeoutMs = confirmTimeoutMs,
                backoffInitialMs = backoffInitialMs,
                backoffMaxMs = backoffMaxMs,
                dlqEnabled = amqpDlqEnabledKey.cfe { DEFAULT_DLQ_ENABLED.toString() }.toBoolean(),
                mainQueueName = amqpMainQueueKey.cfe { DEFAULT_MAIN_QUEUE_NAME },
                dlqExchangeName = amqpDlqExchangeKey.cfe { DEFAULT_DLQ_EXCHANGE_NAME },
                dlqQueueName = amqpDlqQueueKey.cfe { DEFAULT_DLQ_QUEUE_NAME }
            )
        }
    }
}
