# 우아한형제들 Java/Spring Framework 과제 제출 - 고재헌

## 시작하기

1. 터미널을 통해, 압축폴더를 해제한 한 위치로 이동합니다.
2. docker-compose를 통해 MySQL을 설치 및 실행합니다.
<br> `$ docker-compose up -d`
4. 서버 빌드 및 실행을 시켜줍니다. (빌드 시 Test 진행)
<br> `$ ./gradlew build`
<br> `$ ./java -jar ./build/libs/BookRecord-0.0.1-SNAPSHOT.jar`
5. 빌드 및 실행이 잘 안될 경우, 아래와 같이 입력 후 4번 내용을 다시 입력합니다.
<br> `$ ./gradlew clean`
<br>

## 추가 설명
### <공통>
![image](https://user-images.githubusercontent.com/72333462/121840903-39f1f700-cd18-11eb-8537-04709ea1db77.png)
###### (출처 : https://dahye-jeong.gitbook.io/spring/spring/2020-04-12-layer)

1. 위의 그림과 같이 구현하기 위해 노력했습니다.
2. 상위 폴더에 `logback-yyyy-MM-dd.i.log`파일에 **데이터 생성, 수정**에 대한 info log가 100MB단위로 저장되어 나갑니다.
<br> 터미널에 `tail -n 1000 logback-yyyy-MM-dd.i.log` 입력하면, 최근 1000줄 log를 볼 수 있습니다.
3. 모든 테이블의 Primary Key는 각각의 **id**입니다.
4. 모든 테이블의 수정 api에서는 바꾸고자 하는 값들이 기존 값들과 모두 같으면, 수정할 정보가 없다는 error를 발생시키며 수정이 일어나지 않도록 했습니다.

### <사용자>
1. 이미 가입된 email은 가입되지 않도록 했습니다.
-> 중복생성을 막아 자원을 효율적으로 관리하기 위함입니다.
### <책>
1. 책의 제목, 저자, 출판사가 모두 같으면 중복생성이 되지 않도록 했습니다.
-> 중복생성을 막아 자원을 효율적으로 관리하기 위함입니다.
2. 저자에 쉼표(,)가 들어가면 2인 이상이라고 생각하여, 생성되지 않도록 했습니다.
-> 문제에서 주어진 조건을 저는 쉼표로 구분했습니다.
### <발췌문>
1. 발췌문은 사용자ID, 책ID, 페이지번호, 발췌한 내용이 다 같아도 저장할 수 있도록 했습니다. (중복생성 허용)
-> 이 부분은 진짜 서비스라고 했을 때, 좋은 내용이라면 두 번이라도 스크랩해서 보관할 수 있겠다고 생각했습니다.

