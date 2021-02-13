package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getAllCredentials(Integer userid){
        return credentialMapper.getAllCredentials(userid);
    }

    public void insertCredential(Credential credential) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        credential.setKey(salt);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());

        credentialMapper.insertCredential(new Credential(null,
                credential.getUrl(), credential.getUsername(), credential.getKey(),
                encryptedPassword,credential.getUserid()));
    }

    public void deleteCredential(long credentialid, Integer userid){
        credentialMapper.deleteCredential(credentialid, userid);
    }

    public void updateCredential(Credential cred, Integer userid){
        Credential credential = getCredential(cred.getCredentialid());
        String encryptedPassword = encryptionService.encryptValue(cred.getPassword(), credential.getKey());

        credentialMapper.updateCredential(new Credential(credential.getCredentialid(),
                credential.getUrl(), credential.getUsername(), credential.getKey(),
                encryptedPassword, credential.getUserid()), userid);
    }

    public Credential getCredential(long credentialid){
        return credentialMapper.getCredential(credentialid);
    }
}
