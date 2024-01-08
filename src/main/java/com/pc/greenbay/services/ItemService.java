package com.pc.greenbay.services;

import com.pc.greenbay.models.RequestDTOs.ItemRequestDTO;
import com.pc.greenbay.models.ResponseDTOs.ItemCommonResponseDTO;
import com.pc.greenbay.models.ResponseDTOs.ItemListDTO;
import com.pc.greenbay.models.ResponseDTOs.ItemResponseDTO;
import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ItemService {

    Map<String, String> buildErrorResponseForItemCreation(BindingResult bindingResult);

    ItemResponseDTO createItem(ItemRequestDTO itemRequestDTO, User seller) throws Exception;

    boolean isItemSellable(Item item);

    List<ItemListDTO> listItems();

    Optional<Item> getItemById(UUID id);

    void saveLastBid(Item item, int bidAmount);

    void makeNotSellable(Item item);

    ItemCommonResponseDTO showItemDetails(UUID id) throws Exception;

    Item saveItem(Item item);

    Page<Item> getItemsBySellableTrueAndPage(int page);

    ResponseEntity<?> getItemsPaged(int page);
}
