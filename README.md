1) как сделать запуск без клиента докера?
2) что такое mySql?
3) как прокинуть папки в файловую систему?
4) какая джава работает тут? на какой джаве написан Флинк?
5) как посмотреть Лаг в Флинке? Правильно я понимаю, что если есть отставание, то можно врубить больше нод и больше слотов?
6)  cd /tmp/flink-output/

docker-compose run --no-deps client flink list

docker-compose run --no-deps client flink stop 07aa0dea42934f0e723e2acba91f719e


docker-compose exec kafka kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic input


docker-compose build

Сценарий №1
1) Запускаем на одной ноде в один слот, смотрим файлы, смотрим чекпоитны, смотрим Lag, смотрим лог джоб менеджера
   docker-compose logs -f jobmanager
   
2) Останавливаем вручную, смотрим чекпоинты, саейвпоинты:
    docker-compose run --no-deps client flink list
    docker-compose run --no-deps client flink stop 89ca182b54d5193b3a4b2acac3a9a468
   
3) Восстанавливаем с тремя слотами, вместо одного
   docker-compose run --no-deps client flink run -s /tmp/flink-savepoints-directory/savepoint-89ca18-30877e67d006 -d /opt/ClickCountJob.jar --bootstrap.servers kafka:9092 --checkpointing --event-time

   docker-compose run --no-deps client flink run -p 3 -s /tmp/flink-savepoints-directory/savepoint-6b1c66-399eac83255b -d /opt/ClickCountJob.jar --bootstrap.servers kafka:9092 --checkpointing --event-time

4) Добавляем еще три ноды, которые автоматически регистрируются в application master
   docker-compose scale taskmanager=4

5) Убиваем первую, на которой занято два слота
   docker kill operations-playground_taskmanager_1
   docker kill operations-playground_taskmanager_2