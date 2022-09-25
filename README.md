# **_RabbitMQ_**

> ### ADD NEW USER RABBITMQ:

###### Open PowerShell:
```sh
docker container ps
```
```sh
docker exec -it e1c2 bash
```
_note:_ 
- 'e1c2' = container id
  
```sh
rabbitmqctl add_user 'admin-rabbit' '123'
```
_note:_ 
- list users: ` rabbitmqctl list_users `
- delete users: ` rabbitmqctl delete_user 'admin-rabbit' `

```sh
rabbitmqctl set_user_tags admin-rabbit administrator
```

```sh
rabbitmqctl set_permissions -p "/" "admin-rabbit" ".*" ".*" ".*"
```

