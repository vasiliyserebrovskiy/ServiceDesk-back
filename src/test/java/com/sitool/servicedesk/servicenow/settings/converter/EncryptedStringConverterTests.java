package com.sitool.servicedesk.servicenow.settings.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("EncryptedStringConverter unit tests.")
class EncryptedStringConverterTests {

    private EncryptedStringConverter converter;

    @BeforeEach
    void setUp() {
        converter = new EncryptedStringConverter();
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);
        String testKey = Base64.getEncoder().encodeToString(keyBytes);
        ReflectionTestUtils.setField(converter, "encryptionKey", testKey);
    }

    @Test
    @DisplayName("Should return original value after encrypt then decrypt")
    void encryptThenDecrypt_shouldReturnOriginalValue() {
        String original = "SomeStrongPassword1!";

        String encrypted = converter.convertToDatabaseColumn(original);
        String decrypted = converter.convertToEntityAttribute(encrypted);

        assertThat(encrypted).isNotEqualTo(original);
        assertThat(decrypted).isEqualTo(original);
    }

    @Test
    @DisplayName("Should produce different ciphertext for the same value on repeated calls")
    void encrypt_shouldProduceDifferentCiphertext_onEachCall() {
        String original = "SomeStrongPassword1!";

        String firstEncryption = converter.convertToDatabaseColumn(original);
        String secondEncryption = converter.convertToDatabaseColumn(original);

        assertThat(firstEncryption).isNotEqualTo(secondEncryption);
    }

    @Test
    @DisplayName("Should return null when encrypting null")
    void convertToDatabaseColumn_shouldReturnNull_whenAttributeIsNull() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    @DisplayName("Should return null when decrypting null")
    void convertToEntityAttribute_shouldReturnNull_whenDbDataIsNull() {
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }

    @Test
    @DisplayName("Should throw when decrypting tampered or invalid data")
    void convertToEntityAttribute_shouldThrow_whenDataIsTamperedOrInvalid() {
        String garbage = Base64.getEncoder().encodeToString("not a real ciphertext at all".getBytes());

        assertThatThrownBy(() -> converter.convertToEntityAttribute(garbage))
                .isInstanceOf(IllegalStateException.class);
    }
}