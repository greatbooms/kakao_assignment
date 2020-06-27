# 카카오페이 뿌리기 API

## Introduction
- 카카오페이에는 머니 뿌리기 기능이 있습니다.
    - 사용자는 다수의 친구들이 있는 대화방에서 뿌릴 금액과 받아갈 대상의 숫자를
입력하여 뿌리기 요청을 보낼 수 있습니다.
    - 요청 시 자신의 잔액이 감소되고 대화방에는 뿌리기 메세지가 발송됩니다.
    - 대화방에 있는 다른 사용자들은 위에 발송된 메세지를 클릭하여 금액을 무작위로
받아가게 됩니다.

## 문제 해결 전략
- 헤더 정보
    - Interceptor를 통해 요청이 들어오기 전 헤더에 해당 값이 없을시 에러를 내뱉어 처리량을 줄인다.
- Token 생성
    - Apache Common Lang 라이브러리를 사용해 생성한 뒤 DB에 존재 하는지 체크한 후 없으면 입력하도록 구현했다.
- 뿌리기 금액 분배
    - 총 분배 금액에서 분배 인원 수 만큼 반복문을 돌려 매 반복문마다 분배금을 임의로 나눈다.
    - 분배금을 나눌때 임의의 상수를 하나 두어 편차를 줄인다.
    - 반복을 할때마다 총 분배금액에서 임의의 분배금을 빼고 마지막 반복에서는 남은 금액이 분배금액이 된다.
- 뿌리기 금액 받기
    - 받기 요청한 순서대로 저장된 분배금액을 받는다.
- 에러처리
    - 상태코드로 정상과 에러를 구분한다. 에러구분은 에러코드에 코드와 메세지를 정의하였다.
    - 비즈니스에러는 상태코드가 모두 400(BAD_REQUEST)으로 내려가며, 이외에 에러코드는 500(INTERNAL_SERVER_ERROR)으로 내려간다.
    - 비즈니스에러는 RuntimeException을 확장한 CommonException을 생성해 에러코드에서 정의된 코드와 메세지가 제이슨 형태로 응답 되도록 했다.

## ERD
![kakao_assignment_erd](https://user-images.githubusercontent.com/5744408/85914267-d48ec700-b876-11ea-840f-76441bc1c0f7.png)

## 실행방법
~~~
git clone https://github.com/greatbooms/kakao_assignment.git
cd kakao_assignment
mvn clean package
mvn spring-boot:run
~~~

## API
- 기본정보 입력
    - 요청
    ~~~
    curl -X GET http://127.0.0.1:50506/api/v1/roomUserInput -H "X-USER-ID:1" -H "X-ROOM-ID:testRoom0001"
    ~~~
    - 응답
    ~~~
    {"roomUserInfoKey":1,"roomId":"testRoom0001","userId":1,"regDate":"2020-06-27T03:33:47.536+00:00"}
    ~~~
- 뿌리기
    - 요청
    ~~~
    curl -X POST http://127.0.0.1:50506/api/v1/spread -H "X-USER-ID:1" -H "X-ROOM-ID:testRoom0001" -d '{"totalAmount":"100000","totalUserCount":"3"}' -H "Accept: application/json" -H "Content-Type: application/json"
    ~~~
    - 응답
    ~~~
    {"spreadInfoKey":1,"token":"aZV","totalAmount":100000,"totalUserCount":3,"spreadTime":"2020-06-27T03:39:07.708+00:00"}
    ~~~
- 받기
    - 요청
    ~~~
    curl -X PUT http://127.0.0.1:50506/api/v1/spread/aZV -H "X-USER-ID:2" -H "X-ROOM-ID:testRoom0001"
    ~~~
    - 응답
    ~~~
    {"spreadReceiveInfoKey":1,"roomUserInfoKey":2,"spreadInfoKey":1,"token":"aZV","receiveAmount":7696,"receiveTime":"2020-06-27T03:40:47.795+00:00"}
    ~~~
- 조회
    - 요청
    ~~~
    curl -X GET http://127.0.0.1:50506/api/v1/spread/aZV -H "X-USER-ID:1" -H "X-ROOM-ID:testRoom0001"
    ~~~
    - 응답
    ~~~
    {"spreadInfoKey":1,"token":"aZV","totalSpreadAmount":100000,"totalReceiveAmount":42976,"totalSpreadUserCount":3,"totalReceiveUserCount":2,"spreadTime":"2020-06-27T03:39:07.000+00:00","srInfoList":[{"userId":2,"receiveAmount":7696,"receiveTime":"2020-06-27T03:40:47.000+00:00"},{"userId":3,"receiveAmount":35280,"receiveTime":"2020-06-27T03:43:46.000+00:00"}]}
    ~~~
  
## 응답코드

| CODE | Message | 
|:--:|----|
| 1001 | HTTP header에 X-USER-ID 또는 X-ROOM-ID가 존재하지 않습니다. |
| 2001 | 참여하지 않은 방에 뿌리기 요청을 할 수 없습니다. |
| 2002 | 뿌리기 최소 인원 수는 1명 이상입니다. |
| 2003 | 현재 방의 총인원 수 보다 많은 뿌리기 요청을 할 수 없습니다. |
| 2004 | 현재 방의 총인원 수보다 적은 금액으로 뿌리기 요청을 할 수 없습니다. |
| 3001 | 참여하지 않은 방에 뿌리기 요청을 받을 수 없습니다. |
| 3002 | 찾을수 없는 뿌리기 요청입니다. |
| 3003 | 자신의 뿌리기 요청은 자신이 받을 수 없습니다. |
| 3004 | 이미 받은 뿌리기 요청은 다시 받을 수 없습니다. |
| 3005 | 만료된 뿌리기 요청입니다. |
| 4001 | 잘못된 요청 또는 만료된 요청입니다. |
| 4002 | 뿌리기 결과조회는 뿌리기 생성자만 가능합니다. |
