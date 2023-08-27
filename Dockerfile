FROM golang:alpine as builder

ARG GOOS
ARG GOARCH

ENV CGO_ENABLED 0
ENV GOOS ${GOOS:-linux}
ENV GOARCH ${GOARCH:-amd64}

RUN apk update --no-cache && apk add --no-cache tzdata

WORKDIR /build

ADD go.mod .
ADD go.sum .
RUN go mod download

COPY . .
RUN go build -ldflags="-s -w" -o orchestrator orchestrator.go

FROM scratch

WORKDIR /app

COPY --from=builder /usr/share/zoneinfo/Europe/Moscow /usr/share/zoneinfo/Europe/Moscow
ENV TZ Europe/Moscow

COPY --from=builder /build/orchestrator /app/orchestrator

EXPOSE 8080

ENTRYPOINT ["/app/orchestrator"]