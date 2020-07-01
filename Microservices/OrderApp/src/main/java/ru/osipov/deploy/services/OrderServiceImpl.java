package ru.osipov.deploy.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import ru.osipov.deploy.entities.Order;
import ru.osipov.deploy.entities.OrderItem;
import ru.osipov.deploy.models.CreateOrder;
import ru.osipov.deploy.models.CreateOrderItem;
import ru.osipov.deploy.models.OrderInfo;
import ru.osipov.deploy.models.UpdateOrder;
import ru.osipov.deploy.repositories.OrderItemRepository;
import ru.osipov.deploy.repositories.OrderRepository;

import javax.annotation.Nonnull;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class OrderServiceImpl implements OrderService {

    private static final Logger logger = getLogger(OrderServiceImpl.class);

    protected OrderRepository rep;

    protected OrderItemRepository itrep;

    @Autowired
    public OrderServiceImpl(OrderRepository r1, OrderItemRepository r2){
        this.rep = r1;
        this.itrep = r2;
    }

    @Nonnull
    @Override
    public OrderInfo getById(long oid) {
        logger.info("Get order by id = '{}'",oid);
        Optional<Order> o = rep.findById(oid);
        if(o.isPresent()){
            logger.info("Order was found.");
            Order e = o.get();
            return ModelBuilder.createOrderInfo(e);
        }
        logger.info("Order was not found!! Return empty.");
        return new OrderInfo(-1l,-1l,-1d,null,null,null,null,null);
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<OrderInfo> getByCreationDate(LocalDate date) {
        logger.info("get orders by created date = '{}'",date.toString());
        return rep.findByCreatedAt(date).stream().map(ModelBuilder::createOrderInfo).collect(Collectors.toList());
    }



    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<OrderInfo> getByCreationTime(LocalTime time) {
        logger.info("get orders by created time = '{}'",time.toString());
        return rep.findByCreatedTime(time).stream().map(ModelBuilder::createOrderInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<OrderInfo> getByUpdatedDate(LocalDate date) {
        logger.info("get orders by updated date = '{}'",date.toString());
        return rep.findByUpdatedAt(date).stream().map(ModelBuilder::createOrderInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<OrderInfo> getByStatus(String status) {
        logger.info("get orders by status = '{}'",status);
        return rep.findByStatus(status).stream().map(ModelBuilder::createOrderInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<OrderInfo> getByUid(long uid) {
        logger.info("get orders by user id = '{}'",uid);
        return rep.findByUid(uid).stream().map(ModelBuilder::createOrderInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<OrderInfo> getBySum(double sum) {
        logger.info("get orders by sum = '{}'",sum);
        return rep.findBySum(sum).stream().map(ModelBuilder::createOrderInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional
    public URI createOrder(CreateOrder req) {
        List<CreateOrderItem> lines = req.getItems();
        List<OrderItem> olines = new ArrayList<>();

        logger.info("Create order");
        Order o = new Order()
                .setUid(req.getUid())
                .setStatus(req.getStatus())
                .setCreatedAt(req.getCreated())
                .setCreatedTime(req.getCtime())
                .setUpdatedAt(req.getUpdated())
                .setSum(req.getSum())
                .setItems(new HashSet<OrderItem>(olines));

        for(CreateOrderItem it : lines){
            OrderItem line = new OrderItem()
                    .setPrice(it.getPrice())
                    .setDiscount(it.getDiscount())
                    .setSid(it.getSeanceId())
                    .setSeatId(it.getSeatId())
                    .setOrder(o);
        }
        logger.info("Data was set. Try to save order");
        rep.save(o);
        logger.info("Order was created. Saving order-lines...");
        itrep.saveAll(olines);
        logger.info("Successful saved.");
        return URI.create("/v1/orders/"+o.getOid());
    }

    @Nonnull
    @Override
    @Transactional
    public OrderInfo updateOrder(UpdateOrder req) {
        long id = req.getId();
        logger.info("Getting order by id = '{}'",id);
        Optional<Order> o = rep.findById(id);
        if(o.isPresent()){
            logger.info("Successful found. Updating order...");
            Order it = o.get();
            it.setStatus(req.getStatus());
            rep.save(it);
            logger.info("Successful updated.");
            return ModelBuilder.createOrderInfo(it);
        }
        logger.info("Order was not found!! Return empty.");
        return new OrderInfo(-1l,-1l,-1d,null,null,null,null,null);
    }

    @Nonnull
    @Override
    @Transactional
    public OrderInfo deleteOrder(long id) {
        logger.info("Getting order by id = '{}'",id);
        Optional<Order> o = rep.findById(id);
        if(o.isPresent()){
            logger.info("Successful found. Deleting order...");
            rep.delete(o.get());
            logger.info("Successful deleted.");
            return ModelBuilder.createOrderInfo(o.get());
        }
        logger.info("Order was not found!! Return empty.");
        return new OrderInfo(-1l,-1l,-1d,null,null,null,null,null);
    }
}
