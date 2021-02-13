package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * from credentials where userid=#{userid}")
    List<Credential> getAllCredentials(Integer userid);

    @Insert("INSERT into credentials (credentialid, url, username, key, password, userid) " +
            "values (#{credentialid}, #{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insertCredential(Credential credential);

    @Delete("DELETE from credentials where credentialid=#{credentialid} and userid=#{userid}")
    void deleteCredential(long credentialid, Integer userid);

    @Update("UPDATE credentials set url=#{credential.url}, username=#{credential.username}, key=#{credential.key}, " +
            "password=#{credential.password} " +
            "where credentialid=#{credential.credentialid} and userid=#{userid}")
    int updateCredential(Credential credential, Integer userid);

    @Select("SELECT * from credentials where credentialid=#{credentialid}")
    Credential getCredential(long credentialid);
}
