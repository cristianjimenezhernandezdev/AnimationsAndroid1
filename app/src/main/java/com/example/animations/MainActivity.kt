package com.example.animations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.layoutId
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
    }
}

@Composable
fun AppContent() {
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(top = 80.dp))//Afegeixo una mica d'espai a dalt perque no se solapi el text amb la hora i tal
    {

        Text("Benvingut a l'app d'animacions!")
        AnimatedVisibilityExample()
        //AnimatedContentExample()
        //MotionLayoutExample()
        //CircularRevealExample()
    }
}

@Composable
fun AnimatedVisibilityExample() {
    var visible by remember { mutableStateOf(true) }

    Column {
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Hide" else "Show")
        }

        AnimatedVisibility(visible) {
            CircularRevealExample()

        }
    }
}

@Composable
fun AnimatedContentExample() {
    var count by remember { mutableStateOf(0) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { count++ }) {
            Text("Increase")
        }

        AnimatedContent(targetState = count) { targetCount ->
            Text("Count: $targetCount")
        }
    }
}
@Composable
fun MotionLayoutExample() {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(1f, animationSpec = tween(2000))
    }

    MotionLayout(
        start = ConstraintSet {
            val box = createRefFor("box")
            constrain(box) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        },
        end = ConstraintSet {
            val box = createRefFor("box")
            constrain(box) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        },
        progress = progress.value
    ) {
        Box(
            Modifier
                .size(60.dp)
                .background(Color.Red)
                .layoutId("box")
        )
    }
}

@Composable
fun GraphicsLayerExample() {
    var rotation by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(16)
            rotation += 2f
        }
    }

    Box(
        modifier = Modifier
            .size(100.dp)
            .graphicsLayer(rotationZ = rotation)
            .background(Color.Green)
    )
}

@Composable
fun CircularRevealExample() {
    val radius = remember { Animatable(0f) }
    var rotation by remember { mutableStateOf(0f) }//per la rotacio

    // infinit de color
    val infiniteTransition = rememberInfiniteTransition()

    val color by infiniteTransition.animateColor(
        initialValue = Color.White,
        targetValue = Color.Black,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )
//Afegeixo un infinite perque doni voltes al eix Y i per tant es mogui
    val offsetY by infiniteTransition.animateFloat(
        initialValue = -100f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        )

    )

    LaunchedEffect(Unit) {
        radius.animateTo(500f, tween(1000))
    }
//afegeixo efecte de rotacio
    LaunchedEffect(Unit) {
        while (true) {
            delay(16)
            rotation += 2f
        }}
    //Poso un box per poder posar el nom també amb el offset faig la translacio peruq es mogui de dalt a baix
    Box(modifier = Modifier.fillMaxSize().offset(y = offsetY.dp).graphicsLayer(rotationZ = rotation)){

    Canvas(modifier = Modifier.fillMaxSize()) {
        clipPath(Path().apply {
            addOval(Rect(center = Offset(size.width / 2, size.height / 2), radius = radius.value))
        }) {
            //poso color que és el infinite i trec el cyan d'abans
            drawRect(color)
        }
    }
    Text(text = "Cristian",
    modifier = Modifier.align(Alignment.Center),
    color = Color.Blue)}

}



fun Modifier.Companion.align(center: Alignment): Modifier {


    return TODO("Provide the return value")
}


@Preview(showBackground = true)
@Composable
fun PreviewAppContent() {
    AppContent()
}
