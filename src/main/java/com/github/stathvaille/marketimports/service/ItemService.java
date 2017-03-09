package com.github.stathvaille.marketimports.service;

import com.github.stathvaille.marketimports.domain.staticdataexport.Item;
import com.github.stathvaille.marketimports.domain.staticdataexport.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final Map<Long, Item> items;

    public ItemService(@Autowired Items items){
        this.items = items.getItems();
    }

    public Optional<Item> getItemByName(String itemName){
        List<Item> matchingItems = items.values().stream()
                                                 .filter(item -> item.getName().getEn() != null)
                                                 .filter(item -> item.getName().getEn().equals(itemName))
                                                 .collect(Collectors.toList());

        if (matchingItems.size() == 0) {
            return Optional.empty();
        }
        else if (matchingItems.size() == 1) {
            return Optional.of(matchingItems.get(0));
        }
        else {
            throw new RuntimeException(String.format("More than one item found with name %s", itemName));
        }
    }

    public Optional<Item> getItemById(Long itemId){
        return items.containsKey(itemId) ? Optional.of(items.get(itemId)) : Optional.empty();
    }
}
