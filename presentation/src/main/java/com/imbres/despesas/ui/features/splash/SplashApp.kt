package com.imbres.despesas.ui.features.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.imbres.despesas.R

@Composable
fun SplashApp(
    onGoToNextScreen: () -> Unit,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.logo))
    val progress by animateLottieCompositionAsState(composition)

/*
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            //modifier = Modifier.padding(bottom = 250.dp),
            fontSize = 36.sp,
            color = colorResource(R.color.black),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.fillMaxWidth().padding(top = 350.dp))
    }

    LottieAnimation(
        composition = composition,
        progress = { progress },
        //modifier = Modifier.padding(top=250.dp)
    )
*/

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.background_app_bar)),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier
                .weight(0.6f)
                .align(Alignment.CenterHorizontally),
            fontSize = 16.sp,
            color = Color.Red,
        )

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.weight(0.4f)
        )
    }

    LaunchedEffect(progress) {
        if (progress >= 1) {
            onGoToNextScreen()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    SplashApp(onGoToNextScreen = {})
}