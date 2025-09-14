package com.bieniucieniu.hometooling.feat.ha.ws.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class HaMessageType {
    @SerialName("auth_required")
    AuthRequired,

    @SerialName("auth")
    Auth,

    @SerialName("auth_ok")
    AuthOk,

    @SerialName("auth_invalid")
    AuthInvalid,

    @SerialName("supported_features")
    SupportedFeatures,

    @SerialName("subscribe_events")
    SubscribeEvents,

    @SerialName("subscribe_trigger")
    SubscribeTrigger,

    @SerialName("unsubscribe_events")
    UnsubscribeEvents,

    @SerialName("fire_event")
    FireEvent,

    @SerialName("call_service")
    CallService,

    @SerialName("get_states")
    GetStates,

    @SerialName("get_config")
    GetConfig,

    @SerialName("get_services")
    GetServices,

    @SerialName("get_panels")
    GetPanels,

    @SerialName("validate_config")
    ValidateConfig,

    @SerialName("ping")
    Ping,

    @SerialName("pong")
    Pong,

    @SerialName("result")
    Result,

    @SerialName("event")
    Event;
}