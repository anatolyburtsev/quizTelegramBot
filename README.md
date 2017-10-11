Rebuild container:
```./gradlew clean build buildDocker```

Run integrationTest:
```
docker run -d -p 8078:8078 -v ${PATH_TO_DIR}/tasks_test.txt:/tasks_test.txt onotolemobile/api_task:0.0.2-snapshot
docker run -d -p 8089:8089 -e "TASK_API_URL=http://${IP}:8078/" onotolemobile/api_person:0.0.3-snapshot
```

Run
```
docker run -d -p 8078:8078 -v ${PATH_TO_DIR}/tasks.txt:/tasks.txt onotolemobile/api_task:0.0.2-snapshot
docker run -d -p 8089:8089 -e "TASK_API_URL=http://${IP}:8078/" onotolemobile/api_person:0.0.3-snapshot
docker run -d -e "PERSON_API_URL=http://${IP}:8089/" -e "BOT_NAME=${BOT_NAME}" -e "BOT_TOKEN=${TOKEN}" onotolemobile/msu_quiz_middle_telegram:0.0.5
```

Load/Reload tasks
```
curl -XPOST localhost:8078/task/reload
```
