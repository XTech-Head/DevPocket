package com.xtech.xdevpocket.presentation.screens.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.R
import kotlinx.coroutines.delay

/**
 * Branded splash shown right after the brief system splash (see
 * Theme.XDevPocket.Splash / MainActivity#installSplashScreen). Unlike the
 * system splash — which can only show a small masked icon — this has full
 * control, so it's where the real x-DevPocket logo (with tagline) gets shown.
 *
 * Purely time-based: fades the logo in, holds briefly, then hands off to
 * [onFinished]. No loading/auth state to wait on since everything here is
 * on-device.
 */
@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var alpha by remember { mutableFloatStateOf(0f) }
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 450),
        label = "splash_logo_alpha",
    )

    LaunchedEffect(Unit) {
        alpha = 1f
        delay(1400)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_xdevpocket),
            contentDescription = "x-DevPocket",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { this.alpha = animatedAlpha },
        )
    }
}
