package io.alcatraz.mathematics

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.sqrt

object PiApi {
    fun calcBBP(precision: Int): BigDecimal {
        val EIGHT = BigDecimal(8)
        var result = BigDecimal(0)
        for (iterator in 0..precision) {
            val iteratorBigDecimal = BigDecimal(iterator)
            val precisionOffset =
                BigDecimal(1).divide(
                    BigDecimal(16).pow(iterator), MathContext(
                        10,            //本次除法经确到到小数点后一位
                        RoundingMode.HALF_DOWN  //舍5进6
                    )
                )//计算1*16^k
            val s1 = BigDecimal(4).divide(
                EIGHT.multiply(iteratorBigDecimal).add(BigDecimal(1)), MathContext(
                    1000,              //本次除法经确到千分
                    RoundingMode.HALF_DOWN      //舍5进6
                )
            )// 计算4/(8k+1)
            val s2 = BigDecimal(2).divide(
                EIGHT.multiply(iteratorBigDecimal).add(BigDecimal(4)), MathContext(
                    1000,              //本次除法经确到千分
                    RoundingMode.HALF_DOWN      //舍5进6
                )
            )// 计算2/(8k+4)
            val s3 = BigDecimal(1).divide(
                EIGHT.multiply(iteratorBigDecimal).add(BigDecimal(5)), MathContext(
                    1000,              //本次除法经确到千分
                    RoundingMode.HALF_DOWN      //舍5进6
                )
            )// 计算1/(8k+5)
            val s4 = BigDecimal(1).divide(
                EIGHT.multiply(iteratorBigDecimal).add(BigDecimal(6)), MathContext(
                    1000,              //本次除法经确到千分
                    RoundingMode.HALF_DOWN      //舍5进6
                )
            )// 计算1/(8k+6)

            //计算当前循环结果
            val currentResult = precisionOffset.multiply(s1.subtract(s2).subtract(s3).subtract(s4))
            //添加当前位数结果
            result = result.add(currentResult)
        }
        result = result.setScale(precision, BigDecimal.ROUND_DOWN)  //舍去不准确位数
        return result
    }

    @Suppress("UNUSED_VARIABLE", "LocalVariableName", "ReplaceJavaStaticMethodWithKotlinAnalog")
    fun calcAGM(iterationTimes: Int): BigDecimal {
        var result: BigDecimal
        //Initialize
        val a0 = BigDecimal(1)
        val b0 = BigDecimal(1).divide(
            BigDecimal(Math.sqrt(2.0), MathContext.DECIMAL128),
            128,
            BigDecimal.ROUND_HALF_UP
        )
        val t0 = BigDecimal(0.25)
        val p0 = BigDecimal(1)
        var a_n = BigDecimal(1)
        var b_n = BigDecimal(1).divide(
            BigDecimal(Math.sqrt(2.0), MathContext.DECIMAL128),
            128,
            BigDecimal.ROUND_HALF_UP
        )
        var t_n = BigDecimal(0.25)
        var p_n = BigDecimal(1)
        var a_np1: BigDecimal
        var b_np1: BigDecimal
        var temp: BigDecimal
        var t_np1: BigDecimal
        var p_np1: BigDecimal
        for (iterator in 1..iterationTimes) {
            a_np1 = a_n.add(b_n).divide(BigDecimal(2))
            b_np1 = BigDecimal(sqrt(a_n.multiply(b_n).toDouble()))
            temp = a_n.subtract(a_np1)
            t_np1 = t_n.subtract(p_n.multiply(temp.multiply(temp)))
            p_np1 = p_n.multiply(BigDecimal(2))
            a_n = a_np1
            b_n = b_np1
            t_n = t_np1
            p_n = p_np1
        }
        val temp2 = a_n.add(b_n)
        result =
            temp2.multiply(temp2).divide(t_n.multiply(BigDecimal(4)), 128, BigDecimal.ROUND_HALF_UP)
        return result
    }

    interface MonteCarloCallback {
        fun onFinished(pi: BigDecimal)
    }

    fun calcMonteCarlo(tryTimes: Long, callback: MonteCarloCallback) {
        var hitTimes = 0
        var totalTrial = 0
        var callThreadFinished = 0
        val eachThreadTask = tryTimes / 12
        for (iterator: Int in 0..11) {      //12线程计算
            Thread(Runnable {
                var threadHitTimes = 0
                var threadTotalTrial = 0
                for (t_iterator in 0..eachThreadTask) {
                    val a = Math.random()
                    val b = Math.random()
                    val c = 1
                    if ((a + b) > c && (a * a + b * b) < (c * c)) {
                        threadHitTimes++
                    }
                    threadTotalTrial++
                }
                synchronized(hitTimes) {
                    hitTimes += threadHitTimes
                }
                synchronized(totalTrial) {
                    totalTrial += threadTotalTrial
                }
                synchronized(callThreadFinished) {
                    callThreadFinished++
                    if (callThreadFinished == 12) {
                        val p = BigDecimal(hitTimes).divide(
                            BigDecimal(totalTrial),
                            128,
                            BigDecimal.ROUND_HALF_DOWN
                        )
                        val result = p.multiply(BigDecimal(4)).add(BigDecimal(2))
                        callback.onFinished(result)
                    }
                }
            }).start()
        }
    }

    external fun nativeBBPDecimal(precision: Int): String
    external fun nativeBBPHex(precision: Int): String

    init {
        System.loadLibrary("native-lib")
    }
}