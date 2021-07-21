package com.squareoff.serviceImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.squareoff.dto.OrederRequestDto;
import com.squareoff.feingClient.UserClient;
import com.squareoff.model.OrderRequest;
import com.squareoff.model.OrderSuccess;
import com.squareoff.model.StockEntity;
import com.squareoff.model.UserEntity;
import com.squareoff.model.UserHoldingModel;
import com.squareoff.repository.OrderRepository;
import com.squareoff.repository.OrderSuccessRepository;
import com.squareoff.repository.StockRepository;
import com.squareoff.repository.UserHoldingRepo;
import com.squareoff.repository.UserRepository;

@Service
public class OrderServiceImpl {
    

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private UserHoldingRepo userHoldingRepo;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private StreamBridge streamBridge;
    
    @Autowired
    private OrderSuccessRepository orderSuccessRepository;
    
//    @Scheduled(cron = "0 0 5 * * MON-FRI")
//    @Scheduled(initialDelay = 10000L,fixedDelay = 50000L)
    public void relaseOrders() {
        
        
        List<OrderRequest> orderRequests = orderRepository.findAll();
        
        for (OrderRequest orderRequest : orderRequests) {
            UserEntity orderEntity = userRepository.findById(orderRequest.getUserId()).get();
            if (orderRequest.getType().equals("buy")) {
                orderEntity.setUserWallet(orderEntity.getUserWallet().add(orderRequest.getPrice().multiply(BigDecimal.valueOf(orderRequest.getQuantity()))));
            }else {
                List<UserHoldingModel> ordredUser = userHoldingRepo.getAllHoldingOfUser(orderEntity);
                for (UserHoldingModel orderRequest2 : ordredUser) {
                    if (orderRequest2.getStockEntity().getStockid().equals(orderRequest.getStockId())) {
                        orderRequest2.setQuantity(orderRequest2.getQuantity() + orderRequest.getQuantity());
                    }
                }
            }
            orderRepository.delete(orderRequest);
        }
    }

