package com.github.stathvaille.marketimports.imports.service;

import com.github.stathvaille.marketimports.imports.domain.ZkilboardKillmail;
import com.github.stathvaille.marketimports.esi.domain.ESIKillmail;
import com.github.stathvaille.marketimports.imports.domain.staticdataexport.Item;
import com.github.stathvaille.marketimports.killmails.ZkillboardService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ZkillboardServiceTest {

    @Autowired
    ZkillboardService zkillboardService;
    @Autowired ItemService itemService;
    @Autowired @Qualifier("interestingItems") List<Item> interestingItems;

    private int braveAllianceId = 99003214;

    @Ignore
    @Test
    public void loadAllianceKillmails() {

        List<ZkilboardKillmail> zkilboardKillmails = zkillboardService.loadAllianceKillmails(braveAllianceId);
        List<ESIKillmail> esiKillmails = zkilboardKillmails.stream()
                .map(killmail -> zkillboardService.esiLookupKillmaail(killmail.getZkb().getHash(), killmail.getKillmailId()))
                .collect(Collectors.toList());

        Map<Long, Long> shipIdToCount = esiKillmails.stream().collect(Collectors.groupingBy(k -> k.getVictim().getShip_type_id(), Collectors.counting()));
        Map<Item, Long> shipToCountLost = shipIdToCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                    e -> itemService.getItemById(e.getKey()).get(),
                    e -> e.getValue(),
                    (o1, o2) -> o1, LinkedHashMap::new
                ));

        Map<Long, Long> itemIdToNumLost = esiKillmails.stream().map(k -> k.getVictim().getItems().stream().collect(Collectors.groupingBy(i -> i.getItem_type_id(), Collectors.summingLong(i -> i.getQuantity_destroyed() + i.getQuantity_dropped()))))
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue(),
                        (v1, v2) -> v1 + v2
                ));
        Map<Item, Long> itemToNumLost = itemIdToNumLost.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                    e -> itemService.getItemById(e.getKey()).get(),
                    e -> e.getValue(),
                    (o1, o2) -> o1, LinkedHashMap::new
                ));

        Map<Item, Long> itemsNotInInterestingItems = itemToNumLost.entrySet().stream()
                .filter(e -> !interestingItems.contains(e.getKey()))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new));

        LinkedHashMap<Item, Long> filteredItemsNotInInterestingItemsList = itemsNotInInterestingItems.entrySet()
                .stream()
                .filter(e -> e.getKey().getGroupID() != 85)
                .filter(e -> e.getKey().getGroupID() != 83)
                .filter(e -> e.getKey().getGroupID() != 460)
                .filter(e -> e.getKey().getGroupID() != 386)
                .filter(e -> e.getKey().getGroupID() != 376)
                .filter(e -> e.getKey().getGroupID() != 467)
                .filter(e -> e.getKey().getGroupID() != 387)
                .filter(e -> e.getKey().getName().getEn().endsWith(" II"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new));

        fail();
    }
}