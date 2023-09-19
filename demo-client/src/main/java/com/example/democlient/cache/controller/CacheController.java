package com.example.democlient.cache.controller;

import com.example.democlient.cache.model.CacheModel;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/client/cache")
@RequiredArgsConstructor
public class CacheController {

    // 단일 데이터

    @GetMapping("/data")
    @Cacheable(value = "TEST", key = "#id")
    public CacheModel getData(@RequestParam String id){
        System.out.println("Controller > getData 진입");

        // DB select 로직 수행
        CacheModel model = new CacheModel();
        model.setId(id);
        model.setText( "hello! my name is " + id );

        return model;
    }

    @GetMapping("/data/update")
    @CachePut(value = "TEST", key = "#id")
    public CacheModel updateData(@RequestParam String id){
        System.out.println("Controller > updateData 진입");

        // DB update 로직 수행
        CacheModel model = new CacheModel();
        model.setId(id);
        model.setText( "bye!" + id );

        return model;
    }

    @GetMapping("/data/delete")
    @CacheEvict(value = "TEST", key = "#id")
    public void deleteData(@RequestParam String id){
        System.out.println("Controller > deleteData 진입");

        // DB delete 로직 수행
        // ...

    }

    // ===================================================================

    // 리스트 데이터

    @GetMapping("/users")
    @Cacheable(value = "TEST", key = "#filter")
    public List<CacheModel> getUsers(@RequestParam String filter){
        System.out.println("Controller > getUsers 진입");

        // DB select 로직 수행
        CacheModel user1 = new CacheModel();
        user1.setId("hjjang");
        user1.setText( "hello! my name is juju");
        CacheModel user2 = new CacheModel();
        user2.setId("hkcha");
        user2.setText( "hello! my name is chacha");
        List<CacheModel> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);

        return list;
    }

    @GetMapping("/user/create")
    @CacheEvict(value = "TEST", allEntries = true)
    public void createUser(@RequestParam String id){
        System.out.println("Controller > createUser 진입");

        // DB create 로직 수행
        // ...
    }

    @GetMapping("/user/update")
    @CacheEvict(value = "TEST", allEntries = true)
    public void updateUser(@RequestParam String id){
        System.out.println("Controller > updateUser 진입");

        // DB update 로직 수행
        // ...
    }

    @GetMapping("/user/delete")
    @CacheEvict(value = "TEST", allEntries = true)
    public void deleteUser(@RequestParam String id){
        System.out.println("Controller > deleteUser 진입");

        // DB delete 로직 수행
        // ...
    }
}
