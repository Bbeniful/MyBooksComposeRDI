package com.example.core.cryptography

import android.content.Context
import androidx.biometric.BiometricPrompt
import javax.crypto.Cipher

interface CryptographyManager {

    /**
     * This method first gets or generates an instance of SecretKey and then initializes the Cipher
     * with the key. The secret key uses [ENCRYPT_MODE][Cipher.ENCRYPT_MODE] is used.
     */
    fun getInitializedCipherForEncryption(keyName: String): Cipher

    /**
     * This method first gets or generates an instance of SecretKey and then initializes the Cipher
     * with the key. The secret key uses [DECRYPT_MODE][Cipher.DECRYPT_MODE] is used.
     */
    fun getInitializedCipherForDecryption(keyName: String, initializationVector: ByteArray): Cipher

    /**
     * The Cipher created with [getInitializedCipherForEncryption] is used here
     */
    fun encryptData(plaintext: ByteArray, cipher: Cipher): EncryptedData

    /**
     * The Cipher created with [getInitializedCipherForDecryption] is used here
     */
    fun decryptData(ciphertext: ByteArray, cipher: Cipher): ByteArray

    /**
     * This method gets cipher to start the authentication flow
     */
    fun getCipherForAuthentication(context: Context, isEncryption: Boolean): Cipher?

    /**
     * This method encrypts or decrypts the text
     */
    fun processText(context: Context, text: ByteArray, isEncryption: Boolean, cryptoObject: BiometricPrompt.CryptoObject?): ByteArray

}