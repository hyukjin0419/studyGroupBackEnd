# ---------- Build Stage ----------
FROM gradle:8.7-jdk17-alpine AS builder
WORKDIR /workspace
COPY . .
# 필요시 테스트 건너뜀(-x test 제거 가능)
RUN gradle clean bootJar -x test

# ---------- Runtime Stage ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 빌드 산출물 JAR 복사
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar

# 운영/개발 공통 런타임 최적화
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -Duser.timezone=Asia/Seoul"

# Spring 프로필은 compose/Railway에서 주입 (여기선 기본값만 주석)
# ENV SPRING_PROFILES_ACTIVE=prod

# 문서용 포트 노출(필수는 아님)
EXPOSE 8080

# 컨테이너 헬스체크(앱 UP 확인)
HEALTHCHECK --interval=30s --timeout=3s --retries=3 CMD wget -qO- http://localhost:8080/actuator/health | grep '"status":"UP"' || exit 1

CMD ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
