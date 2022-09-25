# mensageria-AMQP-RabbitMQ

ADD NEW USER RABBITMQ:
  
  Open PowerShell:
    - docker container ps
    - docker exec -it e1c2 bash
      OBS.: "e1c2" = container id
    - rabbitmqctl add_user 'user1' '123'
      OBS.: 
        list users: - rabbitmqctl list_users
        delete users - rabbitmqctl delete_user 'user1'
