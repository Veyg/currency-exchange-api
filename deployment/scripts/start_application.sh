#!/bin/bash
cd /opt/currency-exchange-api
nohup java -jar currency-exchange-api.jar > app.log &
