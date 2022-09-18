package driver.project.Service;

import driver.project.Mapper.CredentialMapper;
import driver.project.Model.Credential;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public int addCredential(Credential credential) {
        return credentialMapper.addCredential(credential);
    }

    public void updateCredential(Integer credentialId, String url, String username, String key, String password){
        credentialMapper.updateCredential(credentialId, url, username, key, password);
    }

    public Credential[] getAllCredentials(Integer userId) {
        return credentialMapper.getAllCredentials(userId);
    }

    public Credential getCredential(Integer credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    public void deleteCred(Integer credentialId) {
        credentialMapper.deleteCredential(credentialId);
    }
}
