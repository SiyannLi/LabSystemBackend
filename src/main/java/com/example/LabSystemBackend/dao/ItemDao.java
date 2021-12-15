package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemDao {
    List<Item> getAllItems();

    Item getItemByName(@Param("itemName") String itemName);

    Item getItemById(@Param("itemId") int itemId);

    int addItem(@Param("item") Item item);

    int deleteItem(@Param("itemId") int itemId);

    int changeItemAmount(@Param("itemId") int itemId, @Param("amount") int amount);

    int mergeItem(@Param("itemId") int itemId, @Param("targetId") int targetId);


}
