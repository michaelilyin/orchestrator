FROM golang:alpine as builder

ARG GOOS
ARG GOARCH

ENV CGO_ENABLED 0
ENV GOOS ${GOOS:-linux}
ENV GOARCH ${GOARCH:-amd64}

RUN apk update --no-cache && apk add --no-cache ca-certificates tzdata libcap

WORKDIR /build

ADD go.mod .
ADD go.sum .
RUN go mod download

COPY . .
RUN go build -ldflags="-s -w" -o orchestrator orchestrator.go
RUN setcap cap_net_raw=+ep orchestrator

FROM scratch

WORKDIR /app

COPY --from=builder /usr/share/zoneinfo/Europe/Moscow /usr/share/zoneinfo/Europe/Moscow
ENV TZ Europe/Moscow

COPY --from=builder /build/orchestrator /app/orchestrator
COPY templates /app/templates
COPY static /app/static

EXPOSE 8080

ENTRYPOINT ["/app/orchestrator"]