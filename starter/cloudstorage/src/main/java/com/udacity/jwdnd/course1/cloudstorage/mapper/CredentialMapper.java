package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * from credentials")
    List<Credential> getAllCredentials();

    @Insert("INSERT into credentials (credentialid, url, username, key, password, userid) " +
            "values (#{credentialid}, #{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insertCredential(Credential credential);

    @Delete("DELETE from credentials where credentialid=#{credentialid}")
    void deleteCredential(long credentialid);

    @Update("UPDATE credentials set url=#{url}, username=#{username}, password=#{password} " +
            "where credentialid=#{credentialid}")
    int updateCredential(Credential credential);

    @Select("SELECT * from credentials where credentialid=#{credentialid}")
    Credential getCredential(long credentialid);
}
