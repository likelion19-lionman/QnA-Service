docker buildx build \
    --platform linux/amd64 \
    -t seonghun120614/qna-next:1.0 ./nextjs-qna-app \
    --push

docker buildx build \
    --platform linux/amd64 \
    -t seonghun120614/qna-spring:1.0 ./spring-qna-app \
    --push

docker buildx build \
    --platform linux/amd64 \
    -t seonghun120614/qna-mysql:1.0 ./mysql-qna-db \
    --push