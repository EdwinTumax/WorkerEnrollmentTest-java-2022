package edu.kalum.workerenrollment.core.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class QueueConsummerVerticle extends AbstractVerticle {

    private EventBus eventBus;
    private RabbitMQClient rabbitMQClient;
    private static final Logger logger = LoggerFactory.getLogger(QueueConsummerVerticle.class);

    @Override
    public void start(){
        this.eventBus = vertx.eventBus();

    }

    public void readMessageEvent(){
        JsonObject config = new JsonObject()
                .put("user",config().getString("edu.kalum.broker.username"))
                .put("password",config().getString("edu.kalum.broker.password"))
                .put("host",config().getString("edu.kalum.broker.host"))
                .put("port",config().getString("edu.kalum.broker.port"))
                .put("virtualHost",config().getString("edu.kalum.broker.virtualHost"))
                .put("queue",config().getString("edu.kalum.broker.virtualHost"));
        this.rabbitMQClient = RabbitMQClient.create(vertx, config);
        this.rabbitMQClient.start(startResult -> {
            if(startResult.succeeded()){
                logger.debug("Se realizo la conexión a rabbit satisfactoriamente");
            } else {
                logger.error("Hubo un error en el proceso de conexión a rabbit (".concat(startResult.cause().getMessage()).concat(")"));
            }
        });
    }
}
