package com.pc.greenbay.services;

import com.pc.greenbay.models.Bid;
import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.Purchase;
import com.pc.greenbay.models.ResponseDTOs.BidCommonResponseDTO;
import com.pc.greenbay.models.ResponseDTOs.BidListDTO;
import com.pc.greenbay.models.ResponseDTOs.BidPlacedResponseDTO;
import com.pc.greenbay.models.ResponseDTOs.ItemBoughtResponseDTO;
import com.pc.greenbay.models.User;
import com.pc.greenbay.repositories.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final ItemService itemService;
    private final PurchaseService purchaseService;
    private final UserService userService;

    @Autowired
    public BidServiceImpl(BidRepository bidRepository, @Lazy ItemService itemService, PurchaseService purchaseService, UserService userService) {
        this.bidRepository = bidRepository;
        this.itemService = itemService;
        this.purchaseService = purchaseService;
        this.userService = userService;
    }

    @Override
    public BidCommonResponseDTO placeBid(UUID itemId, User bidder, int bidAmount) throws Exception {
        Optional<Item> optionalItem = itemService.getItemById(itemId);
        if(bidder.getBalance() <= 0) {
            throw new Exception("You have no greenBay dollars, you can't bid.");
        }
        if(optionalItem.isEmpty()) {
            throw new Exception("Item not found.");
        }
        Item item = optionalItem.get();
        if(bidder.getId().equals(item.getSeller().getId())) {
            throw new Exception("You cannot bid on your own item.");
        }
        if(!item.isSellable()) {
            throw new Exception("Item is not sellable.");
        }
        if(bidder.getBalance() < bidAmount) {
            throw new Exception("You have not enough greenBay dollars on your account.");
        }
        if(bidAmount < item.getStartingPrice() || bidAmount <= item.getLastBid()) {
            throw new Exception("Your bid is too low.");
        }
        if(bidAmount > item.getLastBid() && bidAmount < item.getPurchasePrice() && bidder.getBalance() >= bidAmount) {
            try {
                itemService.saveLastBid(item, bidAmount);
                Bid bid = new Bid(item, bidder, bidAmount);
                bidRepository.save(bid);
                return new BidPlacedResponseDTO(item.getName(), item.getDescription(), item.getPhotoURL(), item.getSeller().getUsername(), bidAmount);
            } catch (Exception e) {
                throw new Exception("Placing your bid has failed.");
            }
        }
        if(bidAmount >= item.getPurchasePrice() && bidder.getBalance() >= bidAmount) {
            try {
                itemService.saveLastBid(item, bidAmount);
                itemService.makeNotSellable(item);
                Bid bid = new Bid(item, bidder, bidAmount);
                bidRepository.save(bid);
                Purchase purchase = new Purchase(item, bidder, bidAmount);
                purchaseService.savePurchase(purchase);
                bidder.setBalance(bidder.getBalance() - bidAmount);
                userService.saveUser(bidder);
                return new ItemBoughtResponseDTO(item.getName(), item.getDescription(), item.getPhotoURL(), item.getSeller().getUsername(), bidder.getUsername(), bidAmount);
            } catch (Exception e) {
                throw new Exception("Purchase has failed.");
            }
        }
        return null;
    }

    @Override
    public List<BidListDTO> findBidsByItem(Item item) {
        return bidRepository.findAllByItem(item).stream()
                .map(o -> new BidListDTO(o.getId(), o.getBidder().getUsername(), o.getBidAmount())).collect(Collectors.toList());
    }
}
