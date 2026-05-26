package io.github.javoxir.telegram.gateway.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TelegramStatusEnumDeserializationTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void deliveryStatusShouldDeserializeFromString() throws Exception {
        DeliveryStatus status = mapper.readValue("\"sent\"", DeliveryStatus.class);
        assertEquals(DeliveryStatus.SENT, status);
    }

    @Test
    void deliveryStatusShouldDeserializeFromObjectStatusField() throws Exception {
        DeliveryStatus status = mapper.readValue("{\"status\":\"delivered\"}", DeliveryStatus.class);
        assertEquals(DeliveryStatus.DELIVERED, status);
    }

    @Test
    void deliveryStatusShouldDeserializeUnknownToUnknown() throws Exception {
        DeliveryStatus status = mapper.readValue("\"not_real_status\"", DeliveryStatus.class);
        assertEquals(DeliveryStatus.UNKNOWN, status);
    }

    @Test
    void deliveryStatusShouldDeserializeNullToUnknown() {
        assertEquals(DeliveryStatus.UNKNOWN, DeliveryStatus.fromValue((com.fasterxml.jackson.databind.JsonNode) null));
        assertEquals(DeliveryStatus.UNKNOWN, DeliveryStatus.fromValue(NullNode.instance));
    }

    @Test
    void verificationStatusShouldDeserializeFromString() throws Exception {
        VerificationStatus status = mapper.readValue("\"verified\"", VerificationStatus.class);
        assertEquals(VerificationStatus.VERIFIED, status);
    }

    @Test
    void verificationStatusShouldDeserializeFromObjectStatusField() throws Exception {
        VerificationStatus status = mapper.readValue("{\"status\":\"pending\"}", VerificationStatus.class);
        assertEquals(VerificationStatus.PENDING, status);
    }

    @Test
    void verificationStatusShouldDeserializeUnknownToUnknown() throws Exception {
        VerificationStatus status = mapper.readValue("\"not_real_status\"", VerificationStatus.class);
        assertEquals(VerificationStatus.UNKNOWN, status);
    }

    @Test
    void verificationStatusShouldDeserializeNullToUnknown() {
        assertEquals(VerificationStatus.UNKNOWN, VerificationStatus.fromValue((com.fasterxml.jackson.databind.JsonNode) null));
        assertEquals(VerificationStatus.UNKNOWN, VerificationStatus.fromValue(NullNode.instance));
    }
}
