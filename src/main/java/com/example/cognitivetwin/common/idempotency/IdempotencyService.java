package com.example.cognitivetwin.common.idempotency;

import com.example.cognitivetwin.infrastructure.redis.RedisService;
import com.fasterxml.jackson.core.JacksonException;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdempotencyService {

    private final IdempotencyRepository idempotencyRepository;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    private final Duration TTL = Duration.ofHours(48);

    public String generateRequestHash(Object request) throws JacksonException {
        String  hashedString =  Hashing.sha256().hashString(objectMapper.writeValueAsString(request),
                StandardCharsets.UTF_8).toString();
        return hashedString;
    }

    public String generateKey(String key){
        return "idempotency:" + key;
    }

    public boolean initiateRequest(String key,Object request) throws JacksonException{
        String redisKey = generateKey(key);
        IdempotencyEntity entity = new IdempotencyEntity();
        entity.setKey(redisKey);
        entity.setIdempotencyStatus(IdempotencyStatus.IN_PROGRESS);
        entity.setExpiryDate(Instant.now().plus(TTL));
        entity.setRequestHash(generateRequestHash(request));

        boolean locked =  redisService.setIfAbsent(redisKey,entity,TTL);

        if(!locked){
            return false;
        }

        idempotencyRepository.save(entity);
        return true;
    }

    public IdempotencyEntity getRecord(String key){
        return  redisService.get(generateKey(key),IdempotencyEntity.class);
    }

    public void completeRequest(String key,Object responseBody){
        try{
            String redisKey = generateKey(key);
            String serializedResponse = objectMapper.writeValueAsString(responseBody);
            IdempotencyEntity entity = new IdempotencyEntity();
            entity.setKey(redisKey);
            entity.setIdempotencyStatus(IdempotencyStatus.SUCCESS);
            entity.setResponse(serializedResponse);
            entity.setExpiryDate(Instant.now().plus(TTL));

            // Update Redis with the successful response
            redisService.set(redisKey, entity, TTL);

            // Persist to DB for auditing/long-term storage
            idempotencyRepository.save(entity);
        }catch (Exception e){
            log.error("Failed to serialize response for idempotency key : {}",key ,e);
        }
    }

    public void failRequest(String key){
        String redisKey = generateKey(key);
        IdempotencyEntity entity = idempotencyRepository.findByKey(redisKey);
        entity.setIdempotencyStatus(IdempotencyStatus.FAILED);
        idempotencyRepository.save(entity);
        redisService.delete(redisKey);
    }


}
