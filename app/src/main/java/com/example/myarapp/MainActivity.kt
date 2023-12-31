package com.example.myarapp

import android.icu.text.Transliterator
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myarapp.ui.theme.MyArAppTheme
import com.google.ar.core.Config
import com.google.ar.core.Plane
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import kotlinx.coroutines.launch

//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.systemBarsPadding
//import androidx.compose.material3.Text
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.myarapp.ui.theme.MyArAppTheme
//import com.google.android.filament.Engine
//import com.google.ar.core.Anchor
//import com.google.ar.core.Config
//import com.google.ar.core.Frame
//import com.google.ar.core.Plane
//import com.google.ar.core.TrackingFailureReason
//import io.github.sceneview.ar.ARScene
//import io.github.sceneview.ar.arcore.createAnchorOrNull
//import io.github.sceneview.ar.arcore.getUpdatedPlanes
//import io.github.sceneview.ar.arcore.isValid
//import io.github.sceneview.ar.getDescription
//import io.github.sceneview.ar.node.AnchorNode
//import io.github.sceneview.ar.rememberARCameraNode
//import io.github.sceneview.loaders.MaterialLoader
//import io.github.sceneview.loaders.ModelLoader
//import io.github.sceneview.model.ModelInstance
//import io.github.sceneview.node.CubeNode
//import io.github.sceneview.node.ModelNode
//import io.github.sceneview.rememberCollisionSystem
//import io.github.sceneview.rememberEngine
//import io.github.sceneview.rememberMaterialLoader
//import io.github.sceneview.rememberModelLoader
//import io.github.sceneview.rememberNodes
//import io.github.sceneview.rememberOnGestureListener
//import io.github.sceneview.rememberView

//private const val kModelFile = "models/damaged_helmet.glb"
//private const val kMaxModelInstances = 10
//
//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            MyArAppTheme {
//                // A surface container using the 'background' color from the theme
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                ) {
//                    // The destroy calls are automatically made when their disposable effect leaves
//                    // the composition or its key changes.
//                    val engine = rememberEngine()
//                    val modelLoader = rememberModelLoader(engine)
//                    val materialLoader = rememberMaterialLoader(engine)
//                    val cameraNode = rememberARCameraNode(engine)
//                    val childNodes = rememberNodes()
//                    val view = rememberView(engine)
//                    val collisionSystem = rememberCollisionSystem(view)
//
//                    var planeRenderer by remember { mutableStateOf(true) }
//
//                    val modelInstances = remember { mutableListOf<ModelInstance>() }
//                    var trackingFailureReason by remember {
//                        mutableStateOf<TrackingFailureReason?>(null)
//                    }
//                    var frame by remember { mutableStateOf<Frame?>(null) }
//                    ARScene(
//                        modifier = Modifier.fillMaxSize(),
//                        childNodes = childNodes,
//                        engine = engine,
//                        view = view,
//                        modelLoader = modelLoader,
//                        collisionSystem = collisionSystem,
//                        sessionConfiguration = { session, config ->
//                            config.depthMode =
//                                when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
//                                    true -> Config.DepthMode.AUTOMATIC
//                                    else -> Config.DepthMode.DISABLED
//                                }
//                            config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
//                            config.lightEstimationMode =
//                                Config.LightEstimationMode.ENVIRONMENTAL_HDR
//                        },
//                        cameraNode = cameraNode,
//                        planeRenderer = planeRenderer,
//                        onTrackingFailureChanged = {
//                            trackingFailureReason = it
//                        },
//                        onSessionUpdated = { session, updatedFrame ->
//                            frame = updatedFrame
//
//                            if (childNodes.isEmpty()) {
//                                updatedFrame.getUpdatedPlanes()
//                                    .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
//                                    ?.let { it.createAnchorOrNull(it.centerPose) }?.let { anchor ->
//                                        childNodes += createAnchorNode(
//                                            engine = engine,
//                                            modelLoader = modelLoader,
//                                            materialLoader = materialLoader,
//                                            modelInstances = modelInstances,
//                                            anchor = anchor
//                                        )
//                                    }
//                            }
//                        },
//                        onGestureListener = rememberOnGestureListener(
//                            onSingleTapConfirmed = { motionEvent, node ->
//                                if (node == null) {
//                                    val hitResults = frame?.hitTest(motionEvent.x, motionEvent.y)
//                                    hitResults?.firstOrNull {
//                                        it.isValid(
//                                            depthPoint = false,
//                                            point = false
//                                        )
//                                    }?.createAnchorOrNull()
//                                        ?.let { anchor ->
//                                            planeRenderer = false
//                                            childNodes += createAnchorNode(
//                                                engine = engine,
//                                                modelLoader = modelLoader,
//                                                materialLoader = materialLoader,
//                                                modelInstances = modelInstances,
//                                                anchor = anchor
//                                            )
//                                        }
//                                }
//                            })
//                    )
//                    Text(
//                        modifier = Modifier
//                            .systemBarsPadding()
//                            .fillMaxWidth()
//                            .align(Alignment.TopCenter)
//                            .padding(top = 16.dp, start = 32.dp, end = 32.dp),
//                        textAlign = TextAlign.Center,
//                        fontSize = 28.sp,
//                        color = Color.White,
//                        text = trackingFailureReason?.let {
//                            it.getDescription(LocalContext.current)
//                        } ?: if (childNodes.isEmpty()) {
//                            stringResource(R.string.point_your_phone_down)
//                        } else {
//                            stringResource(R.string.tap_anywhere_to_add_model)
//                        }
//                    )
//                }
//            }
//        }
//    }
//
//    fun createAnchorNode(
//        engine: Engine,
//        modelLoader: ModelLoader,
//        materialLoader: MaterialLoader,
//        modelInstances: MutableList<ModelInstance>,
//        anchor: Anchor
//    ): AnchorNode {
//        val anchorNode = AnchorNode(engine = engine, anchor = anchor)
//        val modelNode = ModelNode(
//            modelInstance = modelInstances.apply {
//                if (isEmpty()) {
//                    this += modelLoader.createInstancedModel(kModelFile, kMaxModelInstances)
//                }
//            }.removeLast(),
//            // Scale to fit in a 0.5 meters cube
//            scaleToUnits = 0.5f
//        ).apply {
//            // Model Node needs to be editable for independent rotation from the anchor rotation
//            isEditable = true
//        }
//        val boundingBoxNode = CubeNode(
//            engine,
//            size = modelNode.extents,
//            center = modelNode.center,
//            materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
//        ).apply {
//            isVisible = false
//        }
//        modelNode.addChildNode(boundingBoxNode)
//        anchorNode.addChildNode(modelNode)
//
//        listOf(modelNode, anchorNode).forEach {
//            it.onEditingChanged = { editingTransforms ->
//                boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
//            }
//        }
//        return anchorNode
//    }
//}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyArAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ARComposable()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyArAppTheme {
        ARComposable()
    }
}

