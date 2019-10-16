// Copyright (c) 2012-2019 The Elastos Open Source Project
// Distributed under the MIT software license, see the accompanying
// file COPYING or http://www.opensource.org/licenses/mit-license.php.

package org.elastos.wallet.core;

/**
 * IDChainSubWallet jni
 */
public class IDChainSubWallet extends SidechainSubWallet {
    private long mInstance;

    public String CreateIDTransaction(String payloadJson, String memo) throws WalletException {
        return CreateIDTransaction(mInstance, payloadJson, memo);
    }

    public String GenerateDIDInfoPayload(String inputJson, String passwd) throws WalletException {
        return GenerateDIDInfoPayload(mInstance, inputJson, passwd);
    }

    public String GetAllDID(int start, int count) throws WalletException {
        return GetAllDID(mInstance, start, count);
    }

    public String Sign(String did, String message, String payPassword) throws WalletException {
        return Sign(mInstance, did, message, payPassword);
    }

    public boolean VerifySignature(String publicKey, String message, String signature) throws WalletException {
        return VerifySignature(mInstance, publicKey, message, signature);
    }

    public String GetDIDByPublicKey(String publicKey) throws WalletException {
        return GetDIDByPublicKey(mInstance, publicKey);
    }

    public String GetDetailByDID(String did) throws WalletException {
        return GetDetailByDID(mInstance, did);
    }

    public IDChainSubWallet(long instance) {
        super(instance);
        mInstance = instance;
    }

    private native String CreateIDTransaction(long instance, String payloadJSON, String memo);

    private native String GenerateDIDInfoPayload(long instance, String inputJson, String passwd);

    private native String GetAllDID(long instance, int start, int count);

    private native String Sign(long instance, String did, String message, String payPassword);

    private native boolean VerifySignature(long instance, String publicKey, String message, String signature);

    private native String GetDIDByPublicKey(long instance, String publicKey);

    private native String GetDetailByDID(long instance, String did);
}
