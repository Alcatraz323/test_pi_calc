package io.alcatraz.mathematics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpView()
        calcMonteCarloFromKotlin(100000000)
    }

    private fun setUpView() {
        supportActionBar!!.setSubtitle(R.string.subtitle)
    }
    fun calcBBPFromCpp(precision: Int) {
        settings.text = String.format(getString(R.string.format), precision,  "BBP-C++")
        val timeStart = System.currentTimeMillis()
        val resultStr = String.format(
            "Decimal:%s\nHex:%s",
            PiApi.nativeBBPDecimal(precision),
            PiApi.nativeBBPHex(precision)
        )
        val timeEnd = System.currentTimeMillis()
        time_elapsed.text = String.format(getString(R.string.time_elapsed), timeEnd - timeStart)
        result.text = resultStr
    }

    fun calcBBPFromKotlin(precision: Int) {
        settings.text = String.format(getString(R.string.format), precision, "BBP-Kotlin")
        val timeStart = System.currentTimeMillis()
        val bbpResult = PiApi.calcBBP(precision)
        val resultStr = String.format(
            "Decimal:%s\nHex:%s",
            bbpResult,
            java.lang.Double.toHexString(bbpResult.toDouble())
        )
        val timeEnd = System.currentTimeMillis()
        time_elapsed.text = String.format(getString(R.string.time_elapsed), timeEnd - timeStart)
        result.text = resultStr
    }

    fun calcAGMFromKotlin(iterationTimes: Int) {
        settings.text = String.format(getString(R.string.format), iterationTimes, "AGM-Kotlin")
        val timeStart = System.currentTimeMillis()
        val agmResult = PiApi.calcAGM(iterationTimes)
        val resultStr = String.format(
            "Decimal:%s\nHex:%s",
            agmResult,
            java.lang.Double.toHexString(agmResult.toDouble())
        )
        val timeEnd = System.currentTimeMillis()
        time_elapsed.text = String.format(getString(R.string.time_elapsed), timeEnd - timeStart)
        result.text = resultStr
    }

    fun calcMonteCarloFromKotlin(iterationTimes: Long) {
        settings.text = String.format(getString(R.string.format), iterationTimes, "MonteCarlo-Kotlin")
        val timeStart = System.currentTimeMillis()
        PiApi.calcMonteCarlo(iterationTimes, object : PiApi.MonteCarloCallback{
            override fun onFinished(pi: BigDecimal) {
                runOnUiThread {
                    val resultStr = String.format(
                        "Decimal:%s\nHex:%s",
                        pi,
                        java.lang.Double.toHexString(pi.toDouble())
                    )
                    val timeEnd = System.currentTimeMillis()
                    time_elapsed.text = String.format(getString(R.string.time_elapsed), timeEnd - timeStart)
                    result.text = resultStr
                }
            }
        })

    }
}

