package com.bieniucieniu.hometooling.feat.ha.ws.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
sealed interface HaMessage {
    val type: HaMessageType
    val id: Int?
}

@Serializable
@SerialName("auth_required")
data class AuthRequired(
    @SerialName("ha_version") val haVersion: String,
    override val type: HaMessageType = HaMessageType.AuthRequired,
    override val id: Int? = null,
) : HaMessage

@Serializable
@SerialName("auth")
data class Auth(
    @SerialName("access_token") val accessToken: String,
    override val type: HaMessageType = HaMessageType.Auth,
    override val id: Int? = null,
) : HaMessage

@Serializable
@SerialName("auth_ok")
data class AuthOk(
    @SerialName("ha_version") val haVersion: String,
    override val type: HaMessageType = HaMessageType.AuthOk,
    override val id: Int? = null,
) : HaMessage

@Serializable
@SerialName("auth_invalid")
data class AuthInvalid(
    val message: String,
    override val type: HaMessageType = HaMessageType.AuthInvalid,
    override val id: Int? = null,
) : HaMessage

@Serializable
@SerialName("supported_features")
data class SupportedFeatures(
    override val id: Int,
    val features: Map<String, JsonElement>,
    override val type: HaMessageType = HaMessageType.SupportedFeatures,
) : HaMessage

@Serializable
@SerialName("subscribe_events")
data class SubscribeEvents(
    override val id: Int,
    @SerialName("event_type") val eventType: String? = null,
    override val type: HaMessageType = HaMessageType.SubscribeEvents,
) : HaMessage

@Serializable
@SerialName("subscribe_trigger")
data class SubscribeTrigger(
    override val id: Int,
    val trigger: JsonElement,
    override val type: HaMessageType = HaMessageType.SubscribeTrigger,
) : HaMessage

@Serializable
@SerialName("unsubscribe_events")
data class UnsubscribeEvents(
    override val id: Int,
    val subscription: Int,
    override val type: HaMessageType = HaMessageType.UnsubscribeEvents,
) : HaMessage

@Serializable
@SerialName("fire_event")
data class FireEvent(
    override val id: Int,
    @SerialName("event_type") val eventType: String,
    @SerialName("event_data") val eventData: Map<String, JsonElement>? = null,
    override val type: HaMessageType = HaMessageType.FireEvent,
) : HaMessage

@Serializable
@SerialName("call_service")
data class CallService(
    override val id: Int,
    val domain: String,
    val service: String,
    @SerialName("service_data") val serviceData: Map<String, JsonElement>? = null,
    val target: Map<String, JsonElement>? = null,
    override val type: HaMessageType = HaMessageType.CallService,
) : HaMessage

@Serializable
@SerialName("get_states")
data class GetStates(
    override val id: Int,
    override val type: HaMessageType = HaMessageType.GetStates,
) : HaMessage

@Serializable
@SerialName("get_config")
data class GetConfig(
    override val id: Int,
    override val type: HaMessageType = HaMessageType.GetConfig,
) : HaMessage

@Serializable
@SerialName("get_services")
data class GetServices(
    override val id: Int,
    override val type: HaMessageType = HaMessageType.GetServices,
) : HaMessage

@Serializable
@SerialName("get_panels")
data class GetPanels(
    override val id: Int,
    override val type: HaMessageType = HaMessageType.GetPanels,
) : HaMessage

@Serializable
@SerialName("ping")
data class Ping(
    override val id: Int,
    override val type: HaMessageType = HaMessageType.Ping,
) : HaMessage

@Serializable
@SerialName("pong")
data class Pong(
    override val id: Int,
    override val type: HaMessageType = HaMessageType.Pong,
) : HaMessage

@Serializable
@SerialName("validate_config")
data class ValidateConfig(
    override val id: Int,
    val trigger: JsonElement? = null,
    val condition: JsonElement? = null,
    val action: JsonElement? = null,
    override val type: HaMessageType = HaMessageType.ValidateConfig,
) : HaMessage

@Serializable
@SerialName("result")
data class Result(
    override val id: Int,
    val success: Boolean,
    val result: JsonElement? = null,
    val error: Error? = null,
    override val type: HaMessageType = HaMessageType.Result,
) : HaMessage {
    @Serializable
    data class Error(
        val code: String,
        val message: String
    )
}

@Serializable
@SerialName("event")
data class Event(
    override val id: Int,
    val event: EventData,
    override val type: HaMessageType = HaMessageType.Event,
) : HaMessage {
    @Serializable
    data class EventData(
        @SerialName("event_type") val eventType: String,
        val data: JsonElement,
        val origin: String,
        @SerialName("time_fired") val timeFired: String,
        val context: JsonElement,
    )
}