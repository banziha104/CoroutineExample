package com.example.coroutineexample

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CoroutineTests {
    @Test
    fun test(){
        runBlocking {
            println(coroutineContext) // "coroutine#1":BlockingCoroutine{Active}@7bf3a5d8
            println(Thread.currentThread().name)
            println("Hello")
        }
    }

    @Test
    fun test_2(){
        runBlocking {
            launch {
                println("launch : ${Thread.currentThread().name}")
                println("World!")
            }
            println("runBlocking : ${Thread.currentThread().name}")
            println("Hello!")
        }
    }

    @Test
    fun test_3(){
        runBlocking {
            launch {
                println("launch : ${Thread.currentThread().name}") // 2
                println("World!") // 3
            }
            println("runBlocking : ${Thread.currentThread().name}") // 1
            delay(500L) // 2 : 아래 구문은
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
            Thread.sleep(500L) // 2 : 아래 구문은
            println("Hello!") // 3
        }
    }


    @Test
    fun test_5(){
        runBlocking {
            val job = launch {
                println("launch : ${Thread.currentThread().name}") // 4
                println("World!") // 5
            }
            println("1") // job보다 먼저실행
            job.join()
            println("runBlocking : ${Thread.currentThread().name}") // 1
            Thread.sleep(500L) // 2 : 아래 구문은
            println("Hello!") // 3
        }
    }
}