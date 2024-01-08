package com.pc.greenbay.services;

import com.pc.greenbay.models.DTOs.ErrorDTO;
import com.pc.greenbay.models.Purchase;
import com.pc.greenbay.models.RequestDTOs.ItemRequestDTO;
import com.pc.greenbay.models.ResponseDTOs.*;
import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.User;
import com.pc.greenbay.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final PurchaseService purchaseService;
    private final BidService bidService;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, PurchaseService purchaseService, BidService bidService) {
        this.itemRepository = itemRepository;
        this.purchaseService = purchaseService;
        this.bidService = bidService;
    }

    @Override
    public Map<String, String> buildErrorResponseForItemCreation(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errors;
    }
    @Override
    public ItemResponseDTO createItem(ItemRequestDTO itemRequestDTO, User seller) throws Exception {
        Item itemToSave = new Item(itemRequestDTO, seller);
        try {
            itemRepository.save(itemToSave);
        } catch (Exception e) {
            throw new Exception("Database failure", e);
        }
        return new ItemResponseDTO(itemToSave.getId().toString(),
                itemToSave.getName(),
                itemToSave.getDescription(),
                itemToSave.getPhotoURL(),
                itemToSave.getStartingPrice(),
                itemToSave.getPurchasePrice());
    }
    @Override
    public boolean isItemSellable(Item item) {
        return item.isSellable();
    }

    @Override
    public List<ItemListDTO> listItems() {
         return itemRepository.findAll().stream()
                 .map(o -> new ItemListDTO(o.getId(), o.getName(), o.getDescription(), o.getPhotoURL(), o.getLastBid(), o.isSellable(), o.getSeller().getUsername())).collect(Collectors.toList());
    }
    @Override
    public Optional<Item> getItemById(UUID id) {
        return itemRepository.findItemById(id);
    }

    @Override
    public void saveLastBid(Item item, int bidAmount) {
        item.setLastBid(bidAmount);
        itemRepository.save(item);
    }
    @Override
    public void makeNotSellable(Item item) {
        item.setSellable(false);
        itemRepository.save(item);
    }
    @Override
    public ItemCommonResponseDTO showItemDetails(UUID id) throws Exception {
        Optional<Item> optionalItem = getItemById(id);
        if(optionalItem.isEmpty()) {
            throw new Exception("Item not found.");
        }
        Item item = optionalItem.get();
        if(!item.isSellable()) {
            Optional<Purchase> optionalPurchase = purchaseService.getPurchaseByItem(item);
            if(optionalPurchase.isEmpty()){
                throw new Exception("Purchase record not found.");
            }
            return new ItemNotSellableResponseDTO(item.getName(), item.getDescription(), item.getPhotoURL(), item.getSeller().getUsername(),
                                                    optionalPurchase.get().getBuyer().getUsername(),
                                                    optionalPurchase.get().getPurchaseAmount());
        }
        List<BidListDTO> bidList = bidService.findBidsByItem(item);
        return new ItemSellableResponseDTO(item.getName(), item.getDescription(), item.getPhotoURL(), item.getSeller().getUsername(),
                                            bidList);

    }
    @Override
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Page<Item> getItemsBySellableTrueAndPage(int page) {
        Pageable pageable = PageRequest.of(page, 3); // 3 items per page
        return itemRepository.findAllBySellableTrue(pageable);
    }

    @Override
    public ResponseEntity<?> getItemsPaged(int page) {
        Page<Item> itemPage = getItemsBySellableTrueAndPage(page - 1);

        if (itemPage.hasContent() && page <= itemPage.getTotalPages()) {
            Map<String, Object> response = new HashMap<>();
            response.put("page", page);
            response.put("total_pages", itemPage.getTotalPages());
            response.put("items", itemPage.getContent().stream()
                    .map(o -> new ItemPageDTO(o.getId(), o.getName(), o.getPhotoURL(), o.getLastBid(), o.getSeller().getUsername()))
                    .collect(Collectors.toList()));

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(new ErrorDTO("There is no page: " + page));
        }
    }
}
