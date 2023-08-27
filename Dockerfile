FROM --platform=$BUILDPLATFORM golang:alpine as builder

ARG BUILDPLATFORM
ENV CGO_ENABLED 0
ENV GOOS linux

RUN apk update --no-cache && apk add --no-cache tzdata

WORKDIR /build

ADD go.mod .
#ADD go.sum .
RUN go mod download

COPY . .
RUN go build -ldflags="-s -w" -o orchestrator orchestrator.go

FROM --platform=$BUILDPLATFORM scratch

WORKDIR /app

COPY --from=builder /usr/share/zoneinfo/Europe/Moscow /usr/share/zoneinfo/Europe/Moscow
ENV TZ Europe/Moscow

COPY --from=builder /build/orchestrator /app/orchestrator

EXPOSE 8080

ENTRYPOINT ["/app/orchestrator"]