FROM node:slim

WORKDIR /app

ENV TZ Europe/Moscow

COPY ./dist/web .

EXPOSE 4000

ENTRYPOINT ["node", "server/server.mjs"]
