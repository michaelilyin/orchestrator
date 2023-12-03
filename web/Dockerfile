FROM node:slim

WORKDIR /app

ENV TZ Europe/Moscow

COPY ./dist/web .

EXPOSE 8080

ENTRYPOINT ["node", "server/server.mjs"]
