package com.sga.registrymq;

/**
 * Recieving the status from Status Queue!
 *
 */

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sga.registrymq.Log;

public class App 
{
    private final static String QUEUE_NAME = "hello";
    public static void main( String[] args )
    {
        // Establishing a Connection with RabbitMQ server, 
		// running in the local machine, localhost
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("52.53.188.79");
		Connection connection;
		try {
			connection = factory.newConnection();

			// creating a channel with first_queue
			Channel channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			
			System.out.println("{N|T} Waiting for messages.");
			
			// creating the Consumer, that will be receive a message and convert to String
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					//System.out.println("Java Queue - Message Received '" + message + "'");
					Log log = jsontoClass(message);
					System.out.println(log.toString());
				}
			};
			// loop that waits for message 		
			channel.basicConsume(QUEUE_NAME, true, consumer);
		} catch (IOException e) {
			System.out.println("RabbitMQ server is Down !");
			System.out.println(e.getMessage());
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
    }

	public static Log jsontoClass(String message){
		ObjectMapper mapper = new ObjectMapper();
		try {
			Log log = mapper.readValue(message, Log.class);
			return log;
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Some error occurred while decoding JSON String");
		}
		return null;
	}
}
