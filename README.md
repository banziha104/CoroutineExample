# 코루틴

## 필요성 

1. 비선점형 멀티태스킹(합의)
   - 자원을 서로 나누어 쓰는 것이 비선점 멀티태스킹
   - 자원을 독점하고 크래쉬나느 경우가 많음 
2. 선점형 멀티태스킹(중재)
   - Ring 0의 권한을 가진 운영체제가 나누어 주기로
   - 응용프래그램은 Ring 3 
   - Ring 1-2는 제대로 활용 안됨
   - Ring 레벨이 높은 곳에서 낮은 곳으로만 접근 가능
   - 프로세스 
      - 운영체제는 1개 이상의 프로세스를 시분할에 의해 번갈아 동작 시킴.
      - 코드, 데이터, 힙, 스택등의 자료구조를 가짐
   - 스레드
      - 프로세스는 1개 이상의 스레드를 가지며 시분할에 의해 번갈아 동작
      - 일반적으로는 스택으로 구분하고, 나머지는 공유하지만 운영체제마다 다름 
      - 리눅스에서는 프로세스와 스레드와의 차이가 크지 않음.
3. 병렬성
   - SMP (Symmetric Multiprocessor)와 가시성 
      - 여러 프로세스가 하나의 메모리를 쓰는 모델
      - 여러 프로세스가 물리적으로 여러 일을 동시에 사용하는 일이 가능해짐.
      - 프로그래밍 이슈가 생김
         - 락이나 메모리 베리어가 필요할 수 있음. 
         - 인접한 캐시라인은 한번에 한 코어에서만 접근 가능
   
<br>

## 기본


- 컨셉
    - 코루틴 스코프 : 모든 코루틴은 코루틴 스코프를 가짐
    - 코루틴 컨텍스트 : 코루틴을 처리하기 위한 정보를 가짐
    
- 빌더 
    - launch : 가능한 쓰레드를 공유하면서 실행
    - await : 
- 스코프 빌더 
    - coroutineScope : 가장 기본이 되는 스코프 빌더
- 함수
    - runBlocking : 코루틴을 만들고 코드 블록이 수행이 끝날 떄까지 runBlocking 다의 코드를 수행하지 못하도록 막음 
    - deley : 현재 스레드를 잠시 멈추고 다른 함수가 해당 스레드를 양보 후 대기
- suspend 함수 : 중단 가능한 함수 
    - suspension point : 중단점
- Job : 

```kotlin
@Test
fun test(){
    runBlocking {
        println(coroutineContext) // [CoroutineId(1), "coroutine#1":BlockingCoroutine{Active}@44be0077, BlockingEventLoop@2205a05d]
        println(this) // "coroutine#1":BlockingCoroutine{Active}@7bf3a5d8
        println(Thread.currentThread().name) // 현재 어떤 스레드에서 수행중인지 알아내는 방법, 메인 스레드 (@coroutine1)
        println("Hello")
    }
}

@Test
fun test_2(){
    runBlocking {
        launch { // runBlocking이 블로킹하고, 동일한 메인스레드를 호출하기 때문에 아래 구문보다 늦게 호출됨
            println("launch : ${Thread.currentThread().name}")
            println("World!")
        }
        println("runBlocking : ${Thread.currentThread().name}")
        println("Hello!")
    }
}

```

- delay : 현재 스레드를 잠시 멈추고 다른 함수가 해당 스레드를 양보 후 대기 

```kotlin
@Test
fun test_3(){
    runBlocking {
        launch {
            println("launch : ${Thread.currentThread().name}") // 2
            println("World!") // 3
        }
        println("runBlocking : ${Thread.currentThread().name}") // 1 
        delay(500L) // 2 : 아래 구문은 추후에 호출됨 
        println("Hello!") // 4
    }
}

@Test
fun test_4(){
    runBlocking {
        launch {
            println("launch : ${Thread.currentThread().name}") // 4
            println("World!") // 5
        }
        println("runBlocking : ${Thread.currentThread().name}") // 1
        Thread.sleep(500L) // 2 슬립은 양보하지 않아서 Hello 가 먼저 실행됨
        println("Hello!") // 3
    }
}

```