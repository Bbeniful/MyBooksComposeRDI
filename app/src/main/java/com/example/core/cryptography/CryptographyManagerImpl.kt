package com.example.core.cryptography

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.core.presentation.mybookscompose.MainActivity
import com.example.core.utils.Constants
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.KeyStore
import java.util.*
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec

class CryptographyManagerImpl : CryptographyManager {

    companion object {
        private const val KEY_SIZE: Int = 256
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    }

    override fun getInitializedCipherForEncryption(keyName: String): Cipher {
        val cipher = getCipher()
        val secretKey = getSecretKey(keyName)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher
    }

    override fun getInitializedCipherForDecryption(keyName: String, initializationVector: ByteArray): Cipher {
        val cipher = getCipher()
        val secretKey = getSecretKey(keyName)
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, initializationVector))
        } catch (iape: InvalidAlgorithmParameterException) {
            iape.printStackTrace()
        } catch (ike: InvalidKeyException) {
            ike.printStackTrace()
        }
        return cipher
    }

    override fun encryptData(plaintext: ByteArray, cipher: Cipher): EncryptedData {
        var ciphertext = byteArrayOf()
        try {
            ciphertext = cipher.doFinal(plaintext)
        } catch (bpe: BadPaddingException) {
            bpe.printStackTrace()
        } catch (ibse: IllegalBlockSizeException) {
            ibse.printStackTrace()
        }
        return EncryptedData(ciphertext, cipher.iv)
    }

    override fun decryptData(ciphertext: ByteArray, cipher: Cipher): ByteArray {
        var plaintext = byteArrayOf()
        try {
            plaintext = cipher.doFinal(ciphertext)
        } catch (bpe: BadPaddingException) {
            bpe.printStackTrace()
        } catch (ibse: IllegalBlockSizeException) {
            ibse.printStackTrace()
        }
        return plaintext
    }

    override fun getCipherForAuthentication(context: Context, isEncryption: Boolean): Cipher? {
        if (BiometricManager.from(context)
                .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager
                .BIOMETRIC_SUCCESS
        ) {
            return if (isEncryption) {
                getInitializedCipherForEncryption(Constants.MY_SECRET_AUTH_KEY_ALIAS)
            } else {
                val iv = Base64.getDecoder().decode(getEncryptedSharedPrefs(context).getString(Constants.MY_SECRET_IV_ALIAS, ""))
                if (iv.isEmpty()) throw java.lang.RuntimeException(Constants.MY_SECRET_IV_ERROR)
                getInitializedCipherForDecryption(Constants.MY_SECRET_AUTH_KEY_ALIAS, iv)
            }
        }
        return null
    }

    override fun processText(context: Context, text: ByteArray, isEncryption: Boolean, cryptoObject: BiometricPrompt.CryptoObject?): ByteArray {
        if (cryptoObject == null) {
            MainActivity.destroyState()
            return byteArrayOf()
        }
        return if (isEncryption) {
            val encryptedData = encryptData(text, cryptoObject.cipher!!)
            val ciphertext = encryptedData.ciphertext
            saveEncryptedIvAndKey(context, encryptedData)
            Base64.getEncoder().encodeToString(ciphertext).toByteArray()
        } else  {
            val cipherText = Base64.getDecoder().decode(getEncryptedSharedPrefs(context).getString(Constants.MY_SECRET_AUTH_KEY_ALIAS, ""))
            if (cipherText.isNotEmpty()) {
                decryptData(cipherText, cryptoObject.cipher!!)
            } else {
                byteArrayOf()
            }
        }
    }

    private fun saveEncryptedIvAndKey(context: Context, encryptedData: EncryptedData) {
        val base64Encoder = Base64.getEncoder()
        getEncryptedSharedPrefs(context).edit().apply {
            putString(
                Constants.MY_SECRET_IV_ALIAS,
                base64Encoder.encodeToString(encryptedData.initializationVector))
            putString(
                Constants.MY_SECRET_AUTH_KEY_ALIAS,
                base64Encoder.encodeToString(encryptedData.ciphertext))
            apply()
        }
    }

    fun getEncryptedSharedPrefs(context: Context): SharedPreferences {
        return EncryptedSharedPreferences.create(
            Constants.MY_SECRET_SHARED_PREF_FILE,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getCipher(): Cipher {
        val transformation = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"

        return Cipher.getInstance(transformation)
    }

    private fun getSecretKey(keyName: String): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        keyStore.getKey(keyName, null)?.let { return it as SecretKey }

        return createSecretKey(keyName)
    }

    private fun createSecretKey(keyName: String): SecretKey {
        val paramsBuilder = KeyGenParameterSpec.Builder(keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
        paramsBuilder.apply {
            setBlockModes(ENCRYPTION_BLOCK_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING)
            setKeySize(KEY_SIZE)
            setUserAuthenticationRequired(true)
        }

        val keyGenParams = paramsBuilder.build()
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE)
        keyGenerator.init(keyGenParams)
        return keyGenerator.generateKey()
    }
}