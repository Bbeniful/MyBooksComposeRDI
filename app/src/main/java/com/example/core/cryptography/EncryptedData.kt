package com.example.core.cryptography

@Suppress("ArrayInDataClass")
data class EncryptedData(val ciphertext: ByteArray, val initializationVector: ByteArray)
