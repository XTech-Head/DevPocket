package com.xtech.xdevpocket.domain.utilities

import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

data class ColorConversion(
    val hex: String,
    val rgb: String,
    val hsl: String,
)

sealed class ColorResult {
    data class Success(val conversion: ColorConversion) : ColorResult()
    data class Error(val message: String) : ColorResult()
}

/**
 * Parses hex, rgb(...) or hsl(...) input and returns all three representations.
 * Never crashes on malformed input — always returns a clear error instead.
 */
object ColorConverterUtility {

    fun convert(input: String): ColorResult {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return ColorResult.Error("Input is empty.")

        val rgbTriple = when {
            trimmed.startsWith("#") || trimmed.matches(Regex("^[0-9a-fA-F]{3,8}$")) -> parseHex(trimmed)
            trimmed.startsWith("rgb", ignoreCase = true) -> parseRgbFunction(trimmed)
            trimmed.startsWith("hsl", ignoreCase = true) -> parseHslFunction(trimmed)
            else -> null
        } ?: return ColorResult.Error(
            "Unable to parse color.\n\nUse a hex (#RRGGBB), rgb(r,g,b), or hsl(h,s%,l%) value."
        )

        val (r, g, b) = rgbTriple
        val hex = "#%02X%02X%02X".format(r, g, b)
        val rgbText = "rgb($r, $g, $b)"
        val (h, s, l) = rgbToHsl(r, g, b)
        val hslText = "hsl($h, $s%, $l%)"

        return ColorResult.Success(ColorConversion(hex, rgbText, hslText))
    }

    private fun parseHex(raw: String): Triple<Int, Int, Int>? {
        val hex = raw.removePrefix("#")
        val expanded = if (hex.length == 3) hex.map { "$it$it" }.joinToString("") else hex
        if (expanded.length < 6) return null
        return try {
            val r = expanded.substring(0, 2).toInt(16)
            val g = expanded.substring(2, 4).toInt(16)
            val b = expanded.substring(4, 6).toInt(16)
            Triple(r, g, b)
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun parseRgbFunction(raw: String): Triple<Int, Int, Int>? {
        val nums = Regex("[\\d.]+").findAll(raw).map { it.value.toFloatOrNull() }.toList()
        if (nums.size < 3 || nums.any { it == null }) return null
        val (r, g, b) = nums
        return Triple(
            r!!.roundToInt().coerceIn(0, 255),
            g!!.roundToInt().coerceIn(0, 255),
            b!!.roundToInt().coerceIn(0, 255),
        )
    }

    private fun parseHslFunction(raw: String): Triple<Int, Int, Int>? {
        val nums = Regex("[\\d.]+").findAll(raw).map { it.value.toFloatOrNull() }.toList()
        if (nums.size < 3 || nums.any { it == null }) return null
        val h = nums[0]!!
        val s = nums[1]!! / 100f
        val l = nums[2]!! / 100f
        return hslToRgb(h, s, l)
    }

    private fun hslToRgb(h: Float, s: Float, l: Float): Triple<Int, Int, Int> {
        if (s == 0f) {
            val v = (l * 255).roundToInt()
            return Triple(v, v, v)
        }
        val q = if (l < 0.5f) l * (1 + s) else l + s - l * s
        val p = 2 * l - q
        val hk = (h % 360) / 360f
        fun hueToRgb(pp: Float, qq: Float, t: Float): Float {
            var tt = t
            if (tt < 0) tt += 1f
            if (tt > 1) tt -= 1f
            return when {
                tt < 1f / 6f -> pp + (qq - pp) * 6f * tt
                tt < 1f / 2f -> qq
                tt < 2f / 3f -> pp + (qq - pp) * (2f / 3f - tt) * 6f
                else -> pp
            }
        }
        val r = hueToRgb(p, q, hk + 1f / 3f)
        val g = hueToRgb(p, q, hk)
        val b = hueToRgb(p, q, hk - 1f / 3f)
        return Triple((r * 255).roundToInt(), (g * 255).roundToInt(), (b * 255).roundToInt())
    }

    private fun rgbToHsl(r: Int, g: Int, b: Int): Triple<Int, Int, Int> {
        val rf = r / 255f
        val gf = g / 255f
        val bf = b / 255f
        val maxV = max(rf, max(gf, bf))
        val minV = min(rf, min(gf, bf))
        val l = (maxV + minV) / 2f

        if (maxV == minV) return Triple(0, 0, (l * 100).roundToInt())

        val d = maxV - minV
        val s = if (l > 0.5f) d / (2f - maxV - minV) else d / (maxV + minV)
        val h = when (maxV) {
            rf -> ((gf - bf) / d + (if (gf < bf) 6f else 0f))
            gf -> (bf - rf) / d + 2f
            else -> (rf - gf) / d + 4f
        } * 60f

        return Triple(h.roundToInt(), (s * 100).roundToInt(), (l * 100).roundToInt())
    }
}