//Model
private const val kModelFile= "https://sceneview.github.io/assets/models/DamagedHelmet.glb"
private const val kModelFile2= "https://github.com/CodeSistency/ArApp_android/tree/main/app/assets/models/monumento_a_simon_bolivar.glb"



@Composable
fun ARComposable() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        var isLoading by remember { mutableStateOf(false) }
        var planeRenderer by remember { mutableStateOf(true) }
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val childNodes = rememberNodes()
        val coroutineScope = rememberCoroutineScope()

        ARScene(
            modifier = Modifier.fillMaxSize(),
            childNodes = childNodes,
            engine = engine,
            modelLoader = modelLoader,
            planeRenderer = planeRenderer,
            onSessionConfiguration = { session, config ->
                config.depthMode =
                    when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                        true -> Config.DepthMode.AUTOMATIC
                        else -> Config.DepthMode.DISABLED
                    }
                config.instantPlacementMode = Config.InstantPlacementMode.DISABLED
                config.lightEstimationMode =
                    Config.LightEstimationMode.ENVIRONMENTAL_HDR
            },
            onSessionUpdated = { _, frame ->
                if (childNodes.isNotEmpty()) return@ARScene

                frame.getUpdatedPlanes()
                    .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                    ?.let { plane ->
                        isLoading = true
                        childNodes += AnchorNode(
                            engine = engine,
                            anchor = plane.createAnchor(plane.centerPose)
                        ).apply {
                            isEditable = true
                            coroutineScope.launch {
                                modelLoader.loadModelInstance(
//                                    fileLocation = "models/damaged_helmet.glb",
                                    kModelFile


                                )?.let {
                                    addChildNode(
                                        ModelNode(
                                            modelInstance = it,
                                            // Scale to fit in a 0.5 meters cube
                                            scaleToUnits = 0.5f,
                                            // Bottom origin instead of center so the
                                            // model base is on floor
                                            centerOrigin = Position(y = -0.5f)
                                        ).apply {
                                            isEditable = true
                                        }
                                    )
                                }
                                planeRenderer = false
                                isLoading = false
                            }
                        }
                    }
            }
        )
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                color = Color.Magenta
            )
        }
    }
}