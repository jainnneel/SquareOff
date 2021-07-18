package com.squareoff.serviceImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.squareoff.dto.MoneyAddRequest;
import com.squareoff.dto.PaymentRequest;
import com.squareoff.dto.UserDto;
import com.squareoff.model.AddPayment;
import com.squareoff.model.UserEntity;
import com.squareoff.repository.PaymentRepository;
import com.squareoff.repository.UserRepository;


@Service
public class UserServiceImplementation {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private StreamBridge streamBridge;

    public String createUser(UserDto userDto) {
        try {
            UserEntity entity =  userRepository.findByuserMobie(userDto.getUserMobie());
//            entity = null;
//            
            if (entity!=null) {
                return "already registred";
            }else {
//                UserEntity userEntity = new UserEntity();
                createuser();
//                Keycloak kc = Keycloak.getInstance("http://localhost:8080/auth", "SquareOff", "neel", "123", "squareoff", "4c722b59-2648-43a2-922a-4e11d0c638fd");
//                CredentialRepresentation credential = new CredentialRepresentation();
//                credential.setType(CredentialRepresentation.PASSWORD);
//                credential.setValue("123");
//                UserRepresentation user = new UserRepresentation();
//                user.setUsername("testuser");
//                user.setFirstName("Test");
//                user.setLastName("User");
//                user.setCredentials(Arrays.asList(credential));
//                user.setEnabled(true) ;
//                try {
////                    kc.realm("SquareOff").users().create(user);
//                } catch (ClientErrorException e) {
//                    handleClientErrorException(e);
//                }  catch (Exception e) {
//                    Throwable cause = e.getCause();
//                    if (cause instanceof ClientErrorException) {
//                        handleClientErrorException((ClientErrorException) cause);
//                    } else {
//                        e.printStackTrace();
//                    }
//                }
//                userEntity.setUserMobie(userDto.getUserMobie());
//                userEntity.setUserName(userDto.getUserName());
//                userEntity.setUserPassword(userDto.getUserPassword());
//                userEntity.setUserWallet(BigDecimal.valueOf(0L));
//                userEntity.setUserEnable(true);
//                userRepository.save(userEntity);
                return "registred";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "somthig went wrong";
        }
    }
    
    private void createuser() {
        String serverUrl = "http://localhost:8080/auth";
        String realm = "SquareOff";
        // idm-client needs to allow "Direct Access Grants: Resource Owner Password Credentials Grant"
        String clientId = "squareoff";
        String clientSecret = "fde6501d-343c-49ff-8c2b-0edf88e32c77";
        Keycloak keycloak = KeycloakBuilder.builder() //
                .serverUrl(serverUrl) //
                .realm(realm) //
                .clientId(clientId) //
                .clientSecret(clientSecret) //
                .username("neel") //
                .password("1234") //
                .build();

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue("test");

        
        // Define user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername("tester1");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setCredentials(Arrays.asList(passwordCred));
        user.setEmailVerified(true);
        user.setRealmRoles(Arrays.asList("user"));
        // Get realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersRessource = realmResource.users();

        // Create user (requires manage-users role)

        try {
            Response response = usersRessource.create(user);
            System.out.printf("Repsonse: %s %s%n", response.getStatus(), response.getStatusInfo());
            System.out.println(response.getLocation());
        } catch (ClientErrorException e) {
            handleClientErrorException(e);
        }  catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof ClientErrorException) {
                handleClientErrorException((ClientErrorException) cause);
            } else {
                e.printStackTrace();
            }
        }
        
        
    }

    private static void handleClientErrorException(ClientErrorException e) {
//        e.printStackTrace();
//        javax.ws.rs.core.Response response = e.getResponse();
//        try {
//            System.out.println("status: " + response.getStatus());
//            System.out.println("reason: " + response.getStatusInfo().getReasonPhrase());
//            Map error = JsonSerialization.readValue((ByteArrayInputStream) response.getEntity(), Map.class);
//            System.out.println("error: " + error.get("error"));
//            System.out.println("error_description: " + error.get("error_description"));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

    public UserEntity getUserInfo(long id, String mobile) {
        
        UserEntity entity =  userRepository.findByuserMobie(mobile);
        
        return entity;
    }
    
 public UserEntity getUserInfo(String id) {
        
        UserEntity entity =  userRepository.findById(id).get();
//        for (UserHoldingModel holdingModel : entity.getHolding()) {
//            holdingModel.getHodingId();
//            holdingModel.getQuantity();
//            holdingModel.getStockEntity();
//            holdingModel.getUserEntity();
//        };
        
        return entity;
    }

    public String addpaymentrequest(MoneyAddRequest addRequest, String userId) {
        
        try {
        UserEntity entity = getUserInfo(userId);
        
        if (entity!=null) {
               RazorpayClient razorpayClient = new RazorpayClient("rzp_test_9B8AXwdinhrVTx", "aeRfMEbWIgA3mA2fOsY7LU0G");
               JSONObject options = new JSONObject();
               options.put("amount", addRequest.getAmount().multiply(BigDecimal.valueOf(100)));
               options.put("currency", "INR");
               Order order = razorpayClient.Orders.create(options);
               
               AddPayment addpayment = new AddPayment();
               addpayment.setAmount(addRequest.getAmount());
               addpayment.setOrderId(order.get("id"));
               addpayment.setPaymentDate(Date.valueOf(LocalDate.now()));
               addpayment.setPaymentTime(Time.valueOf(LocalTime.now()));
               addpayment.setSucess(false);
               addpayment.setUserId(entity.getUserId());
               
               paymentRepository.save(addpayment);
               
               return order.get("id");
            }
        } catch (Exception e) {
            return "something went wrong";
        }
        return "something went wrong";
    }

    public boolean verifyandaddUser(PaymentRequest paymetreq, String userId) {
        try {
            AddPayment addPayment =  paymentRepository.getOrderByOrderId(paymetreq.getOrderId());
            RazorpayClient razorpayClient = new RazorpayClient("rzp_test_9B8AXwdinhrVTx", "aeRfMEbWIgA3mA2fOsY7LU0G");
            Payment payment = razorpayClient.Payments.fetch(paymetreq.getPaymentId());
            
            if (addPayment.getUserId().equals(userId) &&  payment!=null && payment.get("order_id").toString().trim().equals(paymetreq.getOrderId()) && payment.get("status").toString().trim().equals("captured")) {
                addPayment.setPaymentId(paymetreq.getOrderId());
                addPayment.setPaymentDate(Date.valueOf(LocalDate.now()));
                addPayment.setPaymentTime(Time.valueOf(LocalTime.now()));
                addPayment.setSucess(true);
                paymentRepository.save(addPayment);
                streamBridge.send("notificationEventSupplier-out-0", addPayment);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        
        return false;
    }
    
}
























