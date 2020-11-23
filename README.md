# 2020_카카오페이 server 개발 과제 
## 목차
- [개발 환경](#개발-환경)
- [핵심 문제해결 전략](#핵심-문제해결-전략])
- [ER 다이어그램](#ER-다이어그램)
- [API 명세](#API-명세)

---

## 개발 환경
- 기본 환경
    - IDE: IntelliJ IDEA Ultimate
    - OS: Windows 10
    - GIT
- Server
    - Java11
    - Spring Boot 2.4.0
    - JPA
    - MariaDB
    - Redis
    - Gradle
    - Junit5 
    - Postman
    
실행하기 전 MariaDB, Redis 설치가 필요.
- [mardiaDB 접속정보]
    - database : kakaopay
    - username : root
    - password : 7777

---

##핵심 문제해결 전략
- 1주일의 짧은 기간이지만, 최우선적으로 과제에서 고려한 사항은 다음과 같습니다.
    - **왜 만드는지에 대한 생각**: 요청 금액에서 인원수에 따라 랜덤으로 금액을 분배 후 분배된 금액에서 다시 셔플한 후 딱 1명만 최고 금액을 받을 수 있도록 하여 실제 사용자가 필요로 하는 기능 추가 개발
    - **예외처리에 대한 생각**: 과제에서 제시하는 뿌리기, 받기, 조회에 대한 제약사항 모두 예외처리 후 그외 개인적으로 추가 필요하다고 생각한 예외처리를 더 작업함.
    - **성능에 대한 생각**: 캐시 기능을 도입 해 캐시에 대한 데이터가 있을 경우에는 캐시 정보로 캐시 데이터가 없을 경우 db 정보로 제공

    #### 뿌리기 기능
    - 뿌리기 제약조건에 따라 실패는 모두 예외처리를 진행 함.
    - 고유 token 발급을 위해 RandomStringUtils.randomAlphabetic 활용, db token 체크를 해 중복 여부를 검사 
    - 뿌릴 금액을 인원수에 맞게 랜덤 알고리즘으로 분배를 진행, 분배 후 셔플 알고리즘까지 진행을 해 랜덤으로 저장되도록 함.
    
    #### 뿌린 금액 받기 기능
    - 받기 제약조건에 따라 실패는 모두 예외처리를 진행 함.
    - 최고금액 찾는 알고리즘을 통해 인원수에서 딱 1명만 최고금액을 지급 받을 수 있도록 함.
    
    #### 조회 기능
    - 조회 제약조건에 따라 실패는 모두 예외처리를 진행 함.
    - 성능을 고려해 캐시에 데이터가 있다면 캐시의 정보를 제공하고, 캐시에 데이터가 없을 경우에만 db 조회를 통해 정보 전달.
---

## ER 다이어그램
![kakaopayErd](https://user-images.githubusercontent.com/51776656/99923042-38121780-2d77-11eb-8c36-881e46a50519.png)

---

## API 명세

#### 뿌리기 API
```
- URL 
POST 
http://localhost:8080/v1/api/allocateMoney
```
```
- Header 
X-USER-ID (Int), X-ROOM-ID (String)
```
```
- Body 
{"money":1000, "count":3}
```

- Request Parameters

    ##### 요청
    | 이름 |  타입  | 필수 | 설명        |
    | ---- | :----: | :---: | ----------- |
    | money | int | ○ | 뿌릴 금액 |
    | count | int | ○ | 뿌릴 인원수 |

    ##### 응답
    | 이름 |  타입  | 필수 | 설명        |
    | ---- | :----: | :---: | ----------- |
    | token | String | ○ | 토큰 |
    | data | Object |  | 응답 정보 |
    | resultCode | String | ○ | 응답 코드 |
    | resultMessage | String | ○ | 응답 메시지 |
    
    응답 예시)
    
    ```json
    {
        "token": "HrY",
        "data": {
            "resultCode": "0000",
            "resultMessage": "정상 처리되었습니다."
        }
    }
    ```

### 받기 API
```
- URL 
PUT http://localhost:8080/v1/api/allocateMoney/{:token}
```
```
- Header 
X-USER-ID (Int), X-ROOM-ID (String)
```
```
- Body
없음
```

- Request Parameters

    ##### 요청
    | 이름 |  타입  | 필수 | 설명        |
    | ---- | :----: | :---: | ----------- |
    | token | String | ○ | 토큰 |    

    ##### 응답
    | 이름 |  타입  | 필수 | 설명        |
    | ---- | :----: | :---: | ----------- |
    | money | int | ○ | 받기 금액 |
    | message | String | ○ | 최고 금액 메세지 |
    | data | Object |  | 응답 정보 |
    | resultCode | String | ○ | 응답 코드 |
    | resultMessage | String | ○ | 응답 메시지 |
    
    최고 금액 받기 응답 예시)
    ```json
    {
        "money": 502,
        "message": "축하합니다. 최고 금액을 받았습니다!",
        "data": {
            "resultCode": "0000",
            "resultMessage": "정상 처리되었습니다."
        }
    }
    ```
    일반 금액 받기 응답 예시)
    ```json
    {
        "money": 311,
        "resultCode": {
            "code": "0000",
            "message": "정상 처리되었습니다."
        }
    }
    ```

### 조회 API
```
- URL 
GET http://localhost:8080/v1/api/allocateMoney/{:token}
```
```
- Header 
X-USER-ID (Int), X-ROOM-ID (String)
```
```
- Body
없음
```

- Request Parameters

    ##### 요청
    | 이름 |  타입  | 필수 | 설명        |
    | ---- | :----: | :---: | ----------- |
    | token | String | ○ | 토큰 |    

    ##### 응답
    | 이름 |  타입  | 필수 | 설명        |
    | ---- | :----: | :---: | ----------- |
    | allocateMoneyTime | String | ○ | 뿌린 시각 |
    | allocateMoney | int | ○ | 뿌린 금액 |
    | receiveMoney | int | ○ | 받기 완료된 금액 |
    | receiveMoneyInfo | Array |  | 받기 완료된 정보 |
    | splitMoney | int | ○ | 받은 금액 |
    | receiverId | int | ○ | 받은 사용자 아이디 |
    | data | Object |  | 응답 정보 |
    | resultCode | String | ○ | 응답 코드 |
    | resultMessage | String | ○ | 응답 메시지 |
    
    최고 금액 받기 응답 예시)
    ```json
    {
        "allocateMoneyTime": "2020-11-23T14:24:23",
        "allocateMoney": 1000,
        "receiveMoney": 1000,
        "receiveMoneyInfo": [
            {
                "splitMoney": 961,
                "receiverId": 10
            },
            {
                "splitMoney": 12,
                "receiverId": 2
            },
            {
                "splitMoney": 27,
                "receiverId": 3
            }
        ],
        "data": {
            "resultCode": "0000",
            "resultMessage": "정상 처리되었습니다."
        }
    }
    ```
  
### API 응답코드
| 코드 |  상태  | 메시지        |
| ---- | :----: | ----------- |
| 0000 | 공통 | 정상 처리되었습니다. |
| 1001 | 공통 | 잘못된 요청입니다. |
| 1002 | 공통 | 요청 시간이 초과되었습니다. |
| 9000 | 공통 | 예상치 않은 오류가 발생하였습니다. |
| 9001 | 공통 | 데이터베이스에 오류가 있습니다. 잠시 후 다시 시도해 주세요. |
| 1003 | 뿌리기 영역 | 토큰 생성이 실패했습니다. 잠시 후 다시 시도해 주세요. |
| 1004 | 뿌리기 영역 | 인원수보다 많은 금액을 입력하세요. |
| 1005 | 뿌리기 영역 | 이미 신청한 뿌리기가 진행중에 있습니다. |
| 1006 | 받기 영역 | 뿌리기 금액 상세 정보가 없습니다. |
| 1007 | 받기 영역 | 뿌리기 당 한 사용자는 한번만 받을 수 있습니다. |
| 1008 | 받기 영역 | 자신이 뿌리기한 건은 자신이 받을 수 없습니다. |
| 1009 | 받기 영역 | 뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다. |
| 1010 | 받기 영역 | 제한시간 10분 초과로 받을 수 없습니다. |
| 1011 | 받기 영역 | 더 이상 받을 뿌리기가 없습니다. |
| 1012 | 조회 영역 | 자신이 신청한 뿌리기만 조회 가능합니다. |
| 1013 | 조회 영역 | 조회한 뿌리기는 7일이 지나 만료되었습니다. |

---