    public OrderRequest applyOrder(UserEntity userEntity, StockEntity stockEntity, OrederRequestDto requestDto) {
        try {
            streamBridge.send("notificationEventSupplier-out-0", MessageBuilder.withPayload(new OrderSuccess()).build());
            OrderRequest orderRequest = null;
            if (requestDto.getType().equals("buy")) {
                if (itIsValidOrder(stockEntity, userEntity, requestDto)) {
                    orderRequest = matchingOrder(requestDto, stockEntity, "sell", userEntity, "buy");
                }
            } else {
                if (isValidForSell(stockEntity, userEntity, requestDto)) {
                    orderRequest = matchingOrder(requestDto, stockEntity, "buy", userEntity, "sell");
                }
            }
            return orderRequest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private OrderRequest matchingOrder(OrederRequestDto requestDto, StockEntity stockEntity, String type1,
            UserEntity userEntity, String type) throws Exception {
        Long quantity = requestDto.getQuantity();
        try {
            List<OrderRequest> listorder = orderRepository.getAllorder(type1, requestDto.getStockId());
            for (OrderRequest orderRequest : listorder) {
                if (orderRequest.getPrice().compareTo(requestDto.getPrice()) == 0) {

                    UserEntity orderEntity = userClient.getUserInfo(orderRequest.getUserId());
                    
                    if (orderEntity.getUserId().equals(userEntity.getUserId())) {
                        continue;
                    }
                    
                    if (orderRequest.getQuantity() > quantity) {
                        executeOrder(requestDto, quantity, userEntity, orderEntity, type, stockEntity);
                        orderRequest.setQuantity(orderRequest.getQuantity() - quantity);
                        orderRepository.save(orderRequest);
                        OrderSuccess orderedSuccess = convertorToOrderSuccess(userEntity,quantity,orderRequest.getOrderId(),orderRequest.getPrice(),orderRequest.getStockId(),type);
                        OrderSuccess orderSuccess = convertorToOrderSuccess(orderEntity,quantity,orderRequest.getOrderId(),orderRequest.getPrice(),orderRequest.getStockId(),type1);
                        orderSuccessRepository.save(orderedSuccess);
                        orderSuccessRepository.save(orderSuccess);
                        saveAndSendMail(orderedSuccess,orderSuccess);
                        quantity = 0L;
                        break;
                    } else if (orderRequest.getQuantity() < quantity) {
                        executeOrder(orderRequest, orderRequest.getQuantity(), userEntity, orderEntity, type, stockEntity);
                        OrderSuccess orderedSuccess = convertorToOrderSuccess(userEntity,quantity,orderRequest.getOrderId(),orderRequest.getPrice(),orderRequest.getStockId(),type);
                        OrderSuccess orderSuccess = convertorToOrderSuccess(orderEntity,quantity,orderRequest.getOrderId(),orderRequest.getPrice(),orderRequest.getStockId(),type1);
                        orderSuccessRepository.save(orderedSuccess);
                        orderSuccessRepository.save(orderSuccess);
                        saveAndSendMail(orderedSuccess,orderSuccess);
                        quantity = quantity - orderRequest.getQuantity();
                    } else {
                        executeOrder(orderRequest, quantity, userEntity, orderEntity, type, stockEntity, 1);
                        OrderSuccess orderedSuccess = convertorToOrderSuccess(userEntity,quantity,orderRequest.getOrderId(),orderRequest.getPrice(),orderRequest.getStockId(),type);
                        OrderSuccess orderSuccess = convertorToOrderSuccess(orderEntity,quantity,orderRequest.getOrderId(),orderRequest.getPrice(),orderRequest.getStockId(),type1);
                        orderSuccessRepository.save(orderedSuccess);
                        orderSuccessRepository.save(orderSuccess);
                        saveAndSendMail(orderedSuccess,orderSuccess);
                        quantity = 0L;
                        break;
                    }
                }       

                if (quantity <= 0) {
                    break;
                }
            }
            
            OrderRequest orderRequest = new OrderRequest();
            if (quantity > 0) {
                orderRequest.setOrderDate(Date.valueOf(LocalDate.now()));
                orderRequest.setOrderTime(Time.valueOf(LocalTime.now()));
                orderRequest.setPrice(requestDto.getPrice());
                orderRequest.setQuantity(quantity);
                orderRequest.setStockId(stockEntity.getStockid());
                orderRequest.setType(requestDto.getType());
                orderRequest.setUserId(userEntity.getUserId());
//                if (type.equals("buy")) {
//                    userEntity.setUserWallet(
//                            userEntity.getUserWallet().subtract(BigDecimal.valueOf(quantity).multiply(requestDto.getPrice())));
//                } else if (type.equals("sell")){
//                    for (UserHoldingModel holding : userHoldingRepo.getAllHoldingOfUser(userEntity)) {
//                        if (holding.getStockEntity().getStockid() == stockEntity.getStockid()) {
//                            holding.setQuantity(holding.getQuantity() - quantity);
//                        }
//                    }
//                }
//                userRepository.save(userEntity);
                orderRepository.save(orderRequest);
                streamBridge.send("notificationEventSupplier-out-0", MessageBuilder.withPayload(convertorToOrderSuccess(orderRequest)).build());
            }
            if(orderRequest.getOrderId() == null) {
                orderRequest.setOrderId(-1L);
                return orderRequest;
            }else {
                return orderRequest;
            }
           
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveAndSendMail(OrderSuccess orderedSuccess, OrderSuccess orderSuccess) {
        streamBridge.send("notificationEventSupplier-out-0", MessageBuilder.withPayload(orderedSuccess).build());
        streamBridge.send("notificationEventSupplier-out-0", MessageBuilder.withPayload(orderSuccess).build());
    }

    private OrderSuccess convertorToOrderSuccess(UserEntity userEntity, Long quantity, Long orderId, BigDecimal price,
            Long stockId,String type) {
          
        OrderSuccess success = new OrderSuccess();
        success.setOrderId(orderId);
        success.setOrderDate(Date.valueOf(LocalDate.now()));
        success.setOrderTime(Time.valueOf(LocalTime.now()));
        success.setPrice(price);
        success.setType(type);
        success.setQuantity(quantity);
        success.setStockId(stockId);
        success.setUserId(userEntity.getUserId());
        return success;
        
    }

    private void executeOrder(OrderRequest orderRequest, Long quantity, UserEntity userEntity, UserEntity orderEntity,
            String type, StockEntity stockEntity, int i) throws Exception {
        List<UserHoldingModel> ordredUser = userHoldingRepo.getAllHoldingOfUser(userEntity);
        List<UserHoldingModel> orderUser = userHoldingRepo.getAllHoldingOfUser(orderEntity);

        UserHoldingModel orderUserHoldingModel = null;
        for (UserHoldingModel userHoldingModel : orderUser) {
            if (userHoldingModel.getStockEntity().getStockid().equals(stockEntity.getStockid())) {
                orderUserHoldingModel = userHoldingModel;
            }
        }
        UserHoldingModel ordredUserHoldingModel = null;
        for (UserHoldingModel userHoldingModel : ordredUser) {
            if (userHoldingModel.getStockEntity().getStockid().equals(stockEntity.getStockid())) {
                ordredUserHoldingModel = userHoldingModel;
            }
        }
        if (type.equals("buy")) {
            OrederRequestDto requestDto = new OrederRequestDto();
            requestDto.setQuantity(quantity);
            requestDto.setPrice(orderRequest.getPrice());
            if(!itIsValidOrder(stockEntity, userEntity, requestDto)) {
                return;
            }
            
            if(!isValidForSell(stockEntity, orderEntity, requestDto)) {
                return;
            }
            
            if (ordredUserHoldingModel != null) {
                ordredUserHoldingModel.setQuantity(orderUserHoldingModel.getQuantity() + quantity);
            } else {
                UserHoldingModel holdingModel = new UserHoldingModel();
                holdingModel.setQuantity(quantity);
                holdingModel.setStockEntity(stockEntity);
                holdingModel.setUserEntity(userEntity);
                ordredUser.add(holdingModel);
            }
            userEntity.setUserWallet(userEntity.getUserWallet()
                    .subtract(BigDecimal.valueOf(quantity).multiply(stockEntity.getStockPrice())));
            
            if (orderUserHoldingModel.getQuantity() - quantity != 0) {
                orderUserHoldingModel.setQuantity(orderUserHoldingModel.getQuantity() - quantity);
            }else {
                userHoldingRepo.delete(orderUserHoldingModel);
                orderUser.remove((Object)orderUserHoldingModel);
            }
            
            orderEntity.setUserWallet(orderEntity.getUserWallet()
                    .add(BigDecimal.valueOf(quantity).multiply(stockEntity.getStockPrice())));
            
            userEntity.setHolding(ordredUser);
            orderEntity.setHolding(orderUser);
            orderRepository.delete(orderRequest);
            userRepository.save(orderEntity);
            userRepository.save(userEntity);
            Optional<StockEntity> findById = stockRepository.findById(orderRequest.getStockId());
            if (findById.isPresent()) {
                findById.get().setStockPrice(orderRequest.getPrice());
                stockRepository.save(findById.get());
            }
        } else if (type.equals("sell")) {
            
            OrederRequestDto requestDto = new OrederRequestDto();
            requestDto.setQuantity(quantity);
            requestDto.setPrice(orderRequest.getPrice());
            if(!itIsValidOrder(stockEntity, orderEntity, requestDto)) {
                return;
            }
            
            if(!isValidForSell(stockEntity, userEntity, requestDto)) {
                return;
            }
            
            if (ordredUserHoldingModel != null) {
                if (ordredUserHoldingModel.getQuantity() - quantity != 0) {
                    ordredUserHoldingModel.setQuantity(ordredUserHoldingModel.getQuantity() - quantity);
                }else{
                    userHoldingRepo.delete(ordredUserHoldingModel);
                    ordredUser.remove((Object)ordredUserHoldingModel);
                }
                    
                
                userEntity.setUserWallet(userEntity.getUserWallet()
                        .add(BigDecimal.valueOf(quantity).multiply(stockEntity.getStockPrice())));
                
                if (orderUserHoldingModel != null) {
                    orderUserHoldingModel.setQuantity(orderUserHoldingModel.getQuantity() + quantity);
                } else {
                    UserHoldingModel holdingModel = new UserHoldingModel();
                    holdingModel.setQuantity(quantity);
                    holdingModel.setStockEntity(stockEntity);
                    holdingModel.setUserEntity(orderEntity);
                    orderUser.add(holdingModel);
                }
                orderEntity.setUserWallet(orderEntity.getUserWallet()
                        .subtract(BigDecimal.valueOf(quantity).multiply(stockEntity.getStockPrice())));
                userEntity.setHolding(ordredUser);
                orderEntity.setHolding(orderUser);
                orderRepository.delete(orderRequest);
                userRepository.save(orderEntity);
                userRepository.save(userEntity);
                Optional<StockEntity> findById = stockRepository.findById(orderRequest.getStockId());
                if (findById.isPresent()) {
                    findById.get().setStockPrice(orderRequest.getPrice());
                    stockRepository.save(findById.get());
                }
            } else {
                throw new Exception("Not executed");
            }
        }

    }

    private void executeOrder(OrderRequest orderRequest, Long quantity, UserEntity userEntity, UserEntity orderEntity,
            String type, StockEntity stockEntity) throws Exception {

        List<UserHoldingModel> ordredUser = userHoldingRepo.getAllHoldingOfUser(userEntity);
        List<UserHoldingModel> orderUser = userHoldingRepo.getAllHoldingOfUser(orderEntity);

        UserHoldingModel orderUserHoldingModel = null;
        for (UserHoldingModel userHoldingModel : orderUser) {
            if (userHoldingModel.getStockEntity().getStockid().equals(stockEntity.getStockid())) {
                orderUserHoldingModel = userHoldingModel;
            }
        }
        UserHoldingModel ordredUserHoldingModel = null;
        for (UserHoldingModel userHoldingModel : ordredUser) {
            if (userHoldingModel.getStockEntity().getStockid().equals(stockEntity.getStockid())) {
                ordredUserHoldingModel = userHoldingModel;
            }
        }
        if (type.equals("buy")) {
            OrederRequestDto requestDto = new OrederRequestDto();
            requestDto.setQuantity(quantity);
            requestDto.setPrice(orderRequest.getPrice());
            if(!itIsValidOrder(stockEntity, userEntity, requestDto)) {
                return;
            }
            
            if(!isValidForSell(stockEntity, orderEntity, requestDto)) {
                return;
            }
            
            if (ordredUserHoldingModel != null) {
                ordredUserHoldingModel.setQuantity(orderUserHoldingModel.getQuantity() + quantity);
            } else {
                UserHoldingModel holdingModel = new UserHoldingModel();
                holdingModel.setQuantity(quantity);
                holdingModel.setStockEntity(stockEntity);
                holdingModel.setUserEntity(userEntity);
                ordredUser.add(holdingModel);
            }
            
            userEntity.setUserWallet(userEntity.getUserWallet()
                    .subtract(BigDecimal.valueOf(quantity).multiply(stockEntity.getStockPrice())));
            
            if (orderUserHoldingModel.getQuantity() - quantity != 0) {
                orderUserHoldingModel.setQuantity(orderUserHoldingModel.getQuantity() - quantity);
            }else {
                userHoldingRepo.delete(orderUserHoldingModel);
                orderUser.remove((Object)orderUserHoldingModel);
            }
            
            orderEntity.setUserWallet(orderEntity.getUserWallet()
                    .add(BigDecimal.valueOf(quantity).multiply(stockEntity.getStockPrice())));
            
            userEntity.setHolding(ordredUser);
            orderEntity.setHolding(orderUser);
            orderRepository.delete(orderRequest);
            userRepository.save(orderEntity);
            userRepository.save(userEntity);
            Optional<StockEntity> findById = stockRepository.findById(orderRequest.getStockId());
            if (findById.isPresent()) {
                findById.get().setStockPrice(orderRequest.getPrice());
                stockRepository.save(findById.get());
            }
        } else if (type.equals("sell")) {
            OrederRequestDto requestDto = new OrederRequestDto();
            requestDto.setQuantity(quantity);
            requestDto.setPrice(orderRequest.getPrice());
            if(!itIsValidOrder(stockEntity, orderEntity, requestDto)) {
                return;
            }
            
            if(!isValidForSell(stockEntity, userEntity, requestDto)) {
                return;
            }
            if (ordredUserHoldingModel != null) {
                ordredUserHoldingModel.setQuantity(ordredUserHoldingModel.getQuantity() - quantity);
                userEntity.setUserWallet(userEntity.getUserWallet()
                        .add(BigDecimal.valueOf(quantity).multiply(stockEntity.getStockPrice())));
                if (orderUserHoldingModel != null) {
                    orderUserHoldingModel.setQuantity(orderUserHoldingModel.getQuantity() + quantity);
                } else {
                    UserHoldingModel holdingModel = new UserHoldingModel();
                    holdingModel.setQuantity(quantity);
                    holdingModel.setStockEntity(stockEntity);
                    holdingModel.setUserEntity(orderEntity);
                    orderUser.add(holdingModel);
                }
                orderEntity.setUserWallet(orderEntity.getUserWallet()
                        .subtract(BigDecimal.valueOf(quantity).multiply(stockEntity.getStockPrice())));
                userEntity.setHolding(ordredUser);
                orderEntity.setHolding(orderUser);
                orderRepository.delete(orderRequest);
                userRepository.save(orderEntity);
                userRepository.save(userEntity);
                Optional<StockEntity> findById = stockRepository.findById(orderRequest.getStockId());
                if (findById.isPresent()) {
                    findById.get().setStockPrice(orderRequest.getPrice());
                    stockRepository.save(findById.get());
                }
            } else {
                throw new Exception("Not executed");
            }
        }
    }

    private void executeOrder(OrederRequestDto requestDto, Long quantity, UserEntity userEntity, UserEntity orderEntity,
            String type, StockEntity stockEntity) throws Exception {

        List<UserHoldingModel> ordredUser = userHoldingRepo.getAllHoldingOfUser(userEntity);
        List<UserHoldingModel> orderUser = userHoldingRepo.getAllHoldingOfUser(orderEntity);

        UserHoldingModel orderUserHoldingModel = null;
        for (UserHoldingModel userHoldingModel : orderUser) {
            if (userHoldingModel.getStockEntity().getStockid().equals(stockEntity.getStockid())) {
                orderUserHoldingModel = userHoldingModel;
            }
        }
        UserHoldingModel ordredUserHoldingModel = null;
        for (UserHoldingModel userHoldingModel : ordredUser) {
            if (userHoldingModel.getStockEntity().getStockid().equals(stockEntity.getStockid())) {
                ordredUserHoldingModel = userHoldingModel;
            }
        }
        if (type.equals("buy")) {
            
            if(!itIsValidOrder(stockEntity, userEntity, requestDto)) {
                return;
            }
            
            if(!isValidForSell(stockEntity, orderEntity, requestDto)) {
                return;
            }
            
            if (ordredUserHoldingModel != null) {
                ordredUserHoldingModel.setQuantity(ordredUserHoldingModel.getQuantity() + quantity);
            } else {
                UserHoldingModel holdingModel = new UserHoldingModel();
                holdingModel.setQuantity(quantity);
                holdingModel.setStockEntity(stockEntity);
                holdingModel.setUserEntity(userEntity);
                ordredUser.add(holdingModel);
            }
            userEntity.setUserWallet(userEntity.getUserWallet()
                    .subtract(BigDecimal.valueOf(quantity).multiply(stockEntity.getStockPrice())));
            orderUserHoldingModel.setQuantity(ordredUserHoldingModel.getQuantity() - quantity);
            orderEntity.setUserWallet(orderEntity.getUserWallet()
                    .add(BigDecimal.valueOf(quantity).multiply(stockEntity.getStockPrice())));
            userEntity.setHolding(ordredUser);
            orderEntity.setHolding(orderUser);
            userRepository.save(orderEntity);
            userRepository.save(userEntity);
            Optional<StockEntity> findById = stockRepository.findById(requestDto.getStockId());
            if (findById.isPresent()) {
                findById.get().setStockPrice(requestDto.getPrice());
                stockRepository.save(findById.get());
            }
        } else if (type.equals("sell")) {
            
            if(!itIsValidOrder(stockEntity, orderEntity, requestDto)) {
                return;
            }
            
            if(!isValidForSell(stockEntity, userEntity, requestDto)) {
                return;
            }
            
            if (ordredUserHoldingModel != null) {
                
                if (ordredUserHoldingModel.getQuantity() - quantity != 0) {
                    ordredUserHoldingModel.setQuantity(ordredUserHoldingModel.getQuantity() - quantity);
                }else {
                    userHoldingRepo.delete(ordredUserHoldingModel);
                    ordredUser.remove((Object)ordredUserHoldingModel);
                }
                   
                
                userEntity.setUserWallet(userEntity.getUserWallet()
                        .add(BigDecimal.valueOf(quantity).multiply(stockEntity.getStockPrice())));
                if (orderUserHoldingModel != null) {
                    orderUserHoldingModel.setQuantity(orderUserHoldingModel.getQuantity() + quantity);
                } else {
                    UserHoldingModel holdingModel = new UserHoldingModel();
                    holdingModel.setQuantity(quantity);
                    holdingModel.setStockEntity(stockEntity);
                    holdingModel.setUserEntity(orderEntity);
                    orderUser.add(holdingModel);
                }
                orderEntity.setUserWallet(orderEntity.getUserWallet()
                        .subtract(BigDecimal.valueOf(quantity).multiply(stockEntity.getStockPrice())));
                userEntity.setHolding(ordredUser);
                orderEntity.setHolding(orderUser);
                userRepository.save(orderEntity);
                userRepository.save(userEntity);
                Optional<StockEntity> findById = stockRepository.findById(requestDto.getStockId());
                if (findById.isPresent()) {
                    findById.get().setStockPrice(requestDto.getPrice());
                    stockRepository.save(findById.get());
                }
            } else {
                throw new Exception("Not executed");
            }
        }
    }

    private boolean itIsValidOrder(StockEntity stockEntity, UserEntity userEntity, OrederRequestDto requestDto) {

        if (stockEntity != null) {

            return userEntity.getUserWallet()
                    .compareTo(requestDto.getPrice().multiply(BigDecimal.valueOf(requestDto.getQuantity()))) == 1;
        }
        return false;
    }

    private boolean isValidForSell(StockEntity stockEntity, UserEntity userEntity, OrederRequestDto requestDto) {
        if (stockEntity != null && userEntity != null) {
            List<UserHoldingModel> ordredUser = userHoldingRepo.getAllHoldingOfUser(userEntity);
            if (ordredUser != null) {
                for (UserHoldingModel hold : ordredUser) {
                    if (hold.getStockEntity().getStockid().equals(stockEntity.getStockid())) {
                        if (hold.getQuantity() >= requestDto.getQuantity()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    OrderSuccess convertorToOrderSuccess(OrderRequest orderRequest) {
        
        if (orderRequest!=null) {
            OrderSuccess success = new OrderSuccess();
            success.setOrderId(orderRequest.getOrderId());
            success.setOrderDate(orderRequest.getOrderDate());
            success.setOrderTime(orderRequest.getOrderTime());
            success.setPrice(orderRequest.getPrice());
            success.setType(orderRequest.getType());
            success.setQuantity(orderRequest.getQuantity());
            success.setStockId(orderRequest.getStockId());
            success.setUserId(orderRequest.getUserId());
            return success;
        }else {
            return null;
        }
    }

}




